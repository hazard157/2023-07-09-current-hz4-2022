package com.hazard157.psx24.core.e4.services.prisex;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.e4.services.prisex.IPsxResources.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.movies.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.utils.gifgen.*;
import com.hazard157.psx24.core.utils.gifgen.PsxGifGenParams.*;

/**
 * Реализация {@link IPrisexService}.
 *
 * @author hazard157
 */
public class PrisexService
    implements IPrisexService {

  /**
   * Имя программы для воспроизведения видео.
   * <p>
   * Предполагается использование MPlayer или совместимого по аргументам командной строки проигривателя.
   */
  static final String VIDEO_PLAYER_NAME = "gmplayer"; //$NON-NLS-1$

  /**
   * Имя программы для воспроизведения аудио.
   */
  static final String AUDIO_PLAYER_NAME = "gmplayer"; //$NON-NLS-1$

  private final PsxGifGenFromSink gifGen;

  final IEclipseContext winContext;
  final ITsImageManager imageManager;
  final IPsxFileSystem  fileSystem;

  /**
   * Создает службу.
   * <p>
   * Службу следует создать с контекстом, в котором уже есть главное окно.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PrisexService( IEclipseContext aWinContext ) {
    winContext = TsNullArgumentRtException.checkNull( aWinContext );
    imageManager = winContext.get( ITsImageManager.class );
    fileSystem = winContext.get( IPsxFileSystem.class );
    gifGen = new PsxGifGenFromSink( aWinContext );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void errDialog( String aMsg, Object... aArgs ) {
    TsDialogUtils.error( winContext.get( Shell.class ), aMsg, aArgs );
  }

  private IMediaPlayerService mps() {
    IMediaPlayerService mps = winContext.get( IMediaPlayerService.class );
    TsInternalErrorRtException.checkNull( mps );
    return mps;
  }

  private static IList<Svin> createTrailerSink( Trailer aTrailer ) {
    IListEdit<Svin> ll = new ElemArrayList<>( 100 );
    for( Chunk c : aTrailer.chunks() ) {
      Secint in;
      if( c == aTrailer.chunks().last() ) {
        in = new Secint( c.interval().start(), c.interval().end() - 1 );
      }
      else {
        in = c.interval();
      }
      Svin sp = new Svin( c.episodeId(), c.cameraId(), in );
      ll.add( sp );
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // IPrisexService
  //

  @Override
  public void playEpisodeVideo( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    // проверим существование эпизода
    IEpisode e = winContext.get( IUnitEpisodes.class ).items().findByKey( aSvin.episodeId() );
    if( e == null ) {
      errDialog( FMT_ERR_NO_SUCH_EPISODE, aSvin.episodeId() );
      return;
    }
    IStringMap<ISourceVideo> svs = winContext.get( IUnitSourceVideos.class ).episodeSourceVideos( aSvin.episodeId() );
    if( svs.isEmpty() ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEOS, aSvin.episodeId() );
      return;
    }
    String camId = aSvin.cameraId();
    if( !aSvin.hasCam() ) {
      camId = TsLibUtils.EMPTY_STRING;
      IFrame f = e.frame();
      if( f.isDefined() ) {
        camId = f.cameraId();
      }
      else {
        camId = svs.keys().get( 0 );
      }
    }
    ISourceVideo srcVideo = svs.findByKey( camId );
    if( srcVideo == null ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEO, aSvin.episodeId(), camId );
      return;
    }
    File f = winContext.get( IPsxFileSystem.class ).findSourceVideoFile( srcVideo.id() );
    if( f == null ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEO_FILE, aSvin.episodeId(), camId );
      return;
    }
    if( !TsFileUtils.isFileReadable( f ) ) {
      errDialog( FMT_ERR_SOURCE_VIDEO_FILE_INACCESSABLE, f.getAbsolutePath() );
      return;
    }
    if( !aSvin.isWholeEpisode() ) {
      mps().playVideoFilePart( f, aSvin.interval().start(), aSvin.interval().duration() + 1, false );
    }
    else {
      mps().playVideoFile( f, false );
    }
  }

  @Override
  public void playTrailer( String aTrailerId ) {
    TsNullArgumentRtException.checkNull( aTrailerId );
    File f = fileSystem.findTrailerFile( aTrailerId );
    if( f == null ) {
      errDialog( FMT_ERR_NO_TRAILER_FILE, aTrailerId );
      return;
    }
    if( !TsFileUtils.isFileReadable( f ) ) {
      errDialog( FMT_ERR_TRAILER_FILE_INACCESSABLE, f.getAbsolutePath() );
      return;
    }
    mps().playVideoFile( f, false );
  }

  // @Override
  // public void playFilm( String aFilmId ) {
  // TsNullArgumentRtException.checkNull( aFilmId );
  // File f = fileSystem.findFilmFile( aFilmId );
  // if( f == null ) {
  // errDialog( FMT_ERR_NO_FILM_FILE, aFilmId );
  // return;
  // }
  // if( !TsFileUtils.isFileReadable( f ) ) {
  // errDialog( FMT_ERR_FILM_FILE_INACCESSABLE, f.getAbsolutePath() );
  // return;
  // }
  // mps().playVideoFile( f );
  // }

  @Override
  public void playAudioFile( String aFilePath ) {
    TsNullArgumentRtException.checkNull( aFilePath );
    File f = new File( aFilePath );
    mps().playAudioFile( f );
  }

  @Override
  public void copyFrameImage( IFrame aFrame, File aDestDir ) {
    TsNullArgumentRtException.checkNull( aFrame );
    TsFileUtils.checkDirReadable( aDestDir );
    File srcFile = fileSystem.findFrameFile( aFrame );
    if( srcFile == null ) {
      return;
    }
    String newName = aFrame.episodeId() + '-' + // идентификатор эпизода
        TsFileUtils.extractBareFileName( srcFile.getName() ) + '-' + // номер кадра ЧЧ-ММ-СС_КК
        aFrame.cameraId() + // идентификатор камеры
        TsFileUtils.CHAR_EXT_SEPARATOR + TsFileUtils.extractExtension( srcFile.getName() ); // расширение исходного
                                                                                            // файла
    File destFile = new File( aDestDir, newName );
    TsFileUtils.copyFile( srcFile, destFile );
  }

  @Override
  public TsImage loadTrailerIllimThumb( String aTrailerId, EThumbSize aThumbSize, boolean aRecreate ) {
    TsNullArgumentRtException.checkNulls( aTrailerId, aThumbSize );
    // находим трейлер и его файл
    IUnitTrailers unitTrailers = winContext.get( IUnitTrailers.class );
    Trailer trailer = unitTrailers.tsm().items().getByKey( aTrailerId );
    File trFile = fileSystem.findTrailerFile( aTrailerId );
    if( trFile == null ) {
      return null;
    }
    // gif-файл hfpvtoftncz рядом с файлом трейлера с тем же именем и расширением ".gif"
    File illimDir = trFile.getParentFile();
    String illimName = TsFileUtils.extractBareFileName( trFile.getName() ).concat( ANIMATED_FILE_DOT_EXTENSION );
    File illimFile = new File( illimDir, illimName );
    // если нужно, пересоздадим gif
    if( !illimFile.exists() || aRecreate ) {
      // сначала зададим параметры...
      gifGen.params().setBypassedFramesOnAnim( 3 );
      // gifGen.params().setDeltaSec( 5 );
      gifGen.params().setFinalStillDelay( 2000 );
      gifGen.params().setMode( EMode.SVINS_RECKONED );
      // gifGen.params().setFramesNum( 10 );
      gifGen.params().setSvinReckonSecs( 3 );
      // ... а потом создаем gif
      IList<Svin> sink = createTrailerSink( trailer );
      gifGen.createThumb( illimFile, sink );
    }
    if( !illimFile.exists() ) {
      return null;
    }
    return imageManager.findThumb( illimFile, aThumbSize );
  }

}
