package com.hazard157.prisex24.m5.frames;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IM5CollectionViewer} implementation for {@link FrameM5Model}.
 * <p>
 * TODO add ability to view non-sec frames
 *
 * @author hazard157
 */
class FrameCollectionPanel
    extends AbstractTsStdEventsProducerLazyPanel<IFrame, Control>
    implements IM5CollectionPanel<IFrame>, ITsActionHandler, IPsxGuiContextable {

  // TODO tree mode management

  // TODO create columns

  // TODO selection -> image update

  private final IM5Model<IFrame>         model;
  private final ITreeModeManager<IFrame> treeModeManager;

  private final TsToolbar             toolbar;
  private final IM5TreeViewer<IFrame> framesTree;
  private final IPdwWidget            imageWidget;

  private IM5ItemsProvider<IFrame>    itemsProvider    = IM5ItemsProvider.EMPTY;
  private IM5LifecycleManager<IFrame> lifecycleManager = null;

  private TsComposite board = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the frames model, caller of this constructor
   * @param aItemsProvider {@link IM5ItemsProvider} - items provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FrameCollectionPanel( ITsGuiContext aContext, IM5Model<IFrame> aModel,
      IM5ItemsProvider<IFrame> aItemsProvider ) {
    super( aContext );
    TsNullArgumentRtException.checkNulls( aModel, aItemsProvider );
    model = aModel;
    itemsProvider = aItemsProvider;
    // toolbar
    toolbar = new TsToolbar( tsContext() );
    toolbar.setIconSize( EIconSize.IS_24X24 );
    toolbar.setVertical( false );
    toolbar.setActionDefs( //
        // TODO GIF management menu, ACDEF_SEPARATOR, //
        // TODO AnumationKind menu, ACDEF_SEPARATOR, //
        // TODO Shown cameras menu, ACDEF_SEPARATOR, //
        ACDEF_VIEW_AS_TREE_MENU, ACDEF_VIEW_AS_LIST, ACDEF_SEPARATOR, //
        ACDEF_COLLAPSE_ALL, ACDEF_EXPAND_ALL, //
        // TODO AI_COPY_FRAME, //
        ACDEF_PLAY_MENU, //
        ACDEF_GO_PREV, ACDEF_GO_NEXT //
    );
    toolbar.addListener( this );
    // framesTree
    framesTree = new M5TreeViewer<>( tsContext(), model, false );
    // imageWidget
    imageWidget = new PdwWidgetSimple( tsContext() );
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.setPreferredSizeFixed( false );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    imageWidget.setAreaPreferredSize( new TsPoint( 720, 576 ) );
    // treeModeManager
    treeModeManager = new FrameCollectionPanelTmm( tsContext() ) {

      @Override
      protected void onCurrentModeIdChanged() {
        if( !isCurrentTreeMode() ) {
          framesTree.setTreeMaker( null );
          return;
        }
        TreeModeInfo<IFrame> info = treeModeInfoes().findByKey( currModeId() );
        if( info == null ) {
          framesTree.setTreeMaker( null );
          return;
        }
        framesTree.setTreeMaker( info.treeMaker() );
      }

    };
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void fillViewer( IFrame aToSelect ) {
    if( board != null ) {
      framesTree.items().setAll( itemsProvider.listItems() );
      framesTree.setSelectedItem( aToSelect );
      updateActionsState();
    }
  }

  private void updateActionsState() {
    IFrame sel = framesTree.selectedItem();
    boolean isSel = sel != null;
    boolean isAnim = sel != null && sel.isDefined() && sel.isAnimated();
    // enables
    toolbar.setActionEnabled( ACTID_PLAY, isSel );
    toolbar.setActionEnabled( ACTID_VIEW_AS_LIST, !treeModeManager.isCurrentTreeMode() );
    toolbar.setActionEnabled( ACTID_GO_PREV, !framesTree.items().isEmpty() && sel != framesTree.items().first() );
    toolbar.setActionEnabled( ACTID_GO_NEXT, !framesTree.items().isEmpty() && sel != framesTree.items().last() );
    // checks
    toolbar.setActionChecked( ACTID_VIEW_AS_LIST, !treeModeManager.isCurrentTreeMode() );
    toolbar.setActionChecked( ACTID_VIEW_AS_TREE, treeModeManager.isCurrentTreeMode() );
  }

  private void whenFramesTreeSelectionChange( IFrame aSel ) {
    TsImage img = null;
    if( aSel != null ) {
      File file = cofsFrames().findFrameFile( aSel );
      if( file != null ) {
        img = imageManager().findImage( file );
      }
    }
    imageWidget.setTsImage( img );
    imageWidget.redraw();
    // TODO FrameCollectionPanel.whenFramesTreeSelectionChange()
    selectionChangeEventHelper.fireTsSelectionEvent( aSel );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    IFrame sel = framesTree.selectedItem();
    switch( aActionId ) {
      case "": {
        // TODO FrameCollectionPanel.handleAction()
        break;
      }
      case ACTID_VIEW_AS_TREE: {
        treeModeManager.setCurrentMode( treeModeManager.lastModeId() );
        break;
      }
      case ACTID_VIEW_AS_LIST: {
        treeModeManager.setCurrentMode( null );
        break;
      }
      case ACTID_GO_PREV: {
        IFrame frame = ETsCollMove.PREV.findElemAt( sel, framesTree.items(), 10, false );
        setSelectedItem( frame );
        break;
      }
      case ACTID_GO_NEXT: {
        IFrame frame = ETsCollMove.NEXT.findElemAt( sel, framesTree.items(), 10, false );
        setSelectedItem( frame );
        break;
      }
      case ACTID_COLLAPSE_ALL: {
        framesTree.console().collapseAll();
        break;
      }
      case ACTID_EXPAND_ALL: {
        framesTree.console().expandAll();
        break;
      }
      default:
        TsDialogUtils.warn( getShell(), aActionId );
    }
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsStdEventsProducerLazyPanel
  //

  @Override
  public IFrame selectedItem() {
    return framesTree.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    framesTree.setSelectedItem( aItem );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    // board
    board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // toolbar
    toolbar.createControl( board );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.setActionMenu( ACTID_VIEW_AS_TREE, new TreeModeDropDownMenuCreator<>( tsContext(), treeModeManager ) );
    // sfMain: framesTree, imageWidget
    SashForm sfMain = new SashForm( board, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );
    framesTree.createControl( sfMain );
    IM5Column<IFrame> col = framesTree.columnManager().add( FID_FRAME_NO );
    col.setWidth( 150 );
    framesTree.columnManager().add( FID_IS_ANIMATED );
    framesTree.columnManager().add( FID_CAMERA_ID );
    framesTree.setTreeMaker( null );
    imageWidget.createControl( sfMain );
    // setup
    sfMain.setWeights( 4000, 6000 );
    framesTree.addTsSelectionListener( ( src, sel ) -> whenFramesTreeSelectionChange( sel ) );
    return board;
  }

  // ------------------------------------------------------------------------------------
  // IGenericContentPanel
  //

  @Override
  final public boolean isViewer() {
    return true;
  }

  // ------------------------------------------------------------------------------------
  // IGenericCollPanel
  //

  @Override
  public IList<IFrame> items() {
    return framesTree.items();
  }

  @Override
  public void refresh() {
    fillViewer( framesTree.selectedItem() );
  }

  @Override
  public ITsCheckSupport<IFrame> checkSupport() {
    return framesTree.checks();
  }

  // ------------------------------------------------------------------------------------
  // IM5ModelRelated
  //

  @Override
  final public IM5Model<IFrame> model() {
    return model;
  }

  // ------------------------------------------------------------------------------------
  // IM5PanelBase
  //

  @Override
  final public boolean isEditable() {
    return false;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IM5CollectionPanel
  //

  @Override
  final public IM5ItemsProvider<IFrame> itemsProvider() {
    return itemsProvider;
  }

  @Override
  public void setItemsProvider( IM5ItemsProvider<IFrame> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aItemsProvider );
    itemsProvider = aItemsProvider;
    refresh();
  }

  @Override
  public IM5LifecycleManager<IFrame> lifecycleManager() {
    return lifecycleManager;
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<IFrame> aLifecycleManager ) {
    lifecycleManager = aLifecycleManager;
  }

}
