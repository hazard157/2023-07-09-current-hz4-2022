package com.hazard157.psx24.core.glib.frlstviewer_ep;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.glib.frlstviewer_ep.EflvTreeModeManager.*;
import static com.hazard157.psx24.core.glib.frlstviewer_ep.IPsxResources.*;
import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;
import java.util.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.glib.dialogs.*;

/**
 * Реализация {@link IEpisodeFramesListViewer}.
 *
 * @author hazard157
 */
public class EpisodeFramesListViewer
    extends AbstractLazyPanel<TsComposite>
    implements IEpisodeFramesListViewer {

  /**
   * Last copy destination app pref ID in default prefs bundle.
   */
  public static final String APPREFID_LAST_DESTINATION = "FrameCopyLastDestination"; //$NON-NLS-1$

  /**
   * Раздел {@link IAppPreferences} с настройками класса.
   */
  public static final String PREFS_BUNDLE_ID = "EpisodeFramesListViewer"; //$NON-NLS-1$

  /**
   * Параметр {@link IAppPreferences}: размер значков в выпадающем пункте меню камер
   * {@link IPsxAppActions#AI_CAM_ID_MENU}.
   */
  public static final IDataDef CAM_MENU_THUMB_SIZE = DataDef.create( "CamMenuThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_CAM_MENU_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_CAM_MENU_THUMB_SIZE, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ96 ) //
  );

  /**
   * Параметр {@link IAppPreferences}: размер просматриваемого кадра в правой части панели выбора/просмотра кадров.
   */
  public static final IDataDef FRAME_VIEWER_THUMB_SIZE = DataDef.create( "FrameViewerThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_FRAME_VIEWER_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_FRAME_VIEWER_THUMB_SIZE, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ256 ) //
  );

  protected final ITsActionHandler toolbarListener = this::processAction;

  // ------------------------------------------------------------------------------------
  // Работа с группировками в виде дерева
  //

  // final ITsGuiContext tsContext;
  final IPsxFileSystem    fileSystem;
  final ITsImageManager   imageManager;
  final IUnitEpisodes     ue;
  final IUnitSourceVideos usv;
  final IPrefBundle       prefBundle;

  TsToolbar             toolbar;
  IM5TreeViewer<IFrame> framesTree;
  IPdwWidget            imageWidget;

  /**
   * Управление режимами дерева: описание всех режимов, переключение между ними и таблицей.
   * <p>
   * Для переключения режимов надо задать идентификатор режима в {@link ITreeModeManager#setCurrentMode(String)}.
   */
  final ITreeModeManager<IFrame> treeModeManager;

  /**
   * Критерии отбора кадров для показа, не бывает null.
   */
  FrameSelectionCriteria criteria = FrameSelectionCriteria.NONE;

  /**
   * Локальный критерии показа кадров толькло этой камеры или при null - всех кадров.
   * <p>
   * Используется в {@link #fillAvTable()} для локального отбора показываемых кадров.
   */
  String shownFramesCamId = null;

  /**
   * Создает панель.
   *
   * @param aContext {@link ITsGuiContext} - контекст просмотрщика
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public EpisodeFramesListViewer( ITsGuiContext aContext ) {
    super( aContext );
    TsNullArgumentRtException.checkNull( aContext );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    imageManager = tsContext().get( ITsImageManager.class );
    ue = tsContext().get( IUnitEpisodes.class );
    usv = tsContext().get( IUnitSourceVideos.class );
    IAppPreferences ap = tsContext().get( IAppPreferences.class );
    prefBundle = ap.getBundle( PREFS_BUNDLE_ID );
    treeModeManager = new EflvTreeModeManager( tsContext() ) {

      @Override
      protected void onCurrentModeIdChanged() {
        if( !isCurrentTreeMode() ) {
          framesTree.setTreeMaker( null );
          prefBundle.prefs().setStr( LAST_TREE_MODE_ID, EMPTY_STRING );
          return;
        }
        TreeModeInfo<IFrame> info = treeModeInfoes().findByKey( currModeId() );
        if( info == null ) {
          framesTree.setTreeMaker( null );
          prefBundle.prefs().setStr( LAST_TREE_MODE_ID, EMPTY_STRING );
          return;
        }
        framesTree.setTreeMaker( info.treeMaker() );
        prefBundle.prefs().setStr( LAST_TREE_MODE_ID, info.id() );
      }

    };
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void fillAvTable() {
    IFrame sel = framesTree.selectedItem();
    IEpisode e = ue.items().findByKey( criteria.svin().episodeId() );
    if( e == null ) {
      framesTree.items().clear();
      return;
    }
    IListEdit<IFrame> filtered = new ElemLinkedBundleList<>();
    boolean showSingles = toolbar.isActionChecked( AID_SHOW_SINGLE );
    EAnimationKind kind = EAnimationKind.getType( showSingles, criteria.animationKind().isAnimated() );
    FrameSelectionCriteria c = new FrameSelectionCriteria( criteria.svin(), kind, criteria.isOnlySecAligned() );
    for( IFrame f : fileSystem.listEpisodeFrames( c ) ) {
      if( shownFramesCamId == null || f.cameraId().equals( shownFramesCamId ) ) {
        filtered.add( f );
      }
    }
    framesTree.items().setAll( filtered );
    framesTree.setSelectedItem( sel );
    if( sel != null ) {
      framesTree.reveal( sel );
    }
  }

  void processAction( String aActionId ) {
    IFrame sel = framesTree.selectedItem();
    switch( aActionId ) {
      case AID_CREATE_GIF: {
        TsInternalErrorRtException.checkNull( sel );
        int sec = sel.secNo();
        if( fileSystem.createGifAnimation( sel.episodeId(), sel.cameraId(), sec ) ) {
          IFrame frame = new Frame( sel.episodeId(), sel.cameraId(), sec * FPS, true );
          fillAvTable();
          setSelectedItem( frame );
          File file = fileSystem.findFrameFile( frame );
          if( file == null ) {
            TsDialogUtils.error( getShell(), FMT_ERR_CANT_CREATE_GIF, frame.toString() );
          }
          refresh();
        }
        break;
      }
      // case AID_RECREATE_ALL_GIFS: {
      // // спросим подтверждение
      // IEpisodeBase e = ds.items().findByKey( criteria.svin().episodeId() );
      // String msg;
      // if( shownFramesCamId != null ) {
      // msg = String.format( ASK_RECREATE_SEL_CAM_GIFS, shownFramesCamId, e.id() );
      // }
      // else {
      // msg = String.format( ASK_RECREATE_ALL_CAM_GIFS, e.id() );
      // }
      // if( TsDialogUtils.askYesNoCancel( getShell(), msg ) != ETsDialogCode.YES ) {
      // break;
      // }
      // // сформируем список GIFов
      // IListEdit<IFrame> gifs = new ElemLinkedBundleList<>();
      // FrameSelectionCriteria c = new FrameSelectionCriteria( new Svin( e.id() ), EAnimationKind.ANIMATED, true );
      // for( IFrame f : fileSystem.listEpisodeFrames( c ) ) {
      // if( shownFramesCamId == null || f.cameraId().equals( shownFramesCamId ) ) {
      // gifs.add( f );
      // }
      // }
      // // запустим процесс пересоздания
      // fileSystem.recreateGifAnimations( gifs );
      // refresh();
      // break;
      // }
      case AID_DELETE_GIF: {
        TsInternalErrorRtException.checkNull( sel );
        if( TsDialogUtils.askYesNoCancel( getShell(), ASK_DELETE_FRAME, sel.toString() ) != ETsDialogCode.YES ) {
          return;
        }
        File file = fileSystem.findFrameFile( sel );
        if( file != null ) {
          if( file.exists() ) {
            file.delete();
          }
          IFrame frame = new Frame( sel.episodeId(), sel.cameraId(), sel.frameNo(), false );
          fillAvTable();
          setSelectedItem( frame );
        }
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        if( sel != null ) {
          // создаем список рядом стоящих файлов изображений
          IListEdit<IFrame> navFrames = new ElemArrayList<>( 100 );
          int startFrameNo = sel.frameNo() - 5 * FPS;
          int endFrameNo = sel.frameNo() + 5 * FPS;
          for( int fNo = startFrameNo; fNo <= endFrameNo; fNo++ ) {
            IFrame frame = new Frame( sel.episodeId(), sel.cameraId(), fNo, true );
            File file = fileSystem.findFrameFile( frame );
            if( file != null ) {
              navFrames.add( frame );
            }
            frame = new Frame( sel.episodeId(), sel.cameraId(), fNo, false );
            file = fileSystem.findFrameFile( frame );
            if( file != null ) {
              navFrames.add( frame );
            }
          }
          DialogShowFrameFiles.show( tsContext(), sel, navFrames );
        }
        break;
      }
      case AID_PLAY:
        playItem( (ITsNode)framesTree.console().selectedNode() );
        break;
      case AID_CAM_ID_MENU: {
        shownFramesCamId = null;
        fillAvTable();
        break;
      }
      case AID_SHOW_SINGLE: {
        fillAvTable();
        setSelectedItem( sel );
        break;
      }
      case AID_COPY_FRAME: {
        if( sel != null ) {
          try {
            String path = prefBundle.prefs().getStr( APPREFID_LAST_DESTINATION, EMPTY_STRING );
            File destDir = TsRcpDialogUtils.askDirOpen( getShell(), path );
            if( destDir != null ) {
              IPrisexService prisexService = tsContext().get( IPrisexService.class );
              prisexService.copyFrameImage( sel, destDir );
              path = destDir.getAbsolutePath();
              prefBundle.prefs().setStr( APPREFID_LAST_DESTINATION, path );
            }
          }
          catch( Exception ex ) {
            LoggerUtils.errorLogger().error( ex );
            TsDialogUtils.error( getShell(), ex );
          }

          // GOGA --- 2021-08-02 что-то не то, не разобрался, код выше взят DialogPsxShowFullSizedFrameImage
          // IPsxFileCopyMoveService fcs = tsContext().get( IPsxFileCopyMoveService.class );
          // File destDir = fcs.destinations().getCursor( IPsxFileCopyMoveService.CURSORID_LAST_DESTINATION ).pos();
          // if( destDir == null ) {
          // destDir = new File( EMPTY_STRING ).getAbsoluteFile();
          // }
          // tsContext().get( IPrisexService.class ).copyFrameImage( sel, destDir );
          // fcs.destinations().getCursor( IPsxFileCopyMoveService.CURSORID_LAST_DESTINATION ).setPos( destDir );
          // ---
          // File file = fileSystem.findFrameFile( sel );
          // if( file != null ) {
          // IPsxFileCopyMoveService fileCopyMoveService = appContext.get( IPsxFileCopyMoveService.class );
          // File destDir =
          // fileCopyMoveService.destinations().getCursor( IPsxFileCopyMoveService.CURSORID_LAST_DESTINATION ).pos();
          // fileCopyMoveService.copyImageFileToDestination( file, destDir, true, true );
          // }
        }
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

  protected void updateActionsState() {
    boolean isAlive = isAlive();
    IFrame sel = framesTree.selectedItem();
    boolean isSel = sel != null;
    boolean isAnim = sel != null && sel.isDefined() && sel.isAnimated();
    toolbar.setActionEnabled( AID_CREATE_GIF, isAlive && isSel );
    toolbar.setActionEnabled( AID_RECREATE_ALL_GIFS, isAlive
        // TODO uncomment in processAction()
        && false
    //
    );
    toolbar.setActionEnabled( AID_DELETE_GIF, isAlive && isSel && isAnim );
    toolbar.setActionEnabled( AID_TEST_GIF, isAlive && isSel );
    toolbar.setActionEnabled( AID_GIF_INFO, isAlive && isSel );
    toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL, isAlive && isSel );
    toolbar.setActionEnabled( AID_PLAY, isAlive && (framesTree.console().selectedNode() != null) );
    toolbar.setActionEnabled( AID_COPY_FRAME, isAlive && isSel );
    toolbar.setActionChecked( ACTID_VIEW_AS_LIST, !treeModeManager.isCurrentTreeMode() );
    toolbar.setActionChecked( ACTID_VIEW_AS_TREE, treeModeManager.isCurrentTreeMode() );
    toolbar.setActionEnabled( ACTID_GO_PREV, !framesTree.items().isEmpty() && sel != framesTree.items().first() );
    toolbar.setActionEnabled( ACTID_GO_NEXT, !framesTree.items().isEmpty() && sel != framesTree.items().last() );
    updateLocalCriteria();
  }

  /**
   * Обновляет контроли в соответствии с локальными критериями просмотра.
   * <p>
   * Кроме глобальных критериев {@link #getCriteria()} есть локальные критерий:
   * <ul>
   * <li>отображать ли в списке одиночные кадры - кнопка {@link IPsxAppActions#AI_SHOW_SINGLE};</li>
   * <li>отображать ли кадры конкретной камеры или всех камер эпизода - кнопка с выпадающим меню
   * {@link IPsxAppActions#AI_CAM_ID_MENU}.</li>
   * </ul>
   * В зависимости от глобальных критериев, локальные могут быть запрещены (например, если глобально показываются только
   * анимированные кадры, нкнопка показа одиночных запрещена).
   */
  void updateLocalCriteria() {
    boolean isAlive = isAlive();
    if( !isAlive ) {
      toolbar.setActionEnabled( AID_CAM_ID_MENU, false );
      return;
    }
    // выбор камер есть только для эпизодов с несколькими камерами и глобальном критерии !все камеры"
    IStringMap<ISourceVideo> svs = usv.episodeSourceVideos( criteria.svin().episodeId() );
    boolean isManyCamsGlobal = svs.size() > 1 && !criteria.svin().hasCam();
    toolbar.setActionEnabled( AID_CAM_ID_MENU, isManyCamsGlobal );
    // обновим выпадающий список камер
    IMenuCreator mc = new AbstractMenuCreator() {

      @Override
      protected boolean fillMenu( Menu aMenu ) {
        IEpisode e = ue.items().findByKey( criteria.svin().episodeId() );
        if( e == null || svs.isEmpty() ) {
          return false;
        }
        EThumbSize thumbSize = CAM_MENU_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj();
        // final IPsxImagesCache svFramesCache =
        // appContext.get( IPsxImagesSystem.class ).getCache( IPsxImagesSystem.IC_SMALL );
        for( final String camId : svs.keys() ) {
          MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
          mItem.setText( camId );
          ISourceVideo sv = svs.getByKey( camId );
          TsImage mi = fileSystem.findThumb( sv.frame(), thumbSize );
          if( mi != null ) {
            mItem.setImage( mi.image() );
          }
          mItem.addSelectionListener( new SelectionListenerAdapter() {

            @Override
            public void widgetSelected( SelectionEvent aEvent ) {
              shownFramesCamId = camId;
              IFrame sel = framesTree.selectedItem();
              fillAvTable();
              if( sel == null || !sel.cameraId().equals( shownFramesCamId ) ) {
                int frameNo;
                if( sel == null ) {
                  Secint in = criteria.svin().interval();
                  if( in == null ) {
                    in = new Secint( 0, e.duration() - 1 );
                  }
                  frameNo = (in.start() + in.duration() / 2) * FPS;
                }
                else {
                  frameNo = sel.frameNo();
                }
                sel = new Frame( e.id(), camId, frameNo, false );
              }
              framesTree.setSelectedItem( sel );
            }
          } );
        }
        return true;
      }
    };
    toolbar.setActionMenu( AID_CAM_ID_MENU, mc );
  }

  /**
   * Обновляет выпадающее меню кнопки вопроизведения на панели инструметнов.
   * <p>
   * Должен вызываться каждый раз, когда меняется текщий элемент в дереве просмотра.
   *
   * @param aSelectedItem {@link IFrame} - кадр, может быть <code>null</code>
   */
  void addPlayMenu( IFrame aSelectedItem ) {
    toolbar.setActionMenu( AID_PLAY,
        tsContext().get( IPlayMenuSupport.class ).getPlayMenuCreator( tsContext(), new IPlayMenuParamsProvider() {

          @Override
          public Svin playParams() {
            return getItemSvinOrNull( (ITsNode)framesTree.console().selectedNode() );
          }

          @Override
          public int spotlightSec() {
            if( aSelectedItem != null && aSelectedItem.isDefined() ) {
              return aSelectedItem.secNo();
            }
            return -1;
          }
        } ) );
  }

  Svin getItemSvinOrNull( ITsNode aNode ) {
    if( aNode == null ) {
      return null;
    }
    if( aNode.kind() == NK_FRAME ) {
      IFrame f = (IFrame)aNode.entity();
      return new Svin( f.episodeId(), f.cameraId(), getAroundFrame( f ), f );
    }
    if( aNode.kind() == NK_SCENE ) {
      IScene s = (IScene)aNode.entity();
      if( s != null ) {
        return new Svin( s.root().episodeId(), s.frame().cameraId(), s.interval(), s.frame() );
      }
      return criteria.svin();
    }
    if( aNode.kind() == NK_TIME ) {
      int sec = ((Integer)aNode.entity()).intValue();
      IFrame f = new Frame( criteria.svin().episodeId(), criteria.svin().cameraId(), sec, false );
      return new Svin( f.episodeId(), f.cameraId(), getAroundFrame( f ), f );
    }
    if( aNode.kind() == NK_NOTE ) {
      IEpisode e = ue.items().findByKey( criteria.svin().episodeId() );
      if( e != null ) {
        int index = e.noteLine().marksMap().values().indexOf( ((String)aNode.entity()) );
        if( index >= 0 ) {
          Secint in = e.noteLine().marksMap().keys().get( index );
          IScene s = e.story().findBestSceneFor( in, true );
          IFrame f;
          if( s != null ) {
            f = s.frame();
          }
          else {
            f = new Frame( criteria.svin().episodeId(), criteria.svin().cameraId(), in.start() + (in.duration() / 2),
                false );
          }
          return new Svin( f.episodeId(), f.cameraId(), in, f );
        }
      }
      return null;
    }
    if( aNode.kind().id().equals( M5DefaultTreeMaker.KIND_ID ) ) {
      if( aNode.entity() instanceof IFrame ) {
        IFrame f = (IFrame)aNode.entity();
        return new Svin( f.episodeId(), f.cameraId(), getAroundFrame( f ), f );
      }
      return null;
    }
    throw new TsNotAllEnumsUsedRtException();
  }

  private static Secint getAroundFrame( IFrame aFrame ) {
    int fsec = aFrame.frameNo() / FPS;
    if( fsec <= 1 ) {
      return new Secint( 0, 20 );
    }
    return new Secint( fsec - 1, fsec + 15 );
  }

  /**
   * Воспроизводит видео, соответствующий узлу.
   *
   * @param aNode {@link ITsNode} - выбранный узел, может быть <code>null</code>
   */
  void playItem( ITsNode aNode ) {
    if( aNode != null ) {
      Svin svin = getItemSvinOrNull( aNode );
      if( svin != null ) {
        tsContext().get( IPrisexService.class ).playEpisodeVideo( svin );
      }
    }
  }

  private boolean isAlive() {
    IEpisode e = ue.items().findByKey( criteria.svin().episodeId() );
    if( e != null ) {
      IStringMap<ISourceVideo> svs = usv.episodeSourceVideos( criteria.svin().episodeId() );
      return !svs.isEmpty();
    }
    return false;
  }

  EThumbSize determineThumbSize() {
    if( tsContext().params().hasValue( FRAME_VIEWER_THUMB_SIZE.id() ) ) {
      return FRAME_VIEWER_THUMB_SIZE.getValue( tsContext().params() ).asValobj();
    }
    return FRAME_VIEWER_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IFrame> aListener ) {
    framesTree.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IFrame> aListener ) {
    framesTree.removeTsSelectionListener( aListener );
  }

  @Override
  public IFrame selectedItem() {
    return framesTree.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    if( !Objects.equals( framesTree.selectedItem(), aItem ) ) {
      imageWidget.setTsImage( null );
      imageWidget.redraw();
    }
    framesTree.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<IFrame> aListener ) {
    framesTree.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<IFrame> aListener ) {
    framesTree.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  protected TsComposite doCreateControl( Composite aParent ) {
    TsComposite backplane = new TsComposite( aParent );
    backplane.setLayout( new BorderLayout() );
    // toolbar
    TsActionDef gifMgmtMenu = new TsActionDef( AI_CREATE_GIF );
    toolbar = TsToolbar.create( backplane, tsContext(), EIconSize.IS_16X16, //
        gifMgmtMenu, ACDEF_SEPARATOR, //
        AI_SHOW_SINGLE, AI_CAM_ID_MENU, ACDEF_SEPARATOR, //
        ACDEF_VIEW_AS_TREE_MENU, ACDEF_VIEW_AS_LIST, ACDEF_SEPARATOR, //
        ACDEF_COLLAPSE_ALL, ACDEF_EXPAND_ALL, //
        ACDEF_ZOOM_ORIGINAL_PUSHBUTTON, //
        AI_COPY_FRAME, //
        AI_PLAY_MENU, //
        ACDEF_GO_PREV, ACDEF_GO_NEXT //
    );

    toolbar.setNameLabelText( STR_N_TOOLBAR );

    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.setActionMenu( AID_CREATE_GIF, new AbstractGifManagemntDropDownMenuCreator( tsContext(), toolbarListener ) {

      @Override
      protected IFrame getFrame() {
        return framesTree.selectedItem();
      }
    } );
    toolbar.addListener( toolbarListener );
    toolbar.setActionMenu( ACTID_VIEW_AS_TREE, new TreeModeDropDownMenuCreator<>( tsContext(), treeModeManager ) );
    // m5Table
    IM5Model<IFrame> model = tsContext().get( IM5Domain.class ).getModel( MID_FRAME, IFrame.class );
    framesTree = new M5TreeViewer<>( tsContext(), model, false );
    framesTree.createControl( backplane );
    framesTree.getControl().setLayoutData( BorderLayout.CENTER );
    IM5Column<IFrame> col = framesTree.columnManager().add( FID_FRAME_NO );
    col.setWidth( 150 );
    framesTree.columnManager().add( FID_IS_ANIMATED );
    framesTree.columnManager().add( FID_CAMERA_ID );
    framesTree.setTreeMaker( null );
    fillAvTable();
    framesTree.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      addPlayMenu( aSelectedItem );
      imageWidget.setTsImage( null );
      Svin svin = getItemSvinOrNull( (ITsNode)framesTree.console().selectedNode() );
      if( svin != null ) {
        File file = fileSystem.findFrameFile( svin.frame() );
        if( file != null ) {
          ITsPoint prefSize = imageWidget.getAreaPreferredSize();
          EThumbSize thumbSize = EThumbSize.findIncluding( prefSize.x(), prefSize.y() );
          TsImage mi = imageManager.findThumb( file, thumbSize );
          imageWidget.setTsImage( mi );
          imageWidget.redraw();
        }
      }
      updateActionsState();
    } );
    // imageWidget
    imageWidget = new PdwWidgetSimple( tsContext() );
    imageWidget.createControl( backplane );
    imageWidget.getControl().setLayoutData( BorderLayout.EAST );
    EThumbSize thumbSize = determineThumbSize();
    imageWidget.setAreaPreferredSize( thumbSize.pointSize() );
    String lastTreeModeId = LAST_TREE_MODE_ID.getValue( prefBundle.prefs() ).asString();
    if( !treeModeManager.treeModeInfoes().hasKey( lastTreeModeId ) ) {
      lastTreeModeId = null;
    }
    treeModeManager.setCurrentMode( lastTreeModeId );
    framesTree.columnManager().columns().getByKey( FID_FRAME_NO ).adjustWidth( "--00:00.00--" ); //$NON-NLS-1$
    updateActionsState();
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // IEpisodeFramesListViewer
  //

  @Override
  public IList<IFrame> listShownFrames() {
    return framesTree.items();
  }

  // @Override
  // public IFrame positionOnSec( int aSec ) {
  // IList<IFrame> frames = framesTree.items();
  // if( frames.isEmpty() ) {
  // framesTree.setSelectedItem( null );
  // return null;
  // }
  // if( frames.size() == 1 ) {
  // framesTree.setSelectedItem( frames.get( 0 ) );
  // return frames.get( 0 );
  // }
  // IFrame f1 = null, f2 = null;
  // for( IFrame f : frames ) {
  // int fSec = f.frameNo() / FPS;
  // if( fSec == aSec ) {
  // framesTree.setSelectedItem( f );
  // return f;
  // }
  // if( fSec < aSec ) {
  // f1 = f;
  // }
  // else {
  // f2 = f;
  // break;
  // }
  // }
  // if( f1 == null ) {
  // framesTree.setSelectedItem( f2 );
  // return f2;
  // }
  // if( f2 == null ) {
  // framesTree.setSelectedItem( f1 );
  // return f1;
  // }
  // int d1 = aSec - f1.frameNo() / FPS;
  // int d2 = f2.frameNo() / FPS - aSec;
  // if( d1 <= d2 ) {
  // framesTree.setSelectedItem( f1 );
  // return f1;
  // }
  // framesTree.setSelectedItem( f2 );
  // return f2;
  // }

  // ------------------------------------------------------------------------------------
  // IEpisodeFramesListViewer
  //

  @Override
  public void refresh() {
    IFrame frame = selectedItem();
    fillAvTable();
    setSelectedItem( frame );
    updateActionsState();
  }

  @Override
  public FrameSelectionCriteria getCriteria() {
    return criteria;
  }

  @Override
  public void setCriteria( FrameSelectionCriteria aCriteria ) {
    criteria = TsNullArgumentRtException.checkNull( aCriteria );
    toolbar.setActionChecked( AID_SHOW_SINGLE, criteria.animationKind().isSingle() );
    IFrame sel = framesTree.selectedItem();
    imageWidget.setTsImage( null );
    updateLocalCriteria();
    fillAvTable();
    setSelectedItem( sel );
    updateActionsState();
  }

}
