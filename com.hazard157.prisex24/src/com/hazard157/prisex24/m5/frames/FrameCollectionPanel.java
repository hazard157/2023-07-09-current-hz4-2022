package com.hazard157.prisex24.m5.frames;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.prisex24.utils.*;
import com.hazard157.prisex24.utils.gifmgmt.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IM5CollectionViewer} implementation for {@link FrameM5Model}.
 *
 * @author hazard157
 */
class FrameCollectionPanel
    extends AbstractTsStdEventsProducerLazyPanel<IFrame, Control>
    implements IM5CollectionPanel<IFrame>, ITsActionHandler, IPsxGuiContextable {

  private final IM5Model<IFrame> model;

  private final TsToolbar                toolbar;
  private final CommonFramesM5TreeViewer framesTree;
  private final IPdwWidget               imageWidget;
  private final NonsecFramesM5TreeViewer nonsecsList;

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
        ACDEF_GIF_CREATE_MENU, //
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
    framesTree = new CommonFramesM5TreeViewer( tsContext(), model );
    // imageWidget
    imageWidget = new PdwWidgetSimple( tsContext() );
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.setPreferredSizeFixed( false );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    imageWidget.setAreaPreferredSize( new TsPoint( 720, 576 ) );
    // nonsecsList
    nonsecsList = new NonsecFramesM5TreeViewer( tsContext(), model );
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
    toolbar.setActionEnabled( ACTID_VIEW_AS_LIST, !framesTree.tmm().isCurrentTreeMode() );
    toolbar.setActionEnabled( ACTID_GO_PREV, !framesTree.items().isEmpty() && sel != framesTree.items().first() );
    toolbar.setActionEnabled( ACTID_GO_NEXT, !framesTree.items().isEmpty() && sel != framesTree.items().last() );
    // checks
    toolbar.setActionChecked( ACTID_VIEW_AS_LIST, !framesTree.tmm().isCurrentTreeMode() );
    toolbar.setActionChecked( ACTID_VIEW_AS_TREE, framesTree.tmm().isCurrentTreeMode() );
  }

  private void whenFramesTreeSelectionChange( IFrame aSel ) {
    nonsecsList.setBaseFrame( aSel );
    selectionChangeEventHelper.fireTsSelectionEvent( aSel );
    updateActionsState();
  }

  private void whenNonsecsSelectionChanged( IFrame aSel ) {
    TsImage img = null;
    if( aSel != null ) {
      File file = cofsFrames().findFrameFile( aSel );
      if( file != null ) {
        img = imageManager().findImage( file );
      }
    }
    imageWidget.setTsImage( img );
    imageWidget.redraw();
  }

  /**
   * Returns the displayed frame which may be non-seconds aligned one.
   *
   * @return {@link IFrame} - displayed frame or <code>null</code>
   */
  private IFrame getDisplayedFrame() {
    // TODO FrameCollectionPanel.getSelectedFrame()
    return selectedItem();
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_VIEW_AS_TREE: {
        framesTree.tmm().setNextMode();
        break;
      }
      case ACTID_VIEW_AS_LIST: {
        framesTree.tmm().setCurrentMode( null );
        break;
      }
      case ACTID_GO_PREV: {
        nonsecsList.moveSelection( ETsCollMove.PREV );
        break;
      }
      case ACTID_GO_NEXT: {
        nonsecsList.moveSelection( ETsCollMove.NEXT );
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
      // TODO implement actions
      // TODO GIF actions move to AbstractGifManagemntDropDownMenuCreator
      case ACTID_GIF_CREATE:
      case ACTID_GIF_RECREATE_ALL:
      case ACTID_GIF_REMOVE:
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
    toolbar.setActionMenu( ACTID_VIEW_AS_TREE, new TreeModeDropDownMenuCreator<>( tsContext(), framesTree.tmm() ) );
    // mainBoard
    TsComposite mainBoard = new TsComposite( board );
    mainBoard.setLayoutData( BorderLayout.CENTER );
    mainBoard.setLayout( new BorderLayout() );
    // frameTree
    framesTree.createControl( mainBoard );
    framesTree.getControl().setLayoutData( BorderLayout.WEST );
    // imageWidget
    imageWidget.createControl( mainBoard );
    imageWidget.getControl().setLayoutData( BorderLayout.CENTER );
    // nonsecsList
    nonsecsList.createControl( mainBoard );
    nonsecsList.getControl().setLayoutData( BorderLayout.EAST );
    nonsecsList.addTsSelectionListener( ( src, sel ) -> whenNonsecsSelectionChanged( sel ) );
    // setup
    toolbar.setActionMenu( ACTID_GIF_CREATE, new AbstractGifManagemntDropDownMenuCreator( tsContext(), this ) {

      @Override
      protected IFrame doGetFrame() {
        return getDisplayedFrame();
      }
    } );
    imageWidget.addTsUserInputListener( new ITsUserInputListener() {

      @Override
      public boolean onMouseWheel( Object aSource, int aState, ITsPoint aCoors, Control aWidget, int aScrollLines ) {
        if( aScrollLines < 0 ) {
          nonsecsList.moveSelection( ETsCollMove.NEXT );
        }
        if( aScrollLines > 0 ) {
          nonsecsList.moveSelection( ETsCollMove.PREV );
        }
        return true;
      }
    } );
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
