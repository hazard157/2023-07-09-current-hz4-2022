package com.hazard157.prisex24.m5.frames;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Left list of frames.
 *
 * @author hazard157
 */
class NonsecFramesM5TreeViewer
    extends M5TreeViewer<IFrame>
    implements IPsxGuiContextable {

  /**
   * Number of non-sec frames before and after left tree selection to dislay in right list.
   */
  private static final int NONSECS_DELTA_FRAMES = 5 * FPS;

  private final IListEdit<IFrame> items = new ElemArrayList<>();

  public NonsecFramesM5TreeViewer( ITsGuiContext aContext, IM5Model<IFrame> aObjModel ) {
    super( aContext, aObjModel, false );
  }

  // ------------------------------------------------------------------------------------
  // M5TreeViewer
  //

  @Override
  protected void doProcessAfterControlsCreated() {
    IM5Column<IFrame> col;
    // frameNo
    col = columnManager().add( FID_FRAME_NO );
    col.setAlignment( EHorAlignment.CENTER );
    col.pack();
    col = columnManager().add( FID_IS_ANIMATED );
    col.setAlignment( EHorAlignment.CENTER );
    col.pack();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void fillNonsecsList( IFrame aBaseFrame ) {
    items.clear();
    if( aBaseFrame == null ) {
      items().setAll( items );
      setSelectedItem( null );
      return;
    }
    int startFrameNo = aBaseFrame.frameNo() - NONSECS_DELTA_FRAMES;
    int endFrameNo = startFrameNo + 2 * NONSECS_DELTA_FRAMES;
    for( int n = startFrameNo; n <= endFrameNo; n++ ) {
      if( n >= 0 ) {
        // try animated frames
        IFrame f = new Frame( aBaseFrame.episodeId(), aBaseFrame.cameraId(), n, true );
        if( cofsFrames().findFrameFile( f ) != null ) {
          items.add( f );
        }
        // try still frames
        f = new Frame( aBaseFrame.episodeId(), aBaseFrame.cameraId(), n, false );
        if( cofsFrames().findFrameFile( f ) != null ) {
          items.add( f );
        }
      }
    }
    items().setAll( items );
    setSelectedItem( aBaseFrame );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  void setBaseFrame( IFrame aCommonFrame ) {
    fillNonsecsList( aCommonFrame );
  }

  void moveSelection( ETsCollMove aMove ) {
    IFrame toSel = aMove.findElemAt( selectedItem(), items, 5, false );
    setSelectedItem( toSel );
  }

}
