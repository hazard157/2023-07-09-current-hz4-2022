package com.hazard157.prisex24.m5.frames;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Left list of frames.
 *
 * @author hazard157
 */
class CommonFramesM5TreeViewer
    extends M5TreeViewer<IFrame>
    implements IAnimationKindable {

  private final ITreeModeManager<IFrame> treeModeManager;

  private final IStringListEdit showCmaeraIds     = new StringArrayList(); // EMPTY -> show all
  private EAnimationKind        showAnimationKind = EAnimationKind.BOTH;

  public CommonFramesM5TreeViewer( ITsGuiContext aContext, IM5Model<IFrame> aObjModel ) {
    super( aContext, aObjModel, false );
    // treeModeManager
    treeModeManager = new FrameCollectionPanelTmm( tsContext() ) {

      @Override
      protected void onCurrentModeIdChanged() {
        TreeModeInfo<IFrame> info = null;
        String mid = currModeId();
        if( mid != null ) {
          info = treeModeInfoes().findByKey( mid );
        }
        if( info != null ) {
          setTreeMaker( info.treeMaker() );
        }
        else {
          setTreeMaker( null );
        }
        repackColumns();
      }
    };
    filterManager().setFilter( aObj -> {
      if( aObj == null || !aObj.isDefined() ) {
        return false;
      }
      if( showAnimationKind.accept( aObj.isAnimated() ) ) {
        return showCmaeraIds.isEmpty() || showCmaeraIds.hasElem( aObj.cameraId() );
      }
      return false;
    } );
  }

  void repackColumns() {
    IM5Column<IFrame> col;
    col = columnManager().columns().getByKey( FID_FRAME_NO );
    col.setWidth( 220 );
    // isAnimated
    col = columnManager().columns().getByKey( FID_IS_ANIMATED );
    col.setAlignment( EHorAlignment.CENTER );
    col.pack();
    // camera ID
    col = columnManager().columns().getByKey( FID_CAMERA_ID );
    col.pack();
  }

  @Override
  protected void doProcessAfterControlsCreated() {
    IM5Column<IFrame> col;
    // frameNo
    col = columnManager().add( FID_FRAME_NO );
    // isAnimated
    col = columnManager().add( FID_IS_ANIMATED );
    col.setAlignment( EHorAlignment.CENTER );
    // camera ID
    col = columnManager().add( FID_CAMERA_ID );
    repackColumns();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public ITreeModeManager<IFrame> tmm() {
    return treeModeManager;
  }

  public void setShownCameraIds( IStringList aCamIds ) {
    if( !showCmaeraIds.equals( aCamIds ) ) {
      showCmaeraIds.setAll( aCamIds );
      refresh();
    }
  }

  // ------------------------------------------------------------------------------------
  // IAnimationKindable
  //

  @Override
  public EAnimationKind getDefaultAnimationKind() {
    return EAnimationKind.BOTH;
  }

  @Override
  public EAnimationKind getShownAnimationKind() {
    return showAnimationKind;
  }

  @Override
  public void setShownAnimationKind( EAnimationKind aAnimationKind ) {
    TsNullArgumentRtException.checkNull( aAnimationKind );
    if( showAnimationKind != aAnimationKind ) {
      showAnimationKind = aAnimationKind;
      refresh();
    }
  }

}
