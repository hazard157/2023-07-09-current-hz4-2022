package com.hazard157.prisex24.m5.note;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.note.MarkNoteM5Model.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.utils.playmenu.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link MarkNoteM5Model}.
 *
 * @author hazard157
 */
class MarkNoteMpc
    extends MultiPaneComponentModown<MarkNote>
    implements IPsxGuiContextable {

  private final ITsSelectionChangeListener<MarkNote> treeSelectionChangeListener =
      ( aSource, aSelectedItem ) -> addPlayMenu( aSelectedItem );

  public MarkNoteMpc( ITsGuiContext aContext, IM5Model<MarkNote> aModel, IM5ItemsProvider<MarkNote> aItemsProvider,
      IM5LifecycleManager<MarkNote> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    addTsSelectionListener( treeSelectionChangeListener );
  }

  void addPlayMenu( MarkNote aSel ) {
    if( aSel != null ) {
      toolbar().setActionMenu( ACTID_PLAY, new AbstractPlayMenuCreator( tsContext() ) {

        @Override
        protected Svin getPlayableSvin() {
          return getItemSvin( aSel );
        }
      } );
      updateActionsState();
    }
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    aActs.add( ACDEF_SEPARATOR );
    aActs.add( ACDEF_PLAY_MENU );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    MarkNote sel = selectedItem();
    switch( aActionId ) {
      case ACTID_PLAY: {
        Svin svin = getItemSvin( sel );
        if( svin != null ) {
          psxService().playEpisodeVideo( svin );
        }
        break;
      }
      default:
        break;
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, MarkNote aSel ) {
    ICurrentEpisodeService ces = tsContext().get( ICurrentEpisodeService.class );
    IEpisode e = ces.current();
    toolbar().setActionEnabled( ACTID_PLAY, aIsSel && e != null );
  }

  public Svin getItemSvin( MarkNote aItem ) {
    if( aItem == null ) {
      return null;
    }
    ICurrentEpisodeService ces = tsContext().get( ICurrentEpisodeService.class );
    IEpisode e = ces.current();
    if( e == null ) {
      return null;
    }
    String episodeId = e.id();
    String camId = e.frame().cameraId();
    return new Svin( episodeId, camId, aItem.in() );
  }

  @Override
  protected MarkNote doAddItem() {
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext() );
    IM5BunchEdit<MarkNote> initVals = new M5BunchEdit<>( model() );
    Secint in = new Secint( 0, ((INoteLine)lifecycleManager().master()).duration() - 1 );
    INTERVAL.setFieldValue( initVals, in );
    return M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lifecycleManager() );
  }

}
