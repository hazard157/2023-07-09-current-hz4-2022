package com.hazard157.psx24.core.e4.uiparts.refbooks;

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
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.utils.layout.*;

import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.glib.frlstviewer_any.*;
import com.hazard157.psx24.core.m5.trailer.*;

/**
 * Вью правки камер.
 *
 * @author hazard157
 */
public class UipartRefbookTrailers
    extends MwsAbstractPart {

  @Inject
  IUnitTrailers unitTrailers;

  IM5CollectionPanel<Trailer> traPanel;
  PanelFramesListViewer       flPanel;

  @Override
  protected void doInit( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // source videos list
    IM5Model<Trailer> model = m5().getModel( TrailerM5Model.MODEL_ID, Trailer.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<Trailer> lm = model.getLifecycleManager( unitTrailers );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUMMARY_PANE.setValue( ctx.params(), AV_TRUE );
    traPanel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    traPanel.createControl( sfMain );
    // frames list
    ctx = new TsGuiContext( tsContext() );
    flPanel = new PanelFramesListViewer( sfMain, ctx );
    // setup
    traPanel.addTsSelectionListener( ( aSource, aSel ) -> {
      if( aSel == null ) {
        flPanel.setFramesList( null );
        return;
      }
      flPanel.setFramesList( aSel.listFramesOfChunks() );
    } );
    sfMain.setWeights( 350, 650 );
  }

}
