package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.episodes.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Panel to list episodes.
 *
 * @author hazard157
 */
class EpisodeMpc
    extends MultiPaneComponentModown<IEpisode> {

  public EpisodeMpc( ITsGuiContext aContext, IM5Model<IEpisode> aModel, IM5ItemsProvider<IEpisode> aItemsProvider,
      IM5LifecycleManager<IEpisode> aLifecycleManager ) {
    super( prepareContext( aContext ), aModel, aItemsProvider, aLifecycleManager );
    TreeModeInfo<IEpisode> tmi1 = new TreeModeInfo<>( "ByPlace", //$NON-NLS-1$
        STR_TMI_BY_PLACE, STR_TMI_BY_PLACE_D, null, new TreeMakerEpisodeByPlace() );
    treeModeManager().addTreeMode( tmi1 );
    TreeModeInfo<IEpisode> tmi2 = new TreeModeInfo<>( "ByMonth", //$NON-NLS-1$
        STR_TMI_BY_MONTH, STR_TMI_BY_MONTH_D, null, new TreeMakerEpisodeByMonth() );
    treeModeManager().addTreeMode( tmi2 );
    TreeModeInfo<IEpisode> tmi3 = new TreeModeInfo<>( "ByYear", //$NON-NLS-1$
        STR_TMI_BY_YEAR, STR_TMI_BY_YEAR_D, null, new TreeMakerEpisodeByYear() );
    treeModeManager().addTreeMode( tmi3 );
  }

  private static ITsGuiContext prepareContext( ITsGuiContext aContext ) {
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
    return aContext;
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    int index = aActs.indexOf( ACDEF_COLLAPSE_ALL );
    aActs.insert( index, ACDEF_SEPARATOR );
    aActs.insert( index, ACDEF_FILTER_CHECK );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_FILTER: {
        boolean showOnlyUndone = toolbar().isActionChecked( ACTID_FILTER );
        if( showOnlyUndone ) {
          tree().filterManager().setFilter( aObj -> !aObj.info().notes().isEmpty() );
        }
        else {
          tree().filterManager().setFilter( ITsFilter.ALL );
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aActionId );
    }
  }

}
