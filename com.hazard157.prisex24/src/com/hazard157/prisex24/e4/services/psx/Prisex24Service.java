package com.hazard157.prisex24.e4.services.psx;

import static com.hazard157.prisex24.e4.services.psx.IPsxResources.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * {@link IPrisex24Service} implementation.
 *
 * @author hazard157
 */
public class Prisex24Service
    implements IPrisex24Service, IPsxGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Prisex24Service( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void errDialog( String aMsg, Object... aArgs ) {
    TsDialogUtils.error( getShell(), aMsg, aArgs );
  }

  // ------------------------------------------------------------------------------------
  // IPrisex24Service
  //

  @Override
  public TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFrame, aThumbSize );
    File frameFile = cofsFrames().findFrameFile( aFrame );
    if( frameFile == null ) {
      return null;
    }
    return imageManager().findThumb( frameFile, aThumbSize );
  }

  @Override
  public void playEpisodeVideo( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    // check episode exists
    IEpisode e = unitEpisodes().items().findByKey( aSvin.episodeId() );
    if( e == null ) {
      errDialog( FMT_ERR_NO_SUCH_EPISODE, aSvin.episodeId() );
      return;
    }
    IStringMap<ISourceVideo> svs = unitSourceVideos().episodeSourceVideos( aSvin.episodeId() );
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
    File f = psxCofs().findSourceVideoFile( srcVideo.id() );
    if( f == null ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEO_FILE, aSvin.episodeId(), camId );
      return;
    }
    if( !TsFileUtils.isFileReadable( f ) ) {
      errDialog( FMT_ERR_SOURCE_VIDEO_FILE_INACCESSABLE, f.getAbsolutePath() );
      return;
    }
    if( !aSvin.isWholeEpisode() ) {
      mediaPlayer().playVideoFilePart( f, aSvin.interval().start(), aSvin.interval().duration() + 1 );
    }
    else {
      mediaPlayer().playVideoFile( f );
    }
  }

}
