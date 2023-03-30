package com.hazard157.psx24.core.m5.tags;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.m5.tags.IPsxResource.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.jface.action.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;

import com.hazard157.psx.proj3.tags.*;

class TagMpc
    extends MultiPaneComponentModown<ITag> {

  private final IRootTag rootTag;

  TagMpc( ITsGuiContext aContext, IM5Model<ITag> aModel, IM5ItemsProvider<ITag> aItemsProvider,
      IM5LifecycleManager<ITag> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    rootTag = aContext.get( IRootTag.class );
    TreeModeInfo<ITag> tmi1 = new TreeModeInfo<>( "ByGroup", //$NON-NLS-1$
        STR_N_TMI_BY_GROUP, STR_D_TMI_BY_GROUP, ICON_TAGS_LIST, new TreeMakerByGroups( rootTag ) );
    treeModeManager().addTreeMode( tmi1 );
    treeModeManager().setCurrentMode( tmi1.id() );
  }

  @Override
  protected ITag doAddItem() {
    // определение родительского узла подготовка диалога
    ITag master = selectedItem();
    String title;
    if( master != null ) {
      if( ITagsConstants.IS_LEAF.getValue( master.params() ).asBool() ) {
        TsDialogUtils.error( getShell(), FMT_ERR_NO_CHILDS_IN_LEAF, master.id() );
        return null;
      }
      title = String.format( DLG_T_FMT_NEW_CHILD_TAG, master.id() );
    }
    else {
      master = rootTag;
      title = DLG_T_NEW_ROOT_TAG;
    }
    TsDialogInfo cdi = new TsDialogInfo( tsContext(), DLG_C_NEW_TAG, title );
    // подготовка менеджера ЖЦ
    IM5LifecycleManager<ITag> lm = new TagLifecycleManager( model(), master );
    // вызов диалога создания ярлыка
    return M5GuiUtils.askCreate( tsContext(), model(), null, cdi, lm );
  }

  boolean canAddToSelection() {
    ITag sel = selectedItem();
    if( sel == null ) {
      return true; // нет выбранногоэлемента - значит создание корневого элемента
    }
    if( ITagsConstants.IS_LEAF.getValue( sel.params() ).asBool() ) {
      return false;
    }
    return true;
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ITag aSel ) {
    IAction a = toolbar().findAction( ACTID_ADD );
    if( a != null ) {
      a.setEnabled( a.isEnabled() && canAddToSelection() );
    }
  }

}
