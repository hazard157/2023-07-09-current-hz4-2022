package com.hazard157.psx24.core.e4.uiparts.refbooks;

import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.glib.frlstviewer_any.*;
import com.hazard157.psx24.core.m5.camera.*;
import com.hazard157.psx24.core.m5.srcvids.*;

/**
 * Вью правки камер.
 *
 * @author hazard157
 */
public class UipartRefbookCameras
    extends MwsAbstractPart {

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
    IM5Model<Camera> camModel = m5().getModel( CameraM5Model.MODEL_ID, Camera.class );
    IM5LifecycleManager<Camera> camLm = camModel.getLifecycleManager( unitCameras );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    camPanel = camModel.panelCreator().createCollEditPanel( ctx, camLm.itemsProvider(), camLm );
    camPanel.createControl( sfMain );
    // source videos
    IM5Model<ISourceVideo> svModel = m5().getModel( MID_SOURCE_VIDEO, ISourceVideo.class );
    ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<ISourceVideo> lm0 = new CameraSourceVideoLifecycleManager( svModel, null, unitSourceVideos );
    OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), avValobj( EThumbSize.SZ180 ) );
    svPanel = svModel.panelCreator().createCollEditPanel( ctx, lm0.itemsProvider(), lm0 );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_FALSE );
    svPanel.createControl( sfMain );
    // svPanel.addTsSelectionListener( soureceVideoSelectionChangeListener );
    // frames list
    ctx = new TsGuiContext( tsContext() );
    PanelFramesListViewer flPanel = new PanelFramesListViewer( sfMain, ctx );

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
        flPanel.setFramesList( null );
        return;
      }
      Secint in = new Secint( 0, aSel.duration() - 1 );
      Svin svin = new Svin( aSel.episodeId(), aSel.cameraId(), in, aSel.frame() );
      IPsxFileSystem fileSystem = tsContext().get( IPsxFileSystem.class );
      // IList<IFrame> frames =
      // fileSystem.listEpisodeFrames( new FrameSelectionCriteria( svin, EAnimationKind.ANIMATED, true ) );
      // if( frames.isEmpty() ) {
      // int frameSec = svin.interval().start() + svin.interval().duration() / 2;
      // IFsExplorer fe = appContext().get( IFsExplorer.class );
      // IStringMap<File> camDirs = fe.listSourceCameraDirs( svin.episodeId() );
      // if( !camDirs.isEmpty() ) {
      // Frame middleFrame = new Frame( svin.episodeId(), camDirs.values().first().getName(), frameSec, false );
      // frames = new SingleItemList<>( middleFrame );
      // }
      // }
      // flPanel.setFramesList( frames );
      flPanel.setFramesList( fileSystem.listSvinIllustrationFrames( svin ) );
    } );
    sfMain.setWeights( 250, 250, 500 );
  }

}
