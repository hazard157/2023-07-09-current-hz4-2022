package com.hazard157.psx24.films.e4.services;

import static com.hazard157.psx24.films.IPsx24FilmsConstants.*;
import static com.hazard157.psx24.films.e4.services.IPsxResources.*;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.e4.core.contexts.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.incub.*;
import com.hazard157.psx24.films.glib.*;

/**
 * Реализация {@link IPsxFilmsService}.
 *
 * @author hazard157
 */
public class PsxFilmsService
    implements IPsxFilmsService {

  // TODO отслеживать изменения в директории фильмов и использовать fsChangeEventProducer

  // отслеживает изменение настроек приложения
  private final ITsCollectionChangeListener prefsChangeListener = ( aSource, aOp, aItem ) -> initFropAppPrefs();

  private static final String STR_DIR_MAIN_FILM_THUMBS = "/home/hmade/cache/film-thumbs/"; //$NON-NLS-1$
  private static final String STR_DIR_SCRIPTS          = "/home/hmade/bin/scripts";        //$NON-NLS-1$

  private static final String SUBDIR_LEGACY   = "legacy"; //$NON-NLS-1$
  private static final String SUBDIR_KDENLIVE = "devel";  //$NON-NLS-1$

  private static final String GIF_CREATE_SH    = "make-video-thumb.sh"; //$NON-NLS-1$
  private static final String GIF_DOT_EXT      = ".gif";                //$NON-NLS-1$
  private static final String KDENLIVE_DOT_EXT = ".kdenlive";           //$NON-NLS-1$
  private static final String CONVOY_FILE_EXT  = "convoy";              //$NON-NLS-1$

  private final GenericChangeEventer fsChangeEventProducer;

  private final ConvoyFileManager convoyFileManager;

  private final IEclipseContext appContext;
  private final IPrefBundle     filmsPrefBundle;

  private File filmsDir;

  /**
   * Конструктор.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  public PsxFilmsService( IEclipseContext aWinContext ) {
    TsNullArgumentRtException.checkNull( aWinContext );
    fsChangeEventProducer = new GenericChangeEventer( this );
    appContext = aWinContext;
    convoyFileManager = new ConvoyFileManager( CONVOY_FILE_EXT, CONVOY_FILE_EXT );
    filmsPrefBundle = appContext.get( IAppPreferences.class ).getBundle( PREF_BUNDLE_ID );
    filmsPrefBundle.prefs().addCollectionChangeListener( prefsChangeListener );
    initFropAppPrefs();
  }

  void initFropAppPrefs() {
    filmsDir = new File( OP_FILMS_DIR_ROOT.getValue( filmsPrefBundle.prefs() ).asString() );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private static File filmGifFile( File aFilmFile ) {
    String fileName = TsFileUtils.extractFileName( aFilmFile.getName() );
    File thumbsDir = new File( STR_DIR_MAIN_FILM_THUMBS );
    return new File( thumbsDir, fileName + GIF_DOT_EXT );
  }

  private void createFilmGif( File aFilmFile, File aGifFile ) {
    File binDir = new File( STR_DIR_SCRIPTS );
    File shFile = new File( binDir, GIF_CREATE_SH );
    if( !shFile.exists() ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_NO_SH_FILE, shFile.getAbsolutePath() );
      return;
    }
    IRunnableWithProgress thumbCreator = aMonitor -> {
      String s = String.format( FMT_CREATING_FILM_THUMB, aFilmFile.getName() );
      aMonitor.beginTask( s, IProgressMonitor.UNKNOWN );
      TsMiscUtils.runTool( shFile.getAbsolutePath(), aFilmFile.getAbsolutePath(), aGifFile.getAbsolutePath() );
    };
    Shell shell = appContext.get( Shell.class );
    ProgressMonitorDialog d = new ProgressMonitorDialog( shell );
    try {
      d.run( true, false, thumbCreator );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( shell, ex );
    }
  }

  private File ensureFilmGif( File aFilmFile ) {
    File gifFile = filmGifFile( aFilmFile );
    // если нет, или устарел, пересохдает GIF-файл
    if( !gifFile.exists() || gifFile.lastModified() < aFilmFile.lastModified() ) {
      createFilmGif( aFilmFile, gifFile );
    }
    if( !gifFile.exists() ) {
      return null;
    }
    return gifFile;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPsxFilmsService
  //

  @Override
  public File filmsDir() {
    return filmsDir;
  }

  @Override
  public TsImage loadFilmThumb( File aFilmFile, EThumbSize aThumbSize ) {
    if( aFilmFile == null ) {
      return null;
    }
    File gifFile = ensureFilmGif( aFilmFile );
    if( gifFile == null ) {
      return null;
    }
    ITsImageManager imageManager = appContext.get( ITsImageManager.class );
    return imageManager.findThumb( gifFile, aThumbSize );
  }

  @Override
  public IList<File> listFilmFiles() {
    IListBasicEdit<File> result = new SortedElemLinkedBundleList<>();
    if( OP_SHOW_LEGACY_FILMS.getValue( filmsPrefBundle.prefs() ).asBool() ) {
      File legacyDir = new File( filmsDir, SUBDIR_LEGACY );
      result.addAll( TsFileUtils.listChilds( legacyDir, IMediaFileConstants.FF_VIDEOS ) );
    }
    result.addAll( TsFileUtils.listChilds( filmsDir, IMediaFileConstants.FF_VIDEOS ) );
    return result;
    // File[] filesArray = filmsDir.listFiles( IMediaFileConstants.VIDEO_FILES_FILTER );
    // if( filesArray == null || filesArray.length == 0 ) {
    // return IList.EMPTY;
    // }
    // Arrays.sort( filesArray );
    // return new ElemArrayList<>( filesArray );
  }

  @Override
  public IStringList listExistingKeywords() {
    try {
      IList<File> ffl = listFilmFiles();
      IStringListEdit result = new StringArrayList();
      for( File ff : ffl ) {
        IOptionSet props = convoyFileManager.readFileConvoy( ff, OptionSetKeeper.KEEPER, IOptionSet.NULL );
        IStringList kwl = IFilmPropsConstants.FP_KEYWORDS.getValue( props ).asValobj();
        for( String s : kwl ) {
          if( !result.hasElem( s ) ) {
            result.add( s );
          }
        }
      }
      return result;
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      return IStringList.EMPTY;
    }
  }

  @Override
  public IPrefBundle modulePrefs() {
    return filmsPrefBundle;
  }

  @Override
  public File kdenliveProjectFile( File aFilmFile ) {
    TsNullArgumentRtException.checkNull( aFilmFile );
    String bareName = TsFileUtils.extractBareFileName( aFilmFile.getName() );
    File kDir = new File( filmsDir, SUBDIR_KDENLIVE );
    return new File( kDir, bareName + KDENLIVE_DOT_EXT );
  }

  @Override
  public ConvoyFileManager cfm() {
    return convoyFileManager;
  }

  @Override
  public IGenericChangeEventer fsChangeEventProducer() {
    return fsChangeEventProducer;
  }

}
