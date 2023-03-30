package com.hazard157.psx24.timeline.main.stripes;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Source video frames stripe.
 *
 * @author goga
 */
public class SourceVideoStripe
    extends AbstractStripe {

  private final int VER_MARGIN = 2;

  private final ISourceVideo sourceVideo;

  /**
   * Конструктор.
   *
   * @param aSourceVideo {@link ISourceVideo} - исходное видел
   * @param aParams {@link IOptionSet} - начальные значения {@link #params()}
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public SourceVideoStripe( ISourceVideo aSourceVideo, IOptionSet aParams ) {
    super( aSourceVideo.cameraId(), aParams );
    sourceVideo = aSourceVideo;
    setNameAndDescription( aSourceVideo.nmName(), aSourceVideo.description() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractStripe
  //

  @Override
  protected int doGetHeight() {
    return 2 * VER_MARGIN + owner().getFrameThumbSize().size();
  }

  @Override
  protected void doPaintFlow( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {
    IPsxFileSystem fs = tsContext().get( IPsxFileSystem.class );
    for( int sec = aStartSec; sec <= aEndSec; sec += owner().timelineStep().stepSecs() ) {
      int frameNo = FPS * sec;
      IFrame frame = new Frame( sourceVideo.episodeId(), sourceVideo.cameraId(), frameNo, false );
      TsImage mi = fs.findThumb( frame, owner().getFrameThumbSize() );
      if( mi != null ) {
        int x = (int)owner().xCoor( sec );
        int y = aArea.y1() + VER_MARGIN;
        aGc.drawImage( mi.image(), x, y );
      }
    }
  }

  @Override
  protected void doPaintTitle( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {
    String text = id();
    Point tsz = aGc.textExtent( text );
    int x = aArea.x1() + (aArea.width() - tsz.x) / 2;
    int y = aArea.y1() + (aArea.width() - tsz.y) / 2;
    aGc.drawText( id(), x, y );
  }

}
