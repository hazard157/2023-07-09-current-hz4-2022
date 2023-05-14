package com.hazard157.psx24.core.e4.uiparts.eps;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.e4.uiparts.eps.IPsxResources.*;
import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
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
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.core.glib.tagint.*;
import com.hazard157.psx24.core.m5.episode.*;

/**
 * Вью просмотра и редактирования сюжета {@link IEpisode#story()}.
 *
 * @author hazard157
 */
public class UipartEpisodeStory
    extends AbstractEpisodeUipart
    implements ITsKeyInputListener {

  @Inject
  ICurrentFramesListService currentFramesListService;

  @Inject
  IPsxSelectedSvinsService selectedSvinsService;

  // ------------------------------------------------------------------------------------
  // Работа с деревом
  //

  static final ITsNodeKind<IScene> NK_SCENE = new TsNodeKind<>( "Scene", IScene.class, true ); //$NON-NLS-1$

  /**
   * Группировщик сцен в сюжетное дерево.
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
   * Слушатель изменения выбранной сены в дереве.
   */
  private final ITsSelectionChangeListener<IScene> selectedSceneChangeListener = ( aSource, aSelectedItem ) -> {
    addPlayMenu( aSelectedItem );
    Svin svin = getItemSvinOrNull( aSelectedItem );
    if( svin != null ) {
      svin = Svin.removeCamId( svin );
    }
    updateTagMarksPanel();
    currentFramesListService.setCurrentAsSvin( svin );
    selectedSvinsService.setSvin( svin );
    updateActionsState();
  };

  /**
   * Слушатель двойного щелчка на сцене.
   */
  private final ITsDoubleClickListener<IScene> sceneDoubleClickListener =
      ( aSource, aSelectedItem ) -> processAction( ACTID_EDIT );

  /**
   * Слушатель двойного щелчка на zhksrt.
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
   * Отрабатывает "живое" изменение пометки ярлыками сцен сюжета.
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
  // Панель инструментов
  //

  private static final String AID_ADD_TAG_INTERVAL = PSX_ACT_ID + ".AddTagInterval"; //$NON-NLS-1$

  private static final ITsActionDef AI_ADD_TAG_INTERVAL = TsActionDef.ofPush2( AID_ADD_TAG_INTERVAL, //
      ACT_T_ADD_TAG_INTERVAL, ACT_P_ADD_TAG_INTERVAL, ICON_TAG );

  private final ITsActionHandler toolbarListener = this::processAction;

  // ------------------------------------------------------------------------------------
  // Создание вью
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
        AI_PLAY_MENU //
    );
    toolbar.setNameLabelText( STR_STORY_TOOLBAR_NAME );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
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
    frameWidget.setAreaPreferredSize( EThumbSize.SZ360.pointSize() );
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
  // Внутренные методы
  //

  /**
   * Обрабатывает нажатие казанной кнопки на панели управления.
   *
   * @param aItemId int идентификатор кнопки
   */
  void processAction( String aItemId ) {
    if( !toolbar.isActionEnabled( aItemId ) ) {
      return;
    }
    IScene sel = treeViewer.selectedItem();
    switch( aItemId ) {
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
      case AID_PLAY:
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

  /**
   * Обновляет состояние кнопок на панели управления.
   */
  void updateActionsState() {
    IScene sel = treeViewer.selectedItem();
    TsImage mi = null;
    if( sel != null ) {
      IPsxFileSystem fileSystem = tsContext().get( IPsxFileSystem.class );
      mi = fileSystem.findThumb( sel.frame(), thumbSize );
    }
    frameWidget.setTsImage( mi );
    frameWidget.redraw();

    boolean isSel = sel != null;
    boolean isAlive = episode() != null;
    toolbar.setActionEnabled( ACTID_ADD, isAlive );
    toolbar.setActionEnabled( ACTID_EDIT, isAlive && isSel );
    toolbar.setActionEnabled( ACTID_REMOVE, isAlive && isSel );
    toolbar.setActionEnabled( AID_PLAY, isAlive && isSel );
    toolbar.setActionEnabled( AID_ADD_TAG_INTERVAL, isAlive & isSel );
    tsContext().get( ITsE4Helper.class ).updateHandlersCanExecuteState();
  }

  /**
   * Обновляет состояние пометок текущей сцены (дерево ярлыков внизу вью).
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
   * Воспроизводит видео, соответствующий сцене.
   *
   * @param aItemOrNull {@link IScene} - сцена, может быть null
   */
  void playItem( IScene aItemOrNull ) {
    Svin svin = getItemSvinOrNull( aItemOrNull );
    if( svin != null ) {
      tsContext().get( IPrisexService.class ).playEpisodeVideo( svin );
    }
  }

  /**
   * Возвращает воспроизводимый интервал, соотвутетсвующий сцене.
   *
   * @param aItemOrNull {@link IScene} - сцена, может быть null
   * @return {@link Svin} - интервал вопрозведения или null
   */
  Svin getItemSvinOrNull( IScene aItemOrNull ) {
    if( aItemOrNull == null ) {
      return null;
    }
    String camId = aItemOrNull.frame().isDefined() ? aItemOrNull.frame().cameraId() : IStridable.NONE_ID;
    return new Svin( episode().id(), camId, aItemOrNull.interval() );
  }

  /**
   * Обновляет выпадающее меню кнопки вопроизведения на панели инструметнов.
   * <p>
   * Должен вызываться каждый раз, когда меняется текущая (выделенная) в дереве сцена.
   *
   * @param aSelectedItem {@link IScene} - текущая сцена, может быть null
   */
  void addPlayMenu( IScene aSelectedItem ) {
    toolbar.setActionMenu( AID_PLAY,
        tsContext().get( IPlayMenuSupport.class ).getPlayMenuCreator( tsContext(), new IPlayMenuParamsProvider() {

          @Override
          public Svin playParams() {
            return getItemSvinOrNull( aSelectedItem );
          }

          @Override
          public int spotlightSec() {
            if( aSelectedItem != null ) {
              IFrame f = aSelectedItem.frame();
              if( f.isDefined() ) {
                return f.secNo();
              }
            }
            return -1;
          }
        } ) );
  }

  /**
   * Меняет состояние пометок в теглайне эпизода.
   *
   * @param aTagIds {@link IStringList} - идентификаторы изменившихся ярлыков
   * @param aIn {@link Secint} - интервал изменения
   * @param aSet boolean - новое состояние пометки
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
   * Меняет состояние пометок в теглайне эпизода.
   *
   * @param aTagId String - идентификатор изменившегося ярлыка
   * @param aIn {@link Secint} - интервал изменения
   * @param aSet boolean - новое состояние пометки
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
  // Работа со сценами
  //

  /**
   * Добавляет сцену, предварительно запрсив, куда относительно текущей сцены ее разместить.
   */
  private void addScene() {
    if( episode() == null ) {
      return;
    }
    IStory story = episode().story();
    // если сюжет пустой, сразу перейдем к добалению сцены
    if( story.isEmpty() ) {
      doAddItem( story, story.interval() );
      return;
    }
    // в дереве должна быть выбрана сцена, относительно которой создается новая
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
    // если все возможности выбраны, то некуда добавлять сцену
    if( disabledPlaces.size() == ENodeAddPlace.values().length ) {
      TsDialogUtils.error( getShell(), MSG_ERR_NO_PLACES_TO_ADD_SCENE );
      return;
    }
    ENodeAddPlace defPlace = ENodeAddPlace.AFTER; // по умолчанию добавление идет по приоритетм AFTER, CHILD, BEFORE
    if( disabledPlaces.hasElem( defPlace ) ) {
      defPlace = ENodeAddPlace.CHILD;
    }
    if( disabledPlaces.hasElem( defPlace ) ) {
      defPlace = ENodeAddPlace.BEFORE;
    }
    // вызов диалога выбора места добавления новой сцены
    ITsDialogInfo cdi = new TsDialogInfo( tsContext(), DLG_C_ADD_SCENE_PLACE, DLG_T_ADD_SCENE_PLACE );
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
    // собственно вызов диалога свойств сцены и добавление сцены
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
    // определение допустимого интервала
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
    // вызов диалога
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
   * Удаляет указанную сцену, предварительно запросив разрешение.
   *
   * @param aScene {@link IScene} - удаляемая сцена, если <code>null</code>, то метод ничего не делает
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
  // Переопределение методов
  //

  @Override
  protected void doSetEpisode() {
    if( episode() == null ) {
      treeViewer.items().clear();
      return;
    }
    treeViewer.items().setAll( episode().story().allScenesBelow() );
    IM5Column<IScene> col = treeViewer.columnManager().columns().values().get( 0 );
    col.pack();
    col.setWidth( col.width() * 3 / 2 );
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
        processAction( ACTID_ADD );
        yield true;
      }
      case SWT.DEL -> {
        processAction( ACTID_REMOVE );
        yield true;
      }
      default -> false;
    };
  }

}
