package com.hazard157.psx24.planning.m5;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.planning.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Реализация {@link MultiPaneComponentModown} для {@link PlepM5Model}.
 *
 * @author hazard157
 */
class PlepMpc
    extends MultiPaneComponentModown<IPlep> {

  private static final String COPIED_PLEP_ID_SUFFIX = "_copy"; //$NON-NLS-1$

  private static final String AID_COPY_PLEP = PSX_ACT_ID + ".CopyPlep"; //$NON-NLS-1$

  private static final ITsActionDef AI_COPY_PLEP = TsActionDef.ofPush2( AID_COPY_PLEP, //
      STR_N_COPY_PLEP, STR_D_COPY_PLEP, ICONID_EDIT_COPY );

  PlepMpc( ITsGuiContext aContext, IM5Model<IPlep> aModel, IM5ItemsProvider<IPlep> aItemsProvider,
      IM5LifecycleManager<IPlep> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    TreeModeInfo<IPlep> tmi1 = new TreeModeInfo<>( "ByPlace", //$NON-NLS-1$
        STR_N_TM_BY_PLACE, STR_D_TM_BY_PLACE, null, new PlepTreeMakerByPlace() );
    TreeModeInfo<IPlep> tmi2 = new TreeModeInfo<>( "ById", //$NON-NLS-1$
        STR_N_TM_BY_ID, STR_D_TM_BY_ID, null, new PlepTreeMakerById() );
    treeModeManager().addTreeMode( tmi1 );
    treeModeManager().addTreeMode( tmi2 );
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    int index = aActs.indexOf( ACDEF_ADD );
    if( index >= 0 ) {
      aActs.insert( index + 1, AI_COPY_PLEP );
    }
    else {
      aActs.add( ACDEF_SEPARATOR );
      aActs.add( AI_COPY_PLEP );
    }
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    switch( aActionId ) {
      case AID_COPY_PLEP: {
        IPlep sel = selectedItem();
        if( sel == null ) {
          return;
        }
        ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
        IM5BunchEdit<IPlep> initVals = new M5BunchEdit<>( model() );
        initVals.set( FID_ID, avStr( sel.id() + COPIED_PLEP_ID_SUFFIX ) );
        initVals.set( FID_NAME, avStr( sel.nmName() ) );
        initVals.set( FID_DESCRIPTION, avStr( sel.description() ) );
        initVals.set( IPlepM5Constants.FID_PLACE, avStr( sel.info().place() ) );
        IPlep plep = M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lifecycleManager() );
        if( plep != null ) {
          for( IStir s : sel.stirs() ) {
            plep.newStir( -1, s.params() );
          }
          for( ITrack t : sel.tracks() ) {
            plep.newTrack( -1, t.songId(), t.interval() );
          }
          fillViewer( plep );
        }

        // TODO PlepMpc.doProcessAction()
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, IPlep aSel ) {
    toolbar().setActionEnabled( AID_COPY_PLEP, aIsSel );
  }

}
