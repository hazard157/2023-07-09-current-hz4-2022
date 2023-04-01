package com.hazard157.psx24.core.e4.uiparts.refbooks;

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
import org.toxsoft.core.tsgui.utils.layout.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.glib.frlstviewer_any.*;
import com.hazard157.psx24.core.m5.srcvids.*;

/**
 * Вью правки камер.
 *
 * @author hazard157
 */
public class UipartRefbookSourcevids
    extends MwsAbstractPart {

  @Inject
  IUnitSourceVideos unitSourceVideos;

  IM5CollectionPanel<ISourceVideo> svPanel;
  PanelFramesListViewer            flPanel;

  @Override
  protected void doInit( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // source videos list
    IM5Model<ISourceVideo> model = m5().getModel( SourceVideoM5Model.MODEL_ID, ISourceVideo.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<ISourceVideo> lm = model.getLifecycleManager( unitSourceVideos );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUMMARY_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), avValobj( EThumbSize.SZ128 ) );
    svPanel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    svPanel.createControl( sfMain );
    // frames list
    ctx = new TsGuiContext( tsContext() );
    flPanel = new PanelFramesListViewer( sfMain, ctx );
    // setup
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
    sfMain.setWeights( 350, 650 );
  }

}
