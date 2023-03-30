package com.hazard157.psx24.core.e4.services.filesys;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.e4.services.filesys.IPsxResources.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;
import java.time.*;
import java.util.concurrent.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.derivative.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.filesys.impl.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;

/**
 * Реализация {@link IPsxFileSystem} с новой раскладкой файловых ресусров (начиная с марта 2021).
 * <p>
 * 2021-03-14 Ver3: Не-секундные изображения кадров начинаем выносить ВНЕ облачное хранилище.<br>
 * Файлов изображений кадров хранятся в следующих трех директориях (для каждого экпизода/камеры)^
 * <ul>
 * <li>{@link #episodeResRoot}/YYYY-MM-DD/{@link #EPSUBDIR_FRAMES_ANIM}/camId - анимированные GIFы;</li>
 * <li>{@link #episodeResRoot}/YYYY-MM-DD/{@link #EPSUBDIR_FRAMES_SECS}/camId - секндно-выровненные JPGы;</li>
 * <li>{@link #episodeNonsecFramesRoot}/YYYY-MM-DD/camId - все JPGы;</li>
 * </ul>
 * <b>Внимание:</b> секундно-выровненные JPGы хранятся в <b>двух</b> местах!
 * <p>
 * <b>Мотивация:</b> такое разделение на места хранения вызвано следующими мотивами:
 * <ul>
 * <li>не-секундные кадры вынесены в директорию {@link #episodeNonsecFramesRoot}, которая находится вне места
 * синхронизации в облаке hazard.157.ru, поскольку они перегружают nextcloud (этих файлов на 2021-03 более 1
 * миллиона);</li>
 * <li>анимированные и секундные кадры в {@link #episodeResRoot}, то есть, в облаке;</li>
 * <li>секндные кадры можно было не хранить вместе с не-нескундными, но их хранение удобно для создания GIFов путем
 * вызова внешней утилитыф graphicsmagick.</li>
 * </ul>
 *
 * @author goga
 */
