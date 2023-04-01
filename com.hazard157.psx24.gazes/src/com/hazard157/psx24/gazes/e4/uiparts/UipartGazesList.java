package com.hazard157.psx24.gazes.e4.uiparts;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;

import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx24.gazes.e4.services.*;
import com.hazard157.psx24.gazes.m5.*;

/**
 * Lists and manages all the gazes from {@link IUnitGazes#items()}.
 *
 * @author hazard157
 */
public class UipartGazesList
    extends MwsAbstractPart {

  private final ITsSelectionChangeListener<IGaze> panelSelectionChangeListener = ( aSource, aSelectedItem ) -> {
    this.isSelfCurrGazeChange = true;
    try {
      this.currentGazeService.setCurrent( aSelectedItem );
    }
    finally {
      this.isSelfCurrGazeChange = false;
    }
  };

  private final ICurrentEntityChangeListener<IGaze> serviceSelectionChangeListener =
      aCurrent -> this.panel.setSelectedItem( aCurrent );

  @Inject
  ICurrentGazeService currentGazeService;

  IM5CollectionPanel<IGaze> panel;
  boolean                   isSelfCurrGazeChange = false;

  @Override
  protected void doInit( Composite aParent ) {
    currentGazeService.addCurrentEntityChangeListener( serviceSelectionChangeListener );
    IM5Model<IGaze> model = m5().getModel( IGazeM5Constant.MID_GAZE, IGaze.class );
    IUnitGazes unitGazes = tsContext().get( IUnitGazes.class );
    IM5LifecycleManager<IGaze> lm = model.getLifecycleManager( unitGazes );
    IM5ItemsProvider<IGaze> ip = lm.itemsProvider();
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    panel = model.panelCreator().createCollEditPanel( ctx, ip, lm );
    panel.createControl( aParent );
    panel.addTsSelectionListener( panelSelectionChangeListener );
  }

}
