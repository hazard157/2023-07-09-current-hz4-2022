package com.hazard157.prisex24.e4.uiparts.mingles;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
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
import org.toxsoft.core.tsgui.mws.services.currentity.*;

import com.hazard157.prisex24.e4.services.gazes.*;
import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.psx.proj3.mingle.*;

/**
 * Lists and manages all the mingles from {@link IUnitMingles#items()}.
 *
 * @author hazard157
 */
public class UipartMinglesList
    extends PsxAbstractUipart {

  private final ITsSelectionChangeListener<IMingle> panelSelectionChangeListener = ( aSource, aSelectedItem ) -> {
    this.isSelfCurrMingleChange = true;
    try {
      this.currentMingleService.setCurrent( aSelectedItem );
    }
    finally {
      this.isSelfCurrMingleChange = false;
    }
  };

  private final ICurrentEntityChangeListener<IMingle> serviceSelectionChangeListener =
      aCurrent -> this.panel.setSelectedItem( aCurrent );

  @Inject
  ICurrentMingleService currentMingleService;

  IM5CollectionPanel<IMingle> panel;
  boolean                     isSelfCurrMingleChange = false;

  @Override
  protected void doInit( Composite aParent ) {
    currentMingleService.addCurrentEntityChangeListener( serviceSelectionChangeListener );
    IM5Model<IMingle> model = m5().getModel( MID_MINGLE, IMingle.class );
    IUnitMingles unitMingles = tsContext().get( IUnitMingles.class );
    IM5LifecycleManager<IMingle> lm = model.getLifecycleManager( unitMingles );
    IM5ItemsProvider<IMingle> ip = lm.itemsProvider();
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    panel = model.panelCreator().createCollEditPanel( ctx, ip, lm );
    panel.createControl( aParent );
    panel.addTsSelectionListener( panelSelectionChangeListener );
  }

}
