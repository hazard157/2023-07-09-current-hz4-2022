package com.hazard157.prisex24.e4.uiparts.episodes;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.e4.uiparts.episodes.IPsxResources.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.checkstate.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.utils.treeops.*;
import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.glib.tagint.*;
import com.hazard157.prisex24.m5.episodes.*;
import com.hazard157.prisex24.utils.playmenu.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * UIpart: episode {@link IEpisode#story() story} viewer and editor.
 *
 * @author hazard157
 */
public class UipartEpisodeStory
    extends AbstractEpisodeUipart
    implements ITsKeyInputListener, ITsActionHandler {

  @Inject
  IPsxSelectedSvinsService selectedSvinsService;

  // ------------------------------------------------------------------------------------
  // Work with tree
  //

  static final ITsNodeKind<IScene> NK_SCENE = new TsNodeKind<>( "Scene", IScene.class, true ); //$NON-NLS-1$

  /**
   * Groups scenes.
   */
  private final ITsTreeMaker<IScene> treeMakerScenes = new ITsTreeMaker<>() {

    private void addChilds( DefaultTsNode<IScene> aNode ) {
      for( IScene s : aNode.entity().childScenes() ) {
        DefaultTsNode<IScene> childNode = new DefaultTsNode<>( NK_SCENE, aNode, s );
        aNode.addNode( childNode );
        addChilds( childNode );
      }
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IScene> aItems ) {
      if( episode() == null ) {
        return IList.EMPTY;
      }
      // составим дерево из сюжета эпизода, игнорируя аргумент aItems
      IListEdit<DefaultTsNode<IScene>> rootNodes = new ElemArrayList<>();
      for( IScene s : episode().story().childScenes() ) {
        DefaultTsNode<IScene> rNode = new DefaultTsNode<>( NK_SCENE, aRootNode, s );
        rootNodes.add( rNode );
        addChilds( rNode );
      }
      return (IList)rootNodes;
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return true;
    }

  };

  /**
   * Scene change listener - updates tags marks panel, changes {@link IPsxSelectedSvinsService}.
   */
  private final ITsSelectionChangeListener<IScene> selectedSceneChangeListener = ( aSource, aSelectedItem ) -> {
    this.toolbar.setActionMenu( ACTID_PLAY, new AbstractPlayMenuCreator( tsContext() ) {

      @Override
      protected Svin getPlayableSvin() {
        return getItemSvinOrNull( aSelectedItem );
      }
    } );
    Svin svin = getItemSvinOrNull( aSelectedItem );
    if( svin != null ) {
      svin = Svin.removeCamId( svin );
    }
    updateTagMarksPanel();
    selectedSvinsService.setSvin( svin );
    updateActionsState();
  };

  /**
   * Scene double click handler - invokes scene edit dialog.
   */
  private final ITsDoubleClickListener<IScene> sceneDoubleClickListener =
      ( aSource, aSelectedItem ) -> handleAction( ACTID_EDIT );

  /**
   * Tag double click listener - TODO locates on tagline item.
   */
  private final ITsDoubleClickListener<ITag> tagDoubleClickListener = ( aSource, aSelectedItem ) -> {
    if( aSelectedItem != null ) {
      // FIXME locate
      // ITsE4Helper helper = tsContext().get( ITsE4Helper.class );
      // Location l = new Location( IPsx3ModCoreGuiConstants.PERSPID_EPISODES, PARTID_EP_TAGLINE, aSelectedItem );
      // helper.locator().gotoLocation( l );
    }
  };

  /**
   * Tam marks change listener - edits scene marking.
   */
  private final ITagMarksCheckStateChangeListener tagMarksCheckStateChangeListener =
      new ITagMarksCheckStateChangeListener() {

        //

        @Override
        public void onTagsUnchecked( IStringList aTagIds ) {
          IScene sel = treeViewer.selectedItem();
          if( sel != null ) {
            setSceneTagMarks( aTagIds, sel.interval(), false );
          }
        }

        @Override
        public void onTagsChecked( IStringList aTagIds ) {
          IScene sel = treeViewer.selectedItem();
          if( sel != null ) {
            setSceneTagMarks( aTagIds, sel.interval(), true );
          }
        }
      };

  // ------------------------------------------------------------------------------------
  // Toolbar
  //

  private static final String AID_ADD_TAG_INTERVAL = PSX_ACT_ID + ".AddTagInterval"; //$NON-NLS-1$

  private static final ITsActionDef AI_ADD_TAG_INTERVAL = TsActionDef.ofPush2( AID_ADD_TAG_INTERVAL, //
      ACT_ADD_TAG_INTERVAL, ACT_ADD_TAG_INTERVAL_D, ICONID_TAG );

  // ------------------------------------------------------------------------------------
  // Create and init
  //

  private static final EThumbSize DEFAULT_THUMB_SIZE = EThumbSize.SZ360;

  TsToolbar             toolbar;
  IM5Model<IScene>      scenesModel;
  IM5TreeViewer<IScene> treeViewer;
  TagMarksTreePanel     tagMarksPanel;
  IPdwWidget            frameWidget;
  EThumbSize            thumbSize = DEFAULT_THUMB_SIZE;

  @Override
  protected void doCreatePartContent( Composite aParent ) {
    aParent.setLayout( new BorderLayout() );
    scenesModel = m5().getModel( MID_SCENE, IScene.class );
    // toolbar
    toolbar = TsToolbar.create( aParent, tsContext(), STR_STORY_TOOLBAR_NAME, EIconSize.IS_16X16, //
        ACDEF_ADD, ACDEF_EDIT, ACDEF_REMOVE, ACDEF_SEPARATOR, //
        ACDEF_COLLAPSE_ALL, ACDEF_EXPAND_ALL, ACDEF_SEPARATOR, //
        AI_ADD_TAG_INTERVAL, ACDEF_SEPARATOR, //
        ACDEF_PLAY_MENU //
    );
    toolbar.setNameLabelText( STR_STORY_TOOLBAR_NAME );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( this );
    // sash
    SashForm sf = new SashForm( aParent, SWT.VERTICAL );
    sf.setLayoutData( BorderLayout.CENTER );
    // viewer
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    treeViewer = new M5TreeViewer<>( ctx, scenesModel, false );
    treeViewer.setTreeMaker( treeMakerScenes );
    treeViewer.createControl( sf );
    treeViewer.addTsSelectionListener( selectedSceneChangeListener );
    treeViewer.addTsDoubleClickListener( sceneDoubleClickListener );
    treeViewer.addTsKeyInputListener( this );
    for( IM5FieldDef<IScene, ?> fdef : scenesModel.fieldDefs() ) {
      if( (fdef.flags() & M5FF_COLUMN) != 0 ) {
        treeViewer.columnManager().add( fdef.id() );
      }
    }
    // bottomBoard
    TsComposite bottomBoard = new TsComposite( sf, SWT.NONE );
    bottomBoard.setLayout( new BorderLayout() );
    // tagMarksPanel
    tagMarksPanel = new TagMarksTreePanel( bottomBoard, tsContext().get( IUnitTags.class ), tsContext() );
    tagMarksPanel.addTagMarksCheckStateChangeListener( tagMarksCheckStateChangeListener );
    tagMarksPanel.setLayoutData( BorderLayout.CENTER );
    tagMarksPanel.addTsDoubleClickListener( tagDoubleClickListener );
    // frameWidget
    ctx = new TsGuiContext( tsContext() );
    frameWidget = new PdwWidgetSimple( ctx );
    frameWidget.createControl( bottomBoard );
    frameWidget.setAreaPreferredSize( EThumbSize.SZ256.pointSize() );
    frameWidget.setFitInfo( RectFitInfo.BEST );
    frameWidget.setFulcrum( ETsFulcrum.CENTER );
    frameWidget.setPreferredSizeFixed( true );
    frameWidget.getControl().setLayoutData( BorderLayout.EAST );
    // setup
    updateTagMarksPanel();
    sf.setWeights( 65, 35 );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    IScene sel = treeViewer.selectedItem();
    switch( aActionId ) {
      case ACTID_ADD:
        addScene();
        break;
      case ACTID_EDIT:
        if( sel != null ) {
          editScene( sel );
        }
        break;
      case ACTID_REMOVE:
        removeScene( sel );
        break;
      case ACTID_COLLAPSE_ALL:
        treeViewer.console().collapseAll();
        break;
      case ACTID_EXPAND_ALL:
        treeViewer.console().expandAll();
        break;
      case ACTID_PLAY:
        playItem( sel );
        break;
      case AID_ADD_TAG_INTERVAL:
        Secint in;
        if( sel != null ) {
          in = sel.interval();
        }
        else {
          in = new Secint( 0, episode().info().duration() - 1 );
        }
        ITag selTag = tagMarksPanel.selectedItem();
        String tagId = IStridable.NONE_ID;
        if( selTag != null ) {
          tagId = selTag.id();
        }
        Pair<String, Secint> initVals = new Pair<>( tagId, in );
        Secint allowIn = new Secint( 0, episode().info().duration() - 1 );
        Pair<String, Secint> vals = DialogTagInterval.select( tsContext(), initVals, allowIn );
        if( vals != null ) {
          setSceneTagMark( vals.left(), vals.right(), true );
          updateTagMarksPanel();
        }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateActionsState() {
    IScene sel = treeViewer.selectedItem();
    TsImage mi = null;
    if( sel != null ) {
      mi = psxService().findThumb( sel.frame(), thumbSize );
    }
    frameWidget.setTsImage( mi );
    frameWidget.redraw();

    boolean isSel = sel != null;
    boolean isAlive = episode() != null;
    toolbar.setActionEnabled( ACTID_ADD, isAlive );
    toolbar.setActionEnabled( ACTID_EDIT, isAlive && isSel );
    toolbar.setActionEnabled( ACTID_REMOVE, isAlive && isSel );
    toolbar.setActionEnabled( ACTID_PLAY, isAlive && isSel );
    toolbar.setActionEnabled( AID_ADD_TAG_INTERVAL, isAlive & isSel );
    tsContext().get( ITsE4Helper.class ).updateHandlersCanExecuteState();
  }

  /**
   * Refresh tag marks panel according to scenes panel state.
   */
  void updateTagMarksPanel() {
    IScene sel = treeViewer.selectedItem();
    if( sel != null ) {
      IStringMap<ECheckState> tagMarks = episode().tagLine().tagMarksIn( sel.interval() );
      tagMarksPanel.setTagMarks( tagMarks, false );
    }
    else {
      IStringMapEdit<ECheckState> tagMarks = new StringMap<>();
      IUnitTags tagman = tsContext().get( IUnitTags.class );
      for( ITag t : tagman.root().allNodesBelow() ) {
        tagMarks.put( t.id(), ECheckState.UNCHECKED );
      }
      tagMarksPanel.setTagMarks( tagMarks, false );
    }
    tagMarksPanel.setEnabled( sel != null );
  }

  /**
   * Plays the video corresponding to the scene.
   *
   * @param aItemOrNull {@link IScene} - scene, can be <code>null</code>
   */
  void playItem( IScene aItemOrNull ) {
    Svin svin = getItemSvinOrNull( aItemOrNull );
    if( svin != null ) {
      psxService().playEpisodeVideo( svin );
    }
  }

  /**
   * Returns the playback interval corresponding to the scene.
   *
   * @param aItemOrNull {@link IScene} - scene, can be <code>null</code>
   * @return {@link Svin} - replay interval or <code>null</code>
   */
  Svin getItemSvinOrNull( IScene aItemOrNull ) {
    if( aItemOrNull == null ) {
      return null;
    }
    String camId = aItemOrNull.frame().isDefined() ? aItemOrNull.frame().cameraId() : IStridable.NONE_ID;
    return new Svin( episode().id(), camId, aItemOrNull.interval() );
  }

  /**
   * Updates the play button drop-down menu on the toolbar.
   * <p>
   * Must be called every time the current (selected) scene in the tree changes.
   *
   * @param aSelectedItem {@link IScene} - current scene, may be <code>null</code>
   */
  void addPlayMenu( IScene aSelectedItem ) {
    // toolbar.setActionMenu( AID_PLAY,
    // tsContext().get( IPlayMenuSupport.class ).getPlayMenuCreator( tsContext(), new IPlayMenuParamsProvider() {
    //
    // @Override
    // public Svin playParams() {
    // return getItemSvinOrNull( aSelectedItem );
    // }
    //
    // @Override
    // public int spotlightSec() {
    // if( aSelectedItem != null ) {
    // IFrame f = aSelectedItem.frame();
    // if( f.isDefined() ) {
    // return f.secNo();
    // }
    // }
    // return -1;
    // }
    // } ) );
  }

  /**
   * Changes the state of the tag marks in the episode tagline and updates scenes properties.
   *
   * @param aTagIds {@link IStringList} - changed tag IDs
   * @param aIn {@link Secint} - changed interval
   * @param aSet boolean - new state of the mark
   */
  void setSceneTagMarks( IStringList aTagIds, Secint aIn, boolean aSet ) {
    if( episode() == null ) {
      return;
    }
    ITagLine tagline = episode().tagLine();
    for( String tagId : aTagIds ) {
      if( aSet ) {
        tagline.addMark( tagId, aIn );
      }
      else {
        tagline.removeMark( tagId, aIn );
      }
    }
    updateTagMarksPanel();
  }

  /**
   * Changes the state of the tag mark in the episode tagline and updates scenes properties.
   *
   * @param aTagId String - changed tag ID
   * @param aIn {@link Secint} - changed interval
   * @param aSet boolean - new state of the mark
   */
  void setSceneTagMark( String aTagId, Secint aIn, boolean aSet ) {
    if( episode() == null ) {
      return;
    }
    ITagLine tagline = episode().tagLine();
    if( aSet ) {
      tagline.addMark( aTagId, aIn );
    }
    else {
      tagline.removeMark( aTagId, aIn );
    }
    updateTagMarksPanel();
  }

  // ------------------------------------------------------------------------------------
  // Work with the scenes
  //

  /**
   * Adds a scene, after asking where relative to the current scene to place it.
   */
  private void addScene() {
    if( episode() == null ) {
      return;
    }
    IStory story = episode().story();
    // if the story is empty, go straight to adding the scene
    if( story.isEmpty() ) {
      doAddItem( story, story.interval() );
      return;
    }
    // in the tree, a scene must be selected, relative to which a new one is created
    IScene sel = treeViewer.selectedItem();
    if( sel == null ) {
      TsDialogUtils.warn( getShell(), MSG_WARN_FIRST_SELECT_SCENE );
      return;
    }
    IScene parent = sel.parent(); // всегда не-null
    int selIndex = parent.childScenes().keys().indexOf( sel.interval() );
    Secint inBefore = parent.inBeforeChild( selIndex );
    Secint inAfter = parent.inAfterChild( selIndex );
    Secint inChild = sel.isEmpty() ? sel.interval() : null;
    IListEdit<ENodeAddPlace> disabledPlaces = new ElemArrayList<>();
    if( inAfter == null ) {
      disabledPlaces.add( ENodeAddPlace.AFTER );
    }
    if( inChild == null ) {
      disabledPlaces.add( ENodeAddPlace.CHILD );
    }
    if( inBefore == null ) {
      disabledPlaces.add( ENodeAddPlace.BEFORE );
    }
    // if all possibilities are checked, then there is nowhere to add the scene
    if( disabledPlaces.size() == ENodeAddPlace.values().length ) {
      TsDialogUtils.error( getShell(), MSG_ERR_NO_PLACES_TO_ADD_SCENE );
      return;
    }
    ENodeAddPlace defPlace = ENodeAddPlace.AFTER; // default adding priority is AFTER, CHILD, BEFORE
    if( disabledPlaces.hasElem( defPlace ) ) {
      defPlace = ENodeAddPlace.CHILD;
    }
    if( disabledPlaces.hasElem( defPlace ) ) {
      defPlace = ENodeAddPlace.BEFORE;
    }
    // invoke dialog to determine scene adding location
    ITsDialogInfo cdi = new TsDialogInfo( tsContext(), DLG_ADD_SCENE_PLACE, DLG_ADD_SCENE_PLACE_D );
    ENodeAddPlace nap = TreeOpUtils.askNap( defPlace, cdi, disabledPlaces );
    if( nap == null ) {
      return;
    }
    IScene whomToAdd;
    Secint allowedInterval = switch( nap ) {
      case BEFORE -> {
        whomToAdd = parent;
        yield inBefore;
      }
      case AFTER -> {
        whomToAdd = parent;
        yield inAfter;
      }
      case CHILD -> {
        whomToAdd = sel;
        yield inChild;
      }
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    // invokde dialog and save edit changes
    doAddItem( whomToAdd, allowedInterval );
  }

  private void doAddItem( IScene aParent, Secint aAllowedInterval ) {
    IM5LifecycleManager<IScene> lm = scenesModel.getLifecycleManager( aParent );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    M5EntityPanelWithValedsController<IScene> ctrl = new SceneEditPanelController( episode().id(), aAllowedInterval );
    IM5EntityPanel<IScene> panel = scenesModel.panelCreator().createEntityControlledPanel( ctx, lm, ctrl );
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext() );
    int start = aAllowedInterval.start();
    int end = aAllowedInterval.start() + 1 + aAllowedInterval.end() / 3;
    if( end > aAllowedInterval.end() ) {
      end = aAllowedInterval.end();
    }
    Secint in = new Secint( start, end );
    IM5BunchEdit<IScene> initVals = new M5BunchEdit<>( scenesModel );
    SceneM5Model.INTERVAL.setFieldValue( initVals, in );
    SceneM5Model.NAME.setFieldValue( initVals, DEFAULT_NAME_AV );
    SceneM5Model.FRAME.setFieldValue( initVals, new Frame( episode().id(), IStridable.NONE_ID, start * FPS, false ) );
    IScene s = M5GuiUtils.askCreate( tsContext(), panel, initVals, cdi );
    if( s != null ) {
      treeViewer.refresh();
      treeViewer.setSelectedItem( s );
    }
  }

  private void editScene( IScene aScene ) {
    // determine allowed range
    IScene parent = aScene.parent(); // не бывает null, ведь не правим сам сюжет
    Secint allowedInterval = aScene.interval();
    int selIndex = parent.childScenes().keys().indexOf( aScene.interval() );
    Secint inBefore = parent.inBeforeChild( selIndex );
    if( inBefore != null ) {
      allowedInterval = Secint.union( allowedInterval, inBefore );
    }
    Secint inAfter = parent.inAfterChild( selIndex );
    if( inAfter != null ) {
      allowedInterval = Secint.union( allowedInterval, inAfter );
    }
    // invoke scene edit dialog
    IM5LifecycleManager<IScene> lm = scenesModel.getLifecycleManager( aScene.parent() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    M5EntityPanelWithValedsController<IScene> ctrl = new SceneEditPanelController( episode().id(), allowedInterval );
    IM5EntityPanel<IScene> panel = scenesModel.panelCreator().createEntityControlledPanel( ctx, lm, ctrl );
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext() );
    IScene s = M5GuiUtils.askEdit( tsContext(), panel, aScene, cdi );
    if( s != null ) {
      treeViewer.refresh();
      treeViewer.setSelectedItem( s );
    }
  }

  /**
   * Removes asked scene queries allowance before.
   *
   * @param aScene {@link IScene} - scene to remove or <code>null</code> to do nothing
   */
  void removeScene( IScene aScene ) {
    if( aScene == null ) {
      return;
    }
    IM5LifecycleManager<IScene> lm = scenesModel.getLifecycleManager( aScene.parent() );
    IScene toSel = aScene.parent().childScenes().values().next( aScene );
    if( M5GuiUtils.askRemove( tsContext(), scenesModel, aScene, getShell(), lm ) ) {
      doSetEpisode();
      if( toSel == null ) {
        toSel = aScene.parent().childScenes().values().last();
        if( toSel == null ) {
          toSel = aScene.parent();
        }
      }
      treeViewer.setSelectedItem( toSel );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractEpisodeUipart
  //

  @Override
  protected void doSetEpisode() {
    if( episode() == null ) {
      treeViewer.items().clear();
      return;
    }
    treeViewer.items().setAll( episode().story().allScenesBelow() );
    IM5Column<IScene> col = treeViewer.columnManager().columns().values().get( 0 );
    col.setWidth( 80 );
    col = treeViewer.columnManager().columns().values().get( 1 );
    col.pack();
    // TODO temporary, pack DUR_PERC_STR field
    col = treeViewer.columnManager().columns().values().get( 2 );
    col.pack();
  }

  @Override
  protected void doHandleEpisodeContentChange() {
    doSetEpisode();
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    return switch( aCode ) {
      case SWT.INSERT -> {
        handleAction( ACTID_ADD );
        yield true;
      }
      case SWT.DEL -> {
        handleAction( ACTID_REMOVE );
        yield true;
      }
      default -> false;
    };
  }

}
