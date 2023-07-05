package com.hazard157.psx24.explorer.m5;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.explorer.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx24.core.bricks.unit.*;

class InquiryMpc
    extends MultiPaneComponentModown<Inquiry> {

  private static final String COPIED_INQUIRY_ID_SUFFIX = ".copy"; //$NON-NLS-1$

  private static final String AID_COPY_INQUIRY = PSX_ACT_ID + ".CopyInquiry"; //$NON-NLS-1$

  private static final ITsActionDef AI_COPY_INQUIRY = TsActionDef.ofPush2( AID_COPY_INQUIRY, //
      STR_N_COPY_INQUIRY, STR_D_COPY_INQUIRY, ICONID_EDIT_COPY );

  public static final ITsNodeKind<String>  NK_GROUP = new TsNodeKind<>( "IdGroup", String.class, true );  //$NON-NLS-1$
  public static final ITsNodeKind<Inquiry> NK_INQ   = new TsNodeKind<>( "Inquiry", Inquiry.class, true ); //$NON-NLS-1$

  static class TreeMakerByIds
      implements ITsTreeMaker<Inquiry> {

    private DefaultTsNode<?> ensureGroupNode( String aId, IStridablesList<Inquiry> aItems,
        IStringMapEdit<DefaultTsNode<?>> aGroupNodes, ITsNode aRootNode, IListEdit<ITsNode> aTopLevelNodes ) {
      // если узел группы уже есть, выходим
      DefaultTsNode<?> gn = aGroupNodes.findByKey( aId );
      if( gn != null ) {
        return gn;
      }
      ITsNode parent = aRootNode;
      if( StridUtils.isIdAPath( aId ) ) {
        String grandpaId = StridUtils.removeTailingIdNames( aId, 1 );
        parent = ensureGroupNode( grandpaId, aItems, aGroupNodes, aRootNode, aTopLevelNodes );
      }
      Inquiry inq = aItems.findByKey( aId );
      if( inq != null ) { // создаем группирующий узел с запросом
        gn = new DefaultTsNode<>( NK_INQ, parent, inq );
      }
      else { // создаем группирующий узел со строкой идентификатора
        gn = new DefaultTsNode<>( NK_GROUP, parent, aId );
        gn.setName( StridUtils.getLast( aId ) );
      }
      if( parent == aRootNode ) {
        aTopLevelNodes.add( gn );
      }
      else {
        ((DefaultTsNode<?>)parent).addNode( gn );
      }
      aGroupNodes.put( aId, gn );
      return gn;
    }

    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Inquiry> aItems2 ) {
      IListEdit<ITsNode> topLevelNodes = new ElemArrayList<>();
      // делаем сортированный список
      IStridablesListBasicEdit<Inquiry> items = new SortedStridablesList<>();
      items.addAll( aItems2 );
      // сформируем карту групповых узлов
      IStringMapEdit<DefaultTsNode<?>> groupNodes = new StringMap<>();
      for( Inquiry i : items ) {
        int compsCount = StridUtils.getComponents( i.id() ).size();
        // запросы с ИД-именем пропускаем
        if( compsCount == 1 ) {
          continue;
        }
        // создаем групповой узел
        String parentId = StridUtils.removeTailingIdNames( i.id(), 1 );
        ensureGroupNode( parentId, items, groupNodes, aRootNode, topLevelNodes );
      }
      // теперь создаем листья
      for( Inquiry i : items ) {
        // запросы с ИД-именем пока пропускаем
        if( !StridUtils.isIdAPath( i.id() ) ) {
          continue;
        }
        // создаем узлы, которых пока нет
        if( !groupNodes.hasKey( i.id() ) ) {
          String parentId = StridUtils.removeTailingIdNames( i.id(), 1 );
          DefaultTsNode<?> parent = groupNodes.getByKey( parentId );
          DefaultTsNode<Inquiry> node = new DefaultTsNode<>( NK_INQ, parent, i );
          parent.addNode( node );
        }
      }
      // в конце - запросы с одним ИД-именем
      for( Inquiry i : items ) {
        if( !StridUtils.isIdAPath( i.id() ) && !groupNodes.hasKey( i.id() ) ) {
          DefaultTsNode<Inquiry> n = new DefaultTsNode<>( NK_INQ, aRootNode, i );
          topLevelNodes.add( n );
          continue;
        }
      }
      return topLevelNodes;
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_INQ;
    }

  }

  InquiryMpc( ITsGuiContext aContext, IM5Model<Inquiry> aModel, IM5ItemsProvider<Inquiry> aItemsProvider,
      IM5LifecycleManager<Inquiry> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    TreeModeInfo<Inquiry> tmById = new TreeModeInfo<>( "ById", //$NON-NLS-1$
        STR_N_TMI_BY_ID, STR_D_TMI_BY_ID, null, new TreeMakerByIds() );
    treeModeManager().addTreeMode( tmById );
    treeModeManager().setCurrentMode( tmById.id() );
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    int index = aActs.indexOf( ACDEF_ADD );
    if( index >= 0 ) {
      aActs.insert( index + 1, AI_COPY_INQUIRY );
    }
    else {
      aActs.add( ACDEF_SEPARATOR );
      aActs.add( AI_COPY_INQUIRY );
    }
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    switch( aActionId ) {
      case AID_COPY_INQUIRY:
        Inquiry sel = selectedItem();
        if( sel == null ) {
          return;
        }
        ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
        IM5BunchEdit<Inquiry> initVals = new M5BunchEdit<>( model() );
        initVals.set( FID_ID, avStr( sel.id() + COPIED_INQUIRY_ID_SUFFIX ) );
        initVals.set( FID_NAME, avStr( sel.nmName() ) );
        initVals.set( FID_DESCRIPTION, avStr( sel.description() ) );
        Inquiry inq = M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lifecycleManager() );
        if( inq != null ) {
          for( InquiryItem i : sel.items() ) {
            inq.items().add( new InquiryItem( i ) );
          }
          fillViewer( inq );
        }
        break;
      default:
        break;
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, Inquiry aSel ) {
    toolbar().setActionEnabled( AID_COPY_INQUIRY, aIsSel );
    tree().columnManager().columns().getByKey( FID_ID ).pack();
  }

}
