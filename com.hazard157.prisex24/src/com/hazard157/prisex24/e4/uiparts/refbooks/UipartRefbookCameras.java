package com.hazard157.prisex24.e4.uiparts.refbooks;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secint.*;
import com.hazard157.common.quants.secstep.*;
import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.prisex24.m5.srcvideo.*;
import com.hazard157.prisex24.utils.frasel.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Вью правки камер.
 *
 * @author hazard157
 */
public class UipartRefbookCameras
    extends PsxAbstractUipart {

  @Inject
  IUnitCameras unitCameras;

  @Inject
  IUnitSourceVideos unitSourceVideos;

  IM5CollectionPanel<Camera>       camPanel;
  IM5CollectionPanel<ISourceVideo> svPanel;

  @Override
  protected void doInit( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // cameras
    IM5Model<Camera> camModel = m5().getModel( MID_CAMERA, Camera.class );
    IM5LifecycleManager<Camera> camLm = camModel.getLifecycleManager( unitCameras );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_LISTS ) );
    camPanel = camModel.panelCreator().createCollEditPanel( ctx, camLm.itemsProvider(), camLm );
    camPanel.createControl( sfMain );
    // source videos
    IM5Model<ISourceVideo> svModel = m5().getModel( MID_SOURCE_VIDEO, ISourceVideo.class );
    ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<ISourceVideo> lm0 = new CameraSourceVideoLifecycleManager( svModel, null, unitSourceVideos );
    OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_LISTS ) );
    svPanel = svModel.panelCreator().createCollEditPanel( ctx, lm0.itemsProvider(), lm0 );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_FALSE );
    svPanel.createControl( sfMain );
    // svPanel.addTsSelectionListener( soureceVideoSelectionChangeListener );
    // frames list
    ctx = new TsGuiContext( tsContext() );
    ISvinsFramesViewer flPanel = new SvinsFramesViewer( sfMain, ctx );
    flPanel.svinFramesParams().setParams( EAnimationKind.ANIMATED, ESecondsStep.SEC_01, EFramesPerSvin.AT_LEAST_ONE );

    // setup
    camPanel.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      String camId = aSelectedItem != null ? aSelectedItem.id() : null;
      IM5LifecycleManager<ISourceVideo> lm1 = new CameraSourceVideoLifecycleManager( svModel, camId, unitSourceVideos );
      svPanel.setItemsProvider( lm1.itemsProvider() );
      svPanel.setLifecycleManager( lm1 );
      svPanel.setSelectedItem( null );
      svPanel.refresh();
    } );
    svPanel.addTsSelectionListener( ( aSource, aSel ) -> {
      if( aSel == null ) {
        flPanel.svinSeq().svins().clear();
        return;
      }
      Secint in = new Secint( 0, aSel.duration() - 1 );
      Svin svin = new Svin( aSel.episodeId(), aSel.cameraId(), in, aSel.frame() );
      flPanel.svinSeq().svins().setAll( svin );
    } );
    sfMain.setWeights( 250, 250, 500 );
  }

}
