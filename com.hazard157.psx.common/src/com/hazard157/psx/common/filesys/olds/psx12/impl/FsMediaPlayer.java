package com.hazard157.psx.common.filesys.olds.psx12.impl;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.filesys.olds.psx12.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * {@link IFsMediaPlayer} implementation.
 *
 * @author hazard157
 */
class FsMediaPlayer
    implements IFsMediaPlayer {

  // private final PsxFileSystem fileSystem;
  // private final IEclipseContext winContext;
  // private final IUnitEpisodes unitEpisodes;
  // private final IUnitSourceVideos unitSourceVideos;

  FsMediaPlayer( @SuppressWarnings( "unused" ) PsxFileSystem aFileSystem ) {
    // fileSystem = aFileSystem;
    // winContext = fileSystem.winContext();
    // unitEpisodes = winContext.get( IUnitEpisodes.class );
    // unitSourceVideos = winContext.get( IUnitSourceVideos.class );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  // private void errDialog( String aMsg, Object... aArgs ) {
  // TsDialogUtils.error( winContext.get( Shell.class ), aMsg, aArgs );
  // }

  // private IMediaPlayerService mps() {
  // IMediaPlayerService mps = winContext.get( IMediaPlayerService.class );
  // TsInternalErrorRtException.checkNull( mps );
  // return mps;
  // }

  // ------------------------------------------------------------------------------------
  // IPsxMediaPlayer
  //

  @Override
  public void playEpisodeVideo( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );

    // FIXME FsMediaPlayer.playEpisodeVideo()

    // // проверим существование эпизода
    // IEpisode e = unitEpisodes.items().findByKey( aSvin.episodeId() );
    // if( e == null ) {
    // errDialog( FMT_ERR_NO_SUCH_EPISODE, aSvin.episodeId() );
    // return;
    // }
    // IStringMap<ISourceVideo> svs = unitSourceVideos.episodeSourceVideos( aSvin.episodeId() );
    // if( svs.isEmpty() ) {
    // errDialog( FMT_ERR_NO_SOURCE_VIDEOS, aSvin.episodeId() );
    // return;
    // }
    // String camId = aSvin.cameraId();
    // if( !aSvin.hasCam() ) {
    // camId = TsLibUtils.EMPTY_STRING;
    // IFrame f = e.frame();
    // if( f.isDefined() ) {
    // camId = f.cameraId();
    // }
    // else {
    // camId = svs.keys().get( 0 );
    // }
    // }
    // ISourceVideo srcVideo = svs.findByKey( camId );
    // if( srcVideo == null ) {
    // errDialog( FMT_ERR_NO_SOURCE_VIDEO, aSvin.episodeId(), camId );
    // return;
    // }
    // File f = fileSystem.sourceVideoFilesManager().findSourceVideoFile( srcVideo.id() );
    // if( f == null ) {
    // errDialog( FMT_ERR_NO_SOURCE_VIDEO_FILE, aSvin.episodeId(), camId );
    // return;
    // }
    // if( !TsFileUtils.isFileReadable( f ) ) {
    // errDialog( FMT_ERR_SOURCE_VIDEO_FILE_INACCESSABLE, f.getAbsolutePath() );
    // return;
    // }
    // if( !aSvin.isWholeEpisode() ) {
    // mps().playVideoFilePart( f, aSvin.interval().start(), aSvin.interval().duration() + 1 );
    // }
    // else {
    // mps().playVideoFile( f );
    // }
  }

}
