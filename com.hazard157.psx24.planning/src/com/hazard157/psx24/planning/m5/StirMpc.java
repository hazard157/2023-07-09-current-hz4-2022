package com.hazard157.psx24.planning.m5;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link StringM5Model}.
 *
 * @author hazard157
 */
class StirMpc
    extends MultiPaneComponentModown<IStir> {

  public StirMpc( ITsGuiContext aContext, IM5Model<IStir> aModel, IM5ItemsProvider<IStir> aItemsProvider,
      IM5LifecycleManager<IStir> aLifecycleManager ) {
    super( prepareContext( aContext ), aModel, aItemsProvider, aLifecycleManager );
  }

  private static ITsGuiContext prepareContext( ITsGuiContext aContext ) {
    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_SUMMARY_PANE.setValue( aContext.params(), AV_TRUE );
    OPDEF_DETAILS_PANE_PLACE.setValue( aContext.params(), avValobj( EBorderLayoutPlacement.EAST ) );
    return aContext;
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {

    aActs.addAll( ACDEF_SEPARATOR );

    // TODO add action "move to another PLEP"

    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected IMpcSummaryPane<IStir> doCreateSummaryPane() {
    return new StirSummaryPane( this );
  }

}
