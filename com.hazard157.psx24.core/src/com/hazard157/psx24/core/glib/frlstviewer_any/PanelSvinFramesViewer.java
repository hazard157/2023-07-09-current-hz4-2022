package com.hazard157.psx24.core.glib.frlstviewer_any;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.glib.frlstviewer_any.IPsxResources.*;
import static com.hazard157.psx24.core.utils.ftstep.FrameTimeSteppableDropDownMenuCreator.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.glib.pgviewer.*;
import com.hazard157.lib.core.glib.pgviewer.impl.*;
import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.common.utils.ftstep.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.glib.dialogs.imgs.*;
import com.hazard157.psx24.core.utils.ftstep.*;

/**
 * {@link IPanelSvinFramesViewer} implementation.
 *
 * @author hazard157
 */
public class PanelSvinFramesViewer
    extends AbstractLazyPanel<Control>
    implements IPanelSvinFramesViewer, ITsActionHandler {

  private final IPgvVisualsProvider<IFrame> visualsProvider = new IPgvVisualsProvider<>() {

    @Override
    public String getTooltip( IFrame aEntity ) {
      return aEntity.toString();
    }

    @Override
    public TsImage getThumb( IFrame aEntity, EThumbSize aThumbSize ) {
      return fileSystem.findThumb( aEntity, aThumbSize );
    }

    @Override
    public String getLabel2( IFrame aEntity ) {
      return EpisodeUtils.ymdFromId( aEntity.episodeId() );
    }

    @Override
    public String getLabel1( IFrame aEntity ) {
      return HhMmSsFfUtils.mmssff( aEntity.frameNo() );
    }

  };

  private final ITsCollectionChangeListener appSettingsChangeListener = ( aSource, aOp, aItem ) -> {
    if( !this.isSelfParamChanging ) {
      String opId = (String)aItem;
      if( opId == null || opId.equals( APPRM_THUMBSZ_FRAMES_IN_PANELS.id() ) ) {
        setThumbSize( readThumbSizeFromAppSettings() );
      }
    }
  };

  private static final String STORY_STRING_IN_KDENLIVE_FILE_NAME = "story"; //$NON-NLS-1$

  private static final ESecondsStep DEFAULT_FRAME_STEP = ESecondsStep.SEC_10;
  private static final EThumbSize   MAX_THUMB_SIZE     = EThumbSize.SZ512;
  private static final EThumbSize   MIN_THUMB_SIZE     = EThumbSize.SZ64;

  private static final IMenuCreator NO_CAMS_MENU_CREATOR = new AbstractMenuCreator() {

    @Override
    protected boolean fillMenu( Menu aMenu ) {
      return false;
    }
  };

  private final GenericChangeEventer thumbSizeEventer;

  private final TsDoubleClickEventHelper<IFrame>     doubleClickEventHelper;
  private final TsSelectionChangeEventHelper<IFrame> selectionChangeEventHelper;

  private final IPsxFileSystem fileSystem;

  private final IListBasicEdit<Svin> svins          = new SortedElemLinkedBundleList<>();
  private final IStringListEdit      shownCameraIds = new StringArrayList();             // empty -> show all
  private final IStringListEdit      allCameraIds   = new StringArrayList();
  private final IPrefBundle          prefBundle;

  /**
   * Все кадры (секундные jpg и все gif) всех эпизодов, которые есть в {@link #svins}.
   * <p>
   * Это карта "Svin" - "список кадров Svin-а". Для каждого {@link Svin} из {@link #svins} в карте есть значение,
   * который может быть пустым списком.
   * <p>
   * Обновляется в методе {@link #internalUpdate_onSvinsListChange()}.
   */
  private IMapEdit<Svin, IList<IFrame>> mapFramesBySvins = new ElemMap<>();

  /**
   * IDs of episodes having at least one {@link Svin} in {@link #mapFramesBySvins}.
   * <p>
   * Uodates as {@link #mapFramesBySvins} in {@link #internalUpdate_onSvinsListChange()}.
   */
  private IStringListBasicEdit mentionedEpisodeIds = new SortedStringLinkedBundleList();

  /**
   * Frames to be displayed in viewed.
   */
  private IListBasicEdit<IFrame> shownFramesList =
      new SortedElemLinkedBundleList<>( getListInitialCapacity( estimateOrder( 1_000 ) ), true );

  private ESecondsStep   shownFrameTimeStep = DEFAULT_FRAME_STEP;
  private EAnimationKind shownAnimationKind = EAnimationKind.ANIMATED;
  private boolean        showOnlySvinCams   = false;

  private TsComposite             board   = null;
  private TsToolbar               toolbar = null;
  private IPicsGridViewer<IFrame> viewer;

  boolean isSelfParamChanging = false;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelSvinFramesViewer( ITsGuiContext aContext ) {
    super( aContext );
    thumbSizeEventer = new GenericChangeEventer( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this ) {

      @Override
      public void onTsDoubleClick( Object aSource, IFrame aSelectedItem ) {
        super.onTsDoubleClick( aSource, aSelectedItem );
        playFrame( aSelectedItem );
      }
    };
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this ) {

      public void onTsSelectionChanged( Object aSource, IFrame aSelectedItem ) {
        super.onTsSelectionChanged( aSource, aSelectedItem );
        refreshCamMenu( aSelectedItem );
        refreshPlayMenu( aSelectedItem );
        updateActionsState();
      }
    };
    fileSystem = tsContext().get( IPsxFileSystem.class );
    prefBundle = prefBundle( PSX_COMMON_APREF_BUNDLE_ID );
    prefBundle.prefs().addCollectionChangeListener( appSettingsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsE4StdEventsProducerLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // toolbar
    toolbar = TsToolbar.create( board, tsContext(), EIconSize.IS_24X24, //
        AI_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL_MENU, // Frame time step meny (density)
        AI_THUMB_SIZEABLE_ZOOM_MENU, // Thumb size menu (zoom)
        AI_SHOW_AK_BOTH, AI_SHOW_AK_ANIMATED, AI_SHOW_AK_SINGLE, ACDEF_SEPARATOR, // Animation
        AI_CAM_ID_MENU, ACDEF_SEPARATOR, // Camera
        AI_PLAY_MENU, AI_WORK_WITH_FRAMES, // Play menu
        ACDEF_SEPARATOR, ACDEF_ONE_BY_ONE, //
        ACDEF_SEPARATOR, AI_RUN_KDENLIVE //
    );
    // thumb size menu
    ThumbSizeableDropDownMenuCreator tsDdmc = new ThumbSizeableDropDownMenuCreator( this, tsContext(), IS_16X16,
        PSX_MIN_FRAME_THUMB_SIZE, PSX_MAX_FRAME_THUMB_SIZE );
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, tsDdmc );
    // frame time step menu
    FrameTimeSteppableDropDownMenuCreator ftsDdmc = new FrameTimeSteppableDropDownMenuCreator( this, tsContext() );
    toolbar.setActionMenu( AID_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL, ftsDdmc );
    // toolbar other staff
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( this );
    // viewer
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IPicsGridViewerConstants.OP_IS_LABEL2_SHOWN.setValue( ctx.params(), AV_TRUE );
    viewer = new PicsGridViewer<>( board, ctx );
    viewer.getControl().setLayoutData( BorderLayout.CENTER );
    viewer.setVisualsProvider( visualsProvider );
    viewer.setThumbSize( readThumbSizeFromAppSettings() );
    viewer.addTsSelectionListener( selectionChangeEventHelper );
    viewer.addTsDoubleClickListener( doubleClickEventHelper );
    return board;
  }

  @Override
  public IFrame selectedItem() {
    return viewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    viewer.setSelectedItem( aItem );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Implementations
  //

  /**
   * Обновляет внутренности при задании нового списка {@link #svins}.
   */
  private void internalUpdate_onSvinsListChange() {
    // mapFramesBySvins
    mapFramesBySvins.clear();
    for( Svin s : svins ) {
      FrameSelectionCriteria fsc = new FrameSelectionCriteria( s, EAnimationKind.BOTH, ESecondsStep.SEC_01 );
      mapFramesBySvins.put( s, fileSystem.listEpisodeFrames( fsc ) );
    }
    // mentionedEpisodeIds
    mentionedEpisodeIds.clear();
    for( Svin s : svins ) {
      if( !mentionedEpisodeIds.hasElem( s.episodeId() ) ) {
        mentionedEpisodeIds.add( s.episodeId() );
      }
    }
    // allCameraIds
    allCameraIds.clear();
    IStringListEdit doneEpisodeIds = new StringArrayList();
    IUnitSourceVideos usv = tsContext().get( IUnitSourceVideos.class );
    for( Svin s : svins ) {
      String epId = s.episodeId();
      if( !doneEpisodeIds.hasElem( epId ) ) {
        doneEpisodeIds.add( epId );
        IStringMap<ISourceVideo> svs = usv.episodeSourceVideos( epId );
        for( String camId : svs.keys() ) {
          if( !allCameraIds.hasElem( camId ) ) {
            allCameraIds.add( camId );
          }
        }
      }
    }
  }

  private boolean isFrameOfKnownSvinAccepted( IFrame aFrame, Svin aSvin ) {
    if( !shownAnimationKind.accept( aFrame.isAnimated() ) ) {
      return false;
    }
    if( !aFrame.isAnimated() && (aFrame.secNo() % shownFrameTimeStep.stepSecs()) != 0 ) {
      return false;
    }
    if( showOnlySvinCams ) {
      if( aSvin.hasCam() ) {
        return aSvin.cameraId().equals( aFrame.cameraId() );
      }
    }
    return shownCameraIds.isEmpty() || shownCameraIds.hasElem( aFrame.cameraId() );
  }

  /**
   * Обновялет список отображаемоых кадров {@link #shownFramesList}r при изменении параметров отображения.
   * <p>
   * На список отображаемых кадров влияют следующие параметры:
   * <ul>
   * <li>{@link #svins} - этот метод всегда следует вызывать после {@link #internalUpdate_onSvinsListChange()};</li>
   * <li>{@link #shownAnimationKind};</li>
   * <li>{@link #shownCameraIds};</li>
   * <li>{@link #shownFrameTimeStep};</li>
   * <li>состояние кнопки {@link IPsxAppActions#ACDEF_ONE_BY_ONE}.</li>
   * </ul>
   */
  private void internalUpdate_shownFramesList() {
    // shownFramesList - заполним кадрами, удовлетворяющими параметрам отображения
    shownFramesList.clear();
    IListEdit<Svin> undoneSvins = new ElemLinkedBundleList<>( svins );

    // show one frame per svin, prefer GIF rather than still image
    if( toolbar.isActionChecked( ACTID_ONE_BY_ONE ) ) {
      for( Svin s : mapFramesBySvins.keys() ) {
        IListEdit<IFrame> gifsOfSvin = new ElemArrayList<>();
        for( IFrame f : mapFramesBySvins.getByKey( s ) ) {
          if( f.isAnimated() ) {
            gifsOfSvin.add( f );
          }
        }
        if( !gifsOfSvin.isEmpty() ) {
          shownFramesList.add( gifsOfSvin.get( gifsOfSvin.size() / 2 ) );
          undoneSvins.remove( s );
        }
      }
    }
    // all accepted frames of Svin
    else {
      for( Svin s : mapFramesBySvins.keys() ) {
        for( IFrame f : mapFramesBySvins.getByKey( s ) ) {
          if( isFrameOfKnownSvinAccepted( f, s ) ) {
            shownFramesList.add( f );
            undoneSvins.remove( s );
          }
        }
      }
    }
    // добавим начальные кадры непоказанных Svin-ов
    for( Svin s : undoneSvins ) {
      IList<IFrame> svinFrames = mapFramesBySvins.getByKey( s );
      IStringListEdit usedCamIds = new StringArrayList(); // камеры, которые внесены в svinFrames
      for( IFrame f : svinFrames ) {
        if( !f.isAnimated() ) {
          if( usedCamIds.hasElem( s.cameraId() ) ) {
            break;
          }
          usedCamIds.add( s.cameraId() );
          shownFramesList.add( f );
        }
      }
    }
  }

  /**
   * Обновляет выпадающее меню выбора камер.
   * <p>
   * Показывает список камер эпизода, если эпизод один или список камер эпизода кадра, когда выбран любой кадр.
   *
   * @param aFrame {@link IFrame} - текущый выбранны в {@link #viewer} кадр, может быть <code>null</code>
   */
  private void refreshCamMenu( IFrame aFrame ) {
    IUnitEpisodes ue = tsContext().get( IUnitEpisodes.class );
    final IFrame camsFrame;
    if( aFrame == null || !aFrame.isDefined() ) {
      if( mentionedEpisodeIds.size() == 1 ) {
        IEpisode e = ue.items().getByKey( mentionedEpisodeIds.first() );
        camsFrame = e.frame();
      }
      else {
        camsFrame = shownFramesList.first();
        if( camsFrame == null ) {
          toolbar.setActionMenu( AID_CAM_ID_MENU, NO_CAMS_MENU_CREATOR );
          return;
        }
      }
    }
    else {
      camsFrame = aFrame;
    }
    IUnitSourceVideos usv = tsContext().get( IUnitSourceVideos.class );
    IStringMap<ISourceVideo> svs = usv.episodeSourceVideos( camsFrame.episodeId() );
    IMenuCreator mc = new AbstractMenuCreator() {

      @Override
      protected boolean fillMenu( Menu aMenu ) {
        IEpisode e = ue.items().findByKey( camsFrame.episodeId() );
        if( e == null || svs.isEmpty() ) {
          return false;
        }
        EThumbSize thumbSize = readThumbSizeFromAppSettings();
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
              setShownCameraIds( new SingleStringList( camId ) );
            }
          } );
        }
        return true;
      }
    };
    toolbar.setActionMenu( AID_CAM_ID_MENU, mc );
  }

  private static Svin getFrameSvin( IFrame aFrame ) {
    // вернем кадр -5 + 30 сек интервал
    int startSec = aFrame.frameNo() / FPS - 5;
    if( startSec < 0 ) {
      startSec = 0;
    }
    Secint in = new Secint( startSec, startSec + 30 );
    return new Svin( aFrame.episodeId(), aFrame.cameraId(), in );
  }

  void refreshPlayMenu( IFrame aFrame ) {
    if( aFrame == null ) {
      return;
    }
    IPlayMenuSupport pms = tsContext().get( IPlayMenuSupport.class );
    IMenuCreator menuCreator = pms.getPlayMenuCreator( tsContext(), new IPlayMenuParamsProvider() {

      @Override
      public Svin playParams() {
        return getFrameSvin( aFrame );
      }

      @Override
      public int spotlightSec() {
        if( aFrame.isDefined() ) {
          return aFrame.secNo();
        }
        return -1;
      }

    } );
    toolbar.setActionMenu( AID_PLAY, menuCreator );
  }

  void playFrame( IFrame aFrame ) {
    if( aFrame != null && aFrame.isDefined() ) {
      IPrisexService prisexService = tsContext().get( IPrisexService.class );
      prisexService.playEpisodeVideo( getFrameSvin( aFrame ) );
    }
  }

  /**
   * Обновляет отображение в {@link #viewer}, обычно надо вызвать после {@link #internalUpdate_shownFramesList()}.
   */
  void refreshViewer() {
    IFrame sel = selectedItem();
    viewer.setItems( shownFramesList );
    viewer.setSelectedItem( sel );
    refreshCamMenu( sel );
    updateActionsState();
  }

  EThumbSize readThumbSizeFromAppSettings() {
    return APPRM_THUMBSZ_FRAMES_IN_PANELS.getValue( prefBundle.prefs() ).asValobj();
  }

  void saveThumbSizeToAppSettings() {
    isSelfParamChanging = true;
    prefBundle.prefs().setValobj( APPRM_THUMBSZ_FRAMES_IN_PANELS, viewer.thumbSize() );
    isSelfParamChanging = false;
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aItemId ) {
    switch( aItemId ) {
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        setThumbSize( viewer.defaultThumbSize() );
        break;
      }
      case AID_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL: {
        setFrameTimeStep( DEFAULT_FRAME_STEP );
        break;
      }
      case AID_SHOW_AK_BOTH: {
        setShownAnimationKind( EAnimationKind.BOTH );
        break;
      }
      case AID_SHOW_AK_ANIMATED: {
        setShownAnimationKind( EAnimationKind.ANIMATED );
        break;
      }
      case AID_SHOW_AK_SINGLE: {
        setShownAnimationKind( EAnimationKind.SINGLE );
        break;
      }
      case AID_CAM_ID_MENU: {
        setShownCameraIds( allCameraIds );
        break;
      }
      case AID_PLAY: {
        playFrame( viewer.selectedItem() );
        break;
      }
      case AID_WORK_WITH_FRAMES: {
        IFrame frame = viewer.selectedItem();
        if( frame != null ) {
          IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
          IEpisode e = unitEpisodes.items().findByKey( frame.episodeId() );
          if( e != null ) {
            DialogWorkWithFrames.openEpisode( tsContext(), frame, e );
          }
        }
        break;
      }
      case AID_RUN_KDENLIVE: {
        IFrame sel = viewer.selectedItem();
        if( sel != null ) {
          IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
          IEpisode e = unitEpisodes.items().findByKey( sel.episodeId() );
          if( e != null ) {
            IList<File> allProjFiles = fileSystem.listEpisodeKdenliveProjects( e.id() );
            IListEdit<File> storyProjFiles = new ElemArrayList<>();
            for( File f : allProjFiles ) {
              if( f.getName().contains( STORY_STRING_IN_KDENLIVE_FILE_NAME ) ) {
                storyProjFiles.add( f );
              }
            }
            if( storyProjFiles.isEmpty() ) {
              TsDialogUtils.warn( getShell(), FMT_ERR_NO_STORY_KDENLIVE_PROJ, e.id() );
              break;
            }
            File kproj;
            if( storyProjFiles.size() > 1 ) {
              ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext() );
              kproj = DialogItemsList.select( cdi, storyProjFiles, null, ITsNameProvider.DEFAULT );
            }
            else {
              kproj = storyProjFiles.first();
            }
            if( kproj != null ) {
              TsMiscUtils.runProgram( PROGRAM_KDENLIVE, kproj.getAbsolutePath() );
            }
          }
        }
        break;
      }
      case ACTID_ONE_BY_ONE: {
        internalUpdate_shownFramesList();
        refreshViewer();
        break;
      }
      default: {
        TsDialogUtils.underDevelopment( getShell() );
        break;
      }
    }
    updateActionsState();
  }

  void updateActionsState() {
    IFrame sel = viewer.selectedItem();
    toolbar.setActionChecked( AID_SHOW_AK_BOTH, shownAnimationKind == EAnimationKind.BOTH );
    toolbar.setActionChecked( AID_SHOW_AK_ANIMATED, shownAnimationKind == EAnimationKind.ANIMATED );
    toolbar.setActionChecked( AID_SHOW_AK_SINGLE, shownAnimationKind == EAnimationKind.SINGLE );
    toolbar.setActionEnabled( ACTID_ZOOM_IN, thumbSize() != MAX_THUMB_SIZE );
    toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL, thumbSize() != viewer.defaultThumbSize() );
    toolbar.setActionChecked( ACTID_ZOOM_ORIGINAL, thumbSize() == viewer.defaultThumbSize() );
    toolbar.setActionEnabled( ACTID_ZOOM_OUT, thumbSize() != MIN_THUMB_SIZE );
    toolbar.setActionEnabled( AID_RUN_KDENLIVE, sel != null );
    toolbar.setActionEnabled( AID_CAM_ID_MENU, sel != null || mentionedEpisodeIds.size() == 1 );
    toolbar.setActionEnabled( AID_PLAY, sel != null );
    toolbar.setActionEnabled( AID_WORK_WITH_FRAMES, sel != null );
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeable
  //

  @Override
  public EThumbSize thumbSize() {
    return viewer.thumbSize();
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize() != aThumbSize ) {
      viewer.setThumbSize( aThumbSize );
      saveThumbSizeToAppSettings();
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return viewer.defaultThumbSize();
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IFrameTimeSteppable
  //

  @Override
  public ESecondsStep getFrameTimeStep() {
    return shownFrameTimeStep;
  }

  @Override
  public void setFrameTimeStep( ESecondsStep aStep ) {
    TsNullArgumentRtException.checkNull( aStep );
    if( shownFrameTimeStep != aStep ) {
      shownFrameTimeStep = aStep;
      internalUpdate_shownFramesList();
      refreshViewer();
    }
  }

  @Override
  public ESecondsStep defaultFrameTimeStep() {
    return DEFAULT_FRAME_STEP;
  }

  // ------------------------------------------------------------------------------------
  // IPanelSvinFramesViewer
  //

  @Override
  public IList<Svin> getSvins() {
    return svins;
  }

  @Override
  public void setSvin( Svin aSvin ) {
    setSvins( new SingleItemList<>( aSvin ) );
  }

  @Override
  public void setSvins( IList<Svin> aSvins ) {
    TsNullArgumentRtException.checkNull( aSvins );
    svins.setAll( aSvins );
    internalUpdate_onSvinsListChange();
    internalUpdate_shownFramesList();
    // удалим несуществующие камеры
    for( String camId : new StringArrayList( shownCameraIds ) ) {
      if( !allCameraIds.hasElem( camId ) ) {
        shownCameraIds.remove( camId );
      }
    }
    refreshViewer();
  }

  @Override
  public EAnimationKind getShownAnimationKind() {
    return shownAnimationKind;
  }

  @Override
  public void setShownAnimationKind( EAnimationKind aAnimationKind ) {
    TsNullArgumentRtException.checkNull( aAnimationKind );
    if( shownAnimationKind != aAnimationKind ) {
      shownAnimationKind = aAnimationKind;
      internalUpdate_shownFramesList();
      refreshViewer();
    }
  }

  @Override
  public IStringList getShownCameraIds() {
    return shownCameraIds;
  }

  @Override
  public void setShownCameraIds( IStringList aCameraIds ) {
    TsNullArgumentRtException.checkNull( aCameraIds );
    if( !shownCameraIds.equals( aCameraIds ) ) {
      shownCameraIds.setAll( aCameraIds );
      internalUpdate_shownFramesList();
      refreshViewer();
    }
  }

  @Override
  public boolean isOnlySvinCamsShown() {
    return showOnlySvinCams;
  }

  @Override
  public void setOnlySvinCamsShown( boolean aShowOnlySvinCams ) {
    if( showOnlySvinCams != aShowOnlySvinCams ) {
      showOnlySvinCams = aShowOnlySvinCams;
      internalUpdate_shownFramesList();
      refreshViewer();
    }
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IFrame> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IFrame> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<IFrame> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<IFrame> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

}