public class PsxFileSystem
    implements IPsxFileSystem {

  /**
   * Поток загрузки изображений кадров.
   *
   * @author goga
   */
  public class FrameLoaderThread
      implements Runnable, IFrameLoaderThread {

    final IQueue<Item>         queue = new Queue<>();
    final IFrameLoadedCallback callback;

    volatile boolean done = false;

    FrameLoaderThread( IList<IFrame> aFrames, EThumbSize aThumbSize, IFrameLoadedCallback aCallback,
        boolean aImmediatelyLoadExistingThumbs ) {
      callback = aCallback;
      // пройдем по запрошеному списку
      for( IFrame frame : aFrames ) {
        File f = findFrameFile( frame );
        // если файл изображения существует, будем дальше работать
        if( f != null ) {
          // если миниатюра в кеше, вернем его
          boolean loadImmediately = imageManager.isThumbCached( f, aThumbSize );
          if( !loadImmediately ) {
            loadImmediately = aImmediatelyLoadExistingThumbs && imageManager.isThumbFile( f, aThumbSize );
          }
          if( loadImmediately ) {
            TsImage mi = imageManager.findThumb( f, aThumbSize );
            aCallback.imageLoaded( frame, mi );
          }
          else { // добавим в очередь, из которой постоянно извлекаются
            queue.putTail( new Item( frame, f, aThumbSize, aCallback ) );
          }
        }
        else { // файла нет - ивестим запросившего
          aCallback.imageLoaded( frame, null );
        }
      }
    }

    TsImage loadImage( File aImageFile, EThumbSize aThumbSize ) {
      TsImage mi = null;
      long startTime = System.currentTimeMillis();
      while( System.currentTimeMillis() - startTime < IMAGE_LOAD_TIMEOUT ) {
        mi = imageManager.findThumb( aImageFile, aThumbSize );
        if( mi != null ) {
          break;
        }
      }
      return mi;
    }

    @Override
    public void run() {
      while( !done ) {
        // берем очередной элемент запрошенных к загрузке файлов
        Item item = queue.getHeadOrNull();
        if( item == null ) {
          done = true;
          break;
        }
        try {
          // тут могут быть проблемы с многопоточным доступом к imageManager
          TsImage mi = loadImage( item.file(), item.thumbSize() );
          if( !display.isDisposed() ) {
            display.asyncExec( () -> item.callback().imageLoaded( item.frame(), mi ) );
          }
        }
        catch( Exception ex ) {
          ex.printStackTrace();
        }
      }
    }

    @Override
    public void close() {
      if( !done ) {
        done = true;
        try {
          Thread.sleep( 500 );
        }
        catch( InterruptedException ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    @Override
    public boolean isRunning() {
      return !done;
    }

  }

  /**
   * Время в миллисекундах, сколько поток ожидает загрузки файла.
   * <p>
   * Нужно для того, чтобы дать время на формирование дискового файла миниатюры.
   */
  static final long IMAGE_LOAD_TIMEOUT = 30 * 1000;

  static final String LOADER_THREAD_NAME_PREFIX = "PSX image loader #"; //$NON-NLS-1$

  static final String ROOT_DIR            = "/home/hmade/";                                    //$NON-NLS-1$
  static final File   ROOT_DIR_FILE       = new File( ROOT_DIR );
  static final String ORIGINAL_MEDIA_ROOT = "/home/clouds.arch/hazard157.ru/prisex/original/"; //$NON-NLS-1$

  static final File episodeResRoot = new File( ROOT_DIR_FILE, "episodes/" ); //$NON-NLS-1$

  static final String KDENLIVE_EXT                        = "kdenlive"; //$NON-NLS-1$
  static final String BACKUP_STRING_IN_KDENLIVE_FILE_NAME = "_backup";  //$NON-NLS-1$

  static final String EPSUBDIR_FRAMES_ANIM = "frames-anim";  //$NON-NLS-1$
  static final String EPSUBDIR_FRAMES_SECS = "frames-still"; //$NON-NLS-1$
  static final String EPSUBDIR_SRCVIDEOS   = "srcvideos";    //$NON-NLS-1$
  static final String EPSUBDIR_TRAILERS    = "trailers";     //$NON-NLS-1$
  static final String EPSUBDIR_DEVEL       = "devel";        //$NON-NLS-1$

  /**
   * Расположение изображений не-секундных кадров эпизодв, в версии Ver3 файловой системы PSX.
   */
  static final File episodeNonsecFramesRoot = new File( "/home/.psx/episodes/frames-nonsec" ); //$NON-NLS-1$

  final IEclipseContext winContext;
  final ITsImageManager imageManager;
  final Display         display;
  final IPfsFramesCache framesCache;

  private final IPfsOriginalMedia originalMedia;

  private int threadNameCounter = 0;

  /**
   * Constructor.
   *
   * @param aWinContext {@link IEclipseContext} - the windows level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PsxFileSystem( IEclipseContext aWinContext ) {
    TsNullArgumentRtException.checkNulls( aWinContext );
    winContext = aWinContext;
    imageManager = winContext.get( ITsImageManager.class );
    display = winContext.get( Display.class );
    framesCache = new PfsFramesCache();
    File origMediaRoot = new File( ORIGINAL_MEDIA_ROOT );
    if( TsFileUtils.isDirReadable( origMediaRoot ) ) {
      originalMedia = new PfsOriginalMediaExisting( origMediaRoot );
    }
    else {
      originalMedia = new PfsOriginalMediaAbsent( origMediaRoot );
    }
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  File findSourceEpisodeDir( String aEpisodeId ) {
    if( !EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( aEpisodeId ) ) {
      return null;
    }
    LocalDate epDate = EpisodeUtils.episodeId2LocalDate( aEpisodeId );
    File epDir = new File( episodeResRoot, epDate.toString() );
    if( !epDir.exists() ) {
      return null;
    }
    return epDir;
  }

  File findSourceEpisodeSubdir( String aEpisodeId, String aSubdir ) {
    File epDir = findSourceEpisodeDir( aEpisodeId );
    if( epDir != null ) {
      File framesSubdir = new File( epDir, aSubdir );
      if( TsFileUtils.isDirReadable( framesSubdir ) ) {
        return framesSubdir;
      }
    }
    return null;
  }

  // Ver3 2021-03-14
  File findNonSecFramesEpisodeDir( String aEpisodeId ) {
    LocalDate epDate = EpisodeUtils.episodeId2LocalDate( aEpisodeId );
    return new File( episodeNonsecFramesRoot, epDate.toString() );
  }

  File findFrameDir( IFrame aFrame ) {
    if( aFrame.isAnimated() ) {
      return findSourceEpisodeSubdir( aFrame.episodeId(), EPSUBDIR_FRAMES_ANIM );
    }
    if( aFrame.isSecAligned() ) {
      return findSourceEpisodeSubdir( aFrame.episodeId(), EPSUBDIR_FRAMES_SECS );
    }
    return findNonSecFramesEpisodeDir( aFrame.episodeId() );
  }

  IList<File> listCamIdSubdirs( File aDir ) {
    IList<File> allSubdirs = TsFileUtils.listChilds( aDir, TsFileFilter.FF_DIRS );
    IListEdit<File> dd = new ElemArrayList<>( allSubdirs.size() );
    for( File d : allSubdirs ) {
      if( StridUtils.isValidIdPath( d.getName() ) ) {
        dd.add( d );
      }
    }
    return dd;
  }

  /**
   * Returns the path relative to PSX file system root if file is in PSX file system.
   *
   * @param aFile {@link File} - the file (or directory)
   * @return String - relative path or <code>null</code> if argument not in PSX file system
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final String findPsxRelativePath( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    return extractRelativePath( ROOT_DIR_FILE, aFile );
  }

  // private static final String RESOURCES_ROOT = ROOT_DIR + "resources/"; //$NON-NLS-1$
  private static final String RESOURCES_SUBDIR_VIDEO_ILLS = ROOT_DIR + "cache/video-ills/"; //$NON-NLS-1$
  private static final String GIF_DOT_EXT                 = ".gif";                         //$NON-NLS-1$

  /**
   * Возвращает исходную строку, при необходимости убрав все символы разделителя директории {@link File#separatorChar} с
   * окончания строки.
   *
   * @param aPath String - исходная строка, обычно, имя директория
   * @return String - исходная строка, или исходняя строка без разделитей в конце
   * @throws TsNullArgumentRtException aPath = null
   */
  public static String removeEndingSeparator( String aPath ) {
    TsNullArgumentRtException.checkNull( aPath );
    int pathLen = aPath.length();
    if( pathLen == 0 ) {
      return aPath;
    }
    int index = pathLen;
    while( aPath.charAt( index - 1 ) == File.separatorChar ) {
      --index;
    }
    return aPath.substring( 0, index );
  }

  /**
   * Возвращает путь файла относительно к одному из директорий, в которой он находится.
   * <p>
   * Если aChildFile не находится в структру поддиректори каталога aParentDir, возвращает null. Если директрии
   * совпадают, возвращает пусту строку.
   *
   * @param aParentDir File - одна из директории в иерархии от корня до указанного файла
   * @param aChildFile File - файл (или директория), чей относительный путь ищеться
   * @return String - путь aChildFile относительно aParentDir или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aParentDir не директория
   */
  public static String extractRelativePath( File aParentDir, File aChildFile ) {
    TsNullArgumentRtException.checkNulls( aParentDir, aChildFile );
    TsIllegalArgumentRtException.checkFalse( aParentDir.isDirectory() );
    String p1 = removeEndingSeparator( aParentDir.getAbsolutePath() );
    String p2 = removeEndingSeparator( aChildFile.getAbsolutePath() );
    if( p1.length() > p2.length() ) {
      return null;
    }
    if( !p2.startsWith( p1 ) ) {
      return null;
    }
    return removeStartingSeparator( p2.substring( p1.length() ) );
  }

  /**
   * Удаляет расширение из имени файла.
   * <p>
   * Возвращает имя (и возможно путь) файла без расширения. Корректно обрабатываются пути с точками - т.е. точка
   * расшиерния ищется только в имени файла, а не в пути.
   * <p>
   * Данный метод работает только со строкой и не проводит никаких обращений к файловой системе!
   *
   * @param aFileName String имя файла, может содержать путь к файлу
   * @return String имя/путь файла без расширения
   */
  public static String removeExtension( String aFileName ) {
    String fn = aFileName;
    int pathIdx = fn.lastIndexOf( File.separatorChar );
    int extIdx = fn.lastIndexOf( CHAR_EXT_SEPARATOR );
    if( extIdx >= 0 && extIdx > pathIdx ) {
      fn = fn.substring( 0, extIdx );
    }
    return fn;
  }

  private static File makeVideoIllustrationImage( String aVideoFileRelPath ) {
    String bareFileNameWithRelPath = removeExtension( aVideoFileRelPath );
    return new File( RESOURCES_SUBDIR_VIDEO_ILLS, bareFileNameWithRelPath + GIF_DOT_EXT );
  }

  private File ensureVideoFileGif( File aPsxVideoFile ) {
    String relPath = findPsxRelativePath( aPsxVideoFile );
    if( relPath == null ) {
      return null;
    }
    File gifFile = makeVideoIllustrationImage( relPath );
    if( !gifFile.exists() || gifFile.lastModified() < aPsxVideoFile.lastModified() ) {
      gifFile.getParentFile().mkdirs();
      PsxFileSystemUtils.createVideoIllGif( aPsxVideoFile, gifFile, winContext.get( Shell.class ) );
    }
    if( !gifFile.exists() || gifFile.length() == 0 ) {
      return null;
    }
    return gifFile;
  }

  // ------------------------------------------------------------------------------------
  // IPsxFileSystem
  //

  @Override
  public IStringList listProbableEpisodes() {
    IList<File> subdirs = TsFileUtils.listChilds( episodeNonsecFramesRoot, TsFileFilter.FF_DIRS );
    IStringListBasicEdit epIds = new SortedStringLinkedBundleList();
    for( File f : subdirs ) {
      if( EpisodeUtils.isValidEpisodeDateStr( f.getName() ) ) {
        LocalDate d = LocalDate.parse( f.getName() );
        String id = EpisodeUtils.localDate2EpisodeId( d );
        epIds.add( id );
      }
    }
    return epIds;
  }

  @Override
  public File findFrameFile( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    if( aFrame.frameNo() < 0 || !aFrame.isDefined() ) {
      return null;
    }
    File subDir = findFrameDir( aFrame );
    if( !subDir.exists() ) {
      return null;
    }
    File epCameraDir = new File( subDir, aFrame.cameraId() );
    if( !epCameraDir.exists() ) {
      return null;
    }
    String baseFileName = PsxFileSystemUtils.bareSourceFrameFileName( aFrame.frameNo() );
    for( int i = 0; i < IMAGE_FILE_EXTENSIONS.length; i++ ) {
      String ext = IMAGE_FILE_EXTENSIONS[i];
      if( (IS_ANIM_IMAGE_EXT[i]) == aFrame.isAnimated() ) {
        File f = new File( epCameraDir, baseFileName + CHAR_EXT_SEPARATOR + ext );
        if( f.exists() ) {
          return f;
        }
      }
    }
    return null;
  }

  @Override
  public File findIconFile( String aPath ) {
    throw new TsUnderDevelopmentRtException();
  }

  @Override
  public File findSourceVideoFile( String aSourceVideoId ) {
    TsNullArgumentRtException.checkNull( aSourceVideoId );
    String episodeId = SourceVideoUtils.extractEpisodeId( aSourceVideoId );
    File subDir = findSourceEpisodeSubdir( episodeId, EPSUBDIR_SRCVIDEOS );
    if( subDir != null ) {
      String cameraId = SourceVideoUtils.extractCamId( aSourceVideoId );
      for( String ext : VIDEO_FILE_EXTENSIONS ) {
        File srcVideoFile = new File( subDir, cameraId + CHAR_EXT_SEPARATOR + ext );
        if( srcVideoFile.exists() ) {
          return srcVideoFile;
        }
      }
    }
    return null;
  }

  @Override
  public File findTrailerFile( String aTrailerId ) {
    TsNullArgumentRtException.checkNull( aTrailerId );
    String episodeId = TrailerUtils.extractEpisodeId( aTrailerId );
    File subDir = findSourceEpisodeSubdir( episodeId, EPSUBDIR_TRAILERS );
    if( subDir != null ) {
      String localId = TrailerUtils.extractLocalId( aTrailerId );
      for( String ext : VIDEO_FILE_EXTENSIONS ) {
        File trailerFile = new File( subDir, localId + CHAR_EXT_SEPARATOR + ext );
        if( trailerFile.exists() ) {
          return trailerFile;
        }
      }
    }
    return null;
  }

  // @Override
  // public File findFilmFile( String aFilmId ) {
  // throw new TsUnderDevelopmentRtException();
  // }
  //
  // @Override
  // public File findFilmKdenliveProject( String aFilmId ) {
  // throw new TsUnderDevelopmentRtException();
  // }

  @Override
  public IList<IFrame> listEpisodeFrames( FrameSelectionCriteria aCriteria ) {
    TsNullArgumentRtException.checkNull( aCriteria );
    IList<IFrame> rawListOfFrames;
    if( aCriteria.svin().hasCam() ) {
      rawListOfFrames = framesCache.listCamFrames( aCriteria.svin().episodeId(), aCriteria.svin().cameraId() );
    }
    else {
      rawListOfFrames = framesCache.listEpisodeFrames( aCriteria.svin().episodeId() );
    }
    IListBasicEdit<IFrame> frames = new SortedElemLinkedBundleList<>();
    for( IFrame f : rawListOfFrames ) {
      if( aCriteria.isAccepted( f ) ) {
        frames.add( f );
      }
    }
    return frames;
  }

  @Override
  public IList<IFrame> listSvinIllustrationFrames( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    IList<IFrame> frames = listEpisodeFrames( new FrameSelectionCriteria( aSvin, EAnimationKind.ANIMATED, true ) );
    if( !frames.isEmpty() ) {
      return frames;
    }
    int frameSec = aSvin.interval().start() + aSvin.interval().duration() / 2;
    // нет анимированных, ищем не анимированный кадр за frameSec секундув дирктории секундных кадров
    File camsRootDir = findSourceEpisodeSubdir( aSvin.episodeId(), EPSUBDIR_FRAMES_SECS );
    if( camsRootDir != null ) {
      IList<File> dd = listCamIdSubdirs( camsRootDir );
      for( File d : dd ) {
        Frame middleFrame = new Frame( aSvin.episodeId(), d.getName(), frameSec, false );
        File f = findFrameFile( middleFrame );
        if( f != null && f.exists() ) {
          return new SingleItemList<>( middleFrame );
        }
      }
    }
    return IList.EMPTY;
  }

  @Override
  public IList<File> listEpisodeKdenliveProjects( String aEpisodeId ) {
    File develDir = findSourceEpisodeSubdir( aEpisodeId, EPSUBDIR_DEVEL );
    if( develDir == null ) {
      return IList.EMPTY;
    }
    TsFileFilter ff = TsFileFilter.ofFileExt( KDENLIVE_EXT );
    IList<File> allProjFiles = TsFileUtils.listChilds( develDir, ff );
    if( allProjFiles.isEmpty() ) {
      return IList.EMPTY;
    }
    IListEdit<File> nonBackupProjFiles = new ElemArrayList<>();
    for( File f : allProjFiles ) {
      if( !f.getName().contains( BACKUP_STRING_IN_KDENLIVE_FILE_NAME ) ) {
        nonBackupProjFiles.add( f );
      }
    }
    return nonBackupProjFiles;
  }

  @Override
  public TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFrame, aThumbSize );
    File frameFile = findFrameFile( aFrame );
    if( frameFile == null ) {
      return null;
    }
    return imageManager.findThumb( frameFile, aThumbSize );
  }

  @Override
  public IFrameLoaderThread startThumbsLoading( IList<IFrame> aFrames, EThumbSize aThumbSize,
      IFrameLoadedCallback aCallback, boolean aImmediatelyLoadExistingThumbs ) {
    TsNullArgumentRtException.checkNulls( aFrames, aThumbSize, aCallback );
    FrameLoaderThread flh = new FrameLoaderThread( aFrames, aThumbSize, aCallback, aImmediatelyLoadExistingThumbs );
    Thread t = new Thread( flh, LOADER_THREAD_NAME_PREFIX + ++threadNameCounter );
    t.start();
    return flh;
  }

  static boolean          status       = false;
  static final EThumbSize DEF_GIF_SIZE = EThumbSize.SZ512;

  @SuppressWarnings( "nls" )
  ProcessBuilder prepareProcessBuilderForGifCreation( String aEpisodeId, String aCamId, int aStartSec,
      EThumbSize aGifSize ) {
    EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    StridUtils.checkValidIdPath( aCamId );
    HmsUtils.checkSec( aStartSec );
    // найдем директорию
    File epDir = findNonSecFramesEpisodeDir( aEpisodeId );
    if( epDir == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_EPISODE_DIR_FOR_GIF, aEpisodeId );
    }
    File epCameraDir = new File( epDir, aCamId );
    if( !epCameraDir.exists() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_CAM_DIR_FOR_GIF, aEpisodeId, aCamId );
    }
    // создадим командную строку запуска создателя GIF-анимации
    IStringListEdit cmdLineItems = new StringLinkedBundleList();
    // создаем "квадратные" значки
    Integer sz = Integer.valueOf( aGifSize.size() );
    String sizeStr = String.format( "%dx%d", sz, sz );
    String[] CLITEMS = { "gm", "convert",
        // используем GraphicsMagick
        "-delay", "8", //
        "-resize", sizeStr, //
        "-background", "none", //
        "-compose", "Copy", //
        "-gravity", "center", //
        "-extent", sizeStr //
    };
    for( String s : CLITEMS ) {
      cmdLineItems.add( s );
    }
    for( int frameNo = aStartSec * FPS; frameNo < (aStartSec + ANIMATED_GIF_SECS) * FPS; frameNo += 2 ) {
      String fileName = PsxFileSystemUtils.bareSourceFrameFileName( frameNo ) + ".jpg";
      File frameFile = new File( epCameraDir, fileName );
      if( !frameFile.exists() ) {
        return null;
      }
      cmdLineItems.add( fileName );
    }
    String outFileName = PsxFileSystemUtils.bareSourceFrameFileName( aStartSec * FPS ) + ANIMATED_FILE_DOT_EXTENSION;
    File outDir = findSourceEpisodeSubdir( aEpisodeId, EPSUBDIR_FRAMES_ANIM );
    outDir = new File( outDir, aCamId );
    File outFile = new File( outDir, outFileName );
    cmdLineItems.add( outFile.getAbsolutePath() );
    // выполним преобразование с диалогом
    ProcessBuilder pb = new ProcessBuilder( cmdLineItems.toArray() );
    pb.directory( epCameraDir );
    pb.inheritIO();
    return pb;
  }

  @Override
  public boolean createGifAnimation( String aEpisodeId, String aCamId, int aStartSec ) {
    ProcessBuilder pb = prepareProcessBuilderForGifCreation( aEpisodeId, aCamId, aStartSec, DEF_GIF_SIZE );
    // выполним преобразование с диалогом
    IRunnableWithProgress op = aMonitor -> {
      // запускаем GraphicsMagik в директории с кадрами исходно видео
      try {
        // ProcessBuilder pb = new ProcessBuilder( cmdLineItems.toArray() );
        // pb.directory( epCameraDir );
        // pb.inheritIO();
        Process p = pb.start();
        p.waitFor( 60, TimeUnit.SECONDS );
        // обновим кеш
        framesCache.updateFrameFile( new Frame( aEpisodeId, aCamId, aStartSec * FPS, true ) );
        status = true;
      }
      catch( Exception e ) {
        e.printStackTrace();
        TsDialogUtils.error( winContext.get( Shell.class ), e );
        status = false;
      }
    };
    ProgressMonitorDialog dlg = new ProgressMonitorDialog( winContext.get( Shell.class ) );
    try {
      dlg.run( false, false, op );
    }
    catch( Exception ex ) {
      ex.printStackTrace();
      // TsDialogUtils.error( winContext.get( Shell.class ), ex );
    }
    return status;
  }

  @Override
  public void recreateGifAnimations( IList<IFrame> aFrames ) {
    throw new TsUnderDevelopmentRtException();
  }

  @Override
  public File enusurePsxVideoIllustrationImage( File aPsxVideoFile ) {
    TsNullArgumentRtException.checkNull( aPsxVideoFile );
    String ext = TsFileUtils.extractExtension( aPsxVideoFile.getName() ).toLowerCase();
    if( !IMediaFileConstants.VIDEO_FILE_EXT_LIST.hasElem( ext ) ) {
      return null;
    }
    return ensureVideoFileGif( aPsxVideoFile );
  }

  @Override
  public IPfsOriginalMedia originalMedia() {
    return originalMedia;
  }

}
