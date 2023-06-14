package com.hazard157.prisex24.m5.todos;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.todos.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.sql.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Panel creator for {@link ITodo}.
 *
 * @author hazard157
 */
public class TodoM5ModelPanelCreator
    extends M5DefaultPanelCreator<ITodo> {

  static final ITsNodeKind<String>    NK_DONE     = new TsNodeKind<>( "Done", String.class, true );        //$NON-NLS-1$
  static final ITsNodeKind<EPriority> NK_PRIORITY = new TsNodeKind<>( "Priority", EPriority.class, true ); //$NON-NLS-1$
  static final ITsNodeKind<ITodo>     NK_TODO     = new TsNodeKind<>( "Todo", ITodo.class, false );        //$NON-NLS-1$

  static class TreeMakerByDone
      implements ITsTreeMaker<ITodo> {

    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ITodo> aItems ) {
      DefaultTsNode<String> nodeDone = new DefaultTsNode<>( NK_DONE, aRootNode, STR_NODE_DONE );
      DefaultTsNode<String> nodeUndone = new DefaultTsNode<>( NK_DONE, aRootNode, STR_NODE_UNDONE );
      for( ITodo t : aItems ) {
        DefaultTsNode<String> parent;
        if( t.isDone() ) {
          parent = nodeDone;
        }
        else {
          parent = nodeUndone;
        }
        DefaultTsNode<ITodo> n = new DefaultTsNode<>( NK_TODO, parent, t );
        parent.addNode( n );
      }
      return new ElemArrayList<>( nodeDone, nodeUndone );
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_TODO;
    }

  }

  static class TreeMakerByPriority
      implements ITsTreeMaker<ITodo> {

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ITodo> aItems ) {
      IMapEdit<EPriority, DefaultTsNode<EPriority>> prNodes = new ElemMap<>();
      for( EPriority p : EPriority.values() ) {
        prNodes.put( p, new DefaultTsNode<>( NK_PRIORITY, aRootNode, p ) );
      }
      for( ITodo t : aItems ) {
        DefaultTsNode<EPriority> parent = prNodes.getByKey( t.priority() );
        DefaultTsNode<ITodo> n = new DefaultTsNode<>( NK_TODO, parent, t );
        parent.addNode( n );
      }
      return (IList)prNodes.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_TODO;
    }

  }

  /**
   * Constructor.
   */
  public TodoM5ModelPanelCreator() {
    // nop
  }

  @Override
  protected IM5CollectionPanel<ITodo> doCreateCollEditPanel( ITsGuiContext aContext,
      IM5ItemsProvider<ITodo> aItemsProvider, IM5LifecycleManager<ITodo> aLifecycleManager ) {
    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
    OPDEF_DETAILS_PANE_PLACE.setValue( aContext.params(), avValobj( EBorderLayoutPlacement.EAST ) );
    TreeModeInfo<ITodo> tmi1 = new TreeModeInfo<>( "ByDone", //$NON-NLS-1$
        STR_TMM_BY_DONE, STR_TMM_BY_DONE_D, null, new TreeMakerByDone() );
    TreeModeInfo<ITodo> tmi2 = new TreeModeInfo<>( "ByPriority", //$NON-NLS-1$
        STR_TMM_BY_PRIORITY, STR_TMM_BY_PRIORITY_D, null, new TreeMakerByPriority() );
    MultiPaneComponentModown<ITodo> mpc =
        new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager ) {

          @Override
          protected void doAdjustEntityCreationInitialValues( IM5BunchEdit<ITodo> aValues ) {
            aValues.set( FID_CREATION_TIME, avTimestamp( new Timestamp( System.currentTimeMillis() ).getTime() ) );
          }

          @Override
          protected ITsToolbar doCreateToolbar( ITsGuiContext aCtx, String aName, EIconSize aIconSize,
              IListEdit<ITsActionDef> aActs ) {
            aActs.add( ACDEF_SEPARATOR );
            aActs.add( ACDEF_FILTER_CHECK );
            return super.doCreateToolbar( aCtx, aName, aIconSize, aActs );
          }

          @Override
          protected void doProcessAction( String aActionId ) {
            switch( aActionId ) {
              case ACTID_FILTER: {
                boolean onlyUndone = toolbar().isActionChecked( ACTID_FILTER );
                if( onlyUndone ) {
                  tree().filterManager().setFilter( aObj -> !aObj.isDone() );
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

        };
    mpc.treeModeManager().addTreeMode( tmi1 );
    mpc.treeModeManager().addTreeMode( tmi2 );
    return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
  }

  @Override
  protected IM5MultiLookupPanel<ITodo> doCreateMultiLookupPanel( ITsGuiContext aContext,
      IM5LookupProvider<ITodo> aLookupProvider ) {
    OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
    MultiPaneComponentLookup<ITodo> mpc = new MultiPaneComponentLookup<>( aContext, model(), aLookupProvider );
    return new M5MultiLookupPanelMpcLookupWrapper<>( mpc, false );
  }

  @Override
  protected IM5EntityPanel<ITodo> doCreateEntityEditorPanel( ITsGuiContext aContext,
      IM5LifecycleManager<ITodo> aLifecycleManager ) {
    return new M5DefaultEntityEditorPanel<>( aContext, model(), aLifecycleManager ) {

      @Override
      protected void doInitLayout() {
        IVecTabLayout lTabs = new VecTabLayout( false );
        // Properties tab
        IVecLadderLayout lProps = new VecLadderLayout( true );
        IVecBoard bProps = new VecBoard();
        bProps.setLayout( lProps );
        lTabs.addControl( bProps, new VecTabLayoutData( TAB_TODO_PROPS, TAB_TODO_PROPS_D ) );
        // related Todos tab
        IVecBorderLayout lTodos = new VecBorderLayout();
        IVecBoard bTodos = new VecBoard();
        bTodos.setLayout( lTodos );
        lTabs.addControl( bTodos, new VecTabLayoutData( TAB_TODO_TODOS, TAB_TODO_TODOS_D ) );
        // Fulfill stages tab
        IVecBorderLayout lStages = new VecBorderLayout();
        IVecBoard bStages = new VecBoard();
        bStages.setLayout( lStages );
        lTabs.addControl( bStages, new VecTabLayoutData( TAB_TODO_STAGES, TAB_TODO_STAGES_D ) );

        for( String fieldId : editors().keys() ) {
          IValedControl<?> varEditor = editors().getByKey( fieldId );
          switch( fieldId ) {
            case FID_RELATED_TODO_IDS: {
              lTodos.addControl( varEditor, EBorderLayoutPlacement.CENTER );
              break;
            }
            case FID_FULFIL_STAGES: {
              lStages.addControl( varEditor, EBorderLayoutPlacement.CENTER );
              break;
            }
            default: {
              int verSpan = varEditor.params().getInt( OPDEF_VERTICAL_SPAN );
              boolean isPrefWidthFixed = varEditor.params().getBool( OPDEF_IS_WIDTH_FIXED );
              boolean isPrefHeighFixed = varEditor.params().getBool( OPDEF_IS_HEIGHT_FIXED );
              boolean useLabel = !varEditor.params().getBool( OPDEF_NO_FIELD_LABEL );
              EHorAlignment horAl = isPrefWidthFixed ? EHorAlignment.LEFT : EHorAlignment.FILL;
              EVerAlignment verAl = isPrefHeighFixed ? EVerAlignment.TOP : EVerAlignment.FILL;
              IM5FieldDef<?, ?> fd = model().fieldDefs().getByKey( fieldId );
              String label = TsLibUtils.EMPTY_STRING;
              if( !fd.nmName().isEmpty() ) {
                label = fd.nmName() + ": "; //$NON-NLS-1$
              }
              IVecLadderLayoutData layoutData =
                  new VecLadderLayoutData( useLabel, false, verSpan, label, horAl, verAl );
              lProps.addControl( varEditor, layoutData );
              break;
            }
          }
        }
        board().setLayout( lTabs );
        return;
      }

    };
  }

}
