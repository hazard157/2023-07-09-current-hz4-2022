package com.hazard157.psx24.core.glib.frlstviewer_any;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.glib.dialogs.imgs.*;
import com.hazard157.psx24.core.glib.plv.*;

/**
 * Панель просмотра произвольного списка кадров.
 *
 * @author hazard157
 */
public class PanelFramesListViewer
    extends TsPanel {

  private final ITsCollectionChangeListener appSettingsChangeListener = ( aSource, aOp, aItem ) -> {
    if( !this.isSelfParamChanging ) {
      String opId = (String)aItem;
      if( opId == null || opId.equals( APPRM_THUMBSZ_FRAMES_IN_PANELS.id() ) ) {
        changeThumbSize( readThumbSizeFromAppSettings() );
      }
    }
  };

  /**
   * Слушатель двоного щелчка на изображения - запускает воспроизведение видео.
   */
  ITsDoubleClickListener<PlvItem> viewerDoubleClickListener = ( aSource, aSelectedItem ) -> playItem( aSelectedItem );

  /**
   * Слушатель текущей иллюстрации.
   */
  ITsSelectionChangeListener<PlvItem> viewerSelectionChangeListener = ( aSource, aSelectedItem ) -> {
    addPlayMenu( aSelectedItem );
    updateActionsState();
  };

  private final ITsActionHandler toolbarListener = this::processAction;

  /**
   * Обработки факта загрузки изображения.
   */
  final IFrameLoadedCallback callback = new IFrameLoadedCallback() {

    @Override
    public void imageLoaded( IFrame aFrame, TsImage aImage ) {
      if( aImage == null ) {
        return;
      }
      String l1, l2;
      l1 = HhMmSsFfUtils.mmssff( aFrame.frameNo() );
      l2 = aFrame.episodeId();
      PlvItem item = new PlvItem( aImage, l1, l2, aFrame );
      plViewer.addItem( item );
      updateActionsState();
    }
  };

  private final DisposeListener disposeListener = aE -> stopLoaderThread();

  private final IPlvTooltipProvider plvTooltipProvider = ( aItem, aEvent, aTpParent ) -> {
    CLabel label = new CLabel( aTpParent, SWT.SHADOW_NONE );
    Color bk = aEvent.widget.getDisplay().getSystemColor( SWT.COLOR_INFO_BACKGROUND );
    label.setBackground( bk );
    Color fg = aEvent.widget.getDisplay().getSystemColor( SWT.COLOR_INFO_FOREGROUND );
    label.setForeground( fg );
    return label;
  };

  final TsToolbar          toolbar;
  final PicturesListViewer plViewer;
  final IPrisexService     prisexService;
  final IPsxFileSystem     fileSystem;
  final IPrefBundle        prefBundle;

  final IListEdit<IFrame> framesList = new ElemLinkedBundleList<>();

  boolean            isSelfParamChanging = false;
  IFrameLoaderThread frameLoaderThread   = null;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelFramesListViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    prisexService = tsContext().get( IPrisexService.class );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    prefBundle = tsContext().get( IPrefBundle.class );
    prefBundle.prefs().addCollectionChangeListener( appSettingsChangeListener );
    // toolbar
    toolbar = TsToolbar.create( this, tsContext(), EIconSize.IS_16X16, //
        ACDEF_ZOOM_OUT, ACDEF_ZOOM_ORIGINAL, ACDEF_ZOOM_IN, ACDEF_SEPARATOR, //
        AI_PLAY_MENU, ACDEF_SEPARATOR, //
        AI_WORK_WITH_FRAMES //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // pictureListViewer
    plViewer = new PicturesListViewer( this, EPlvLayoutMode.ROWS );
    plViewer.getControl().setLayoutData( BorderLayout.CENTER );
    plViewer.setAutoSelectNewItem( false );
    plViewer.setThumbSize( readThumbSizeFromAppSettings() );
    plViewer.addTsDoubleClickListener( viewerDoubleClickListener );
    plViewer.addTsSelectionListener( viewerSelectionChangeListener );
    plViewer.setTooltipProvider( plvTooltipProvider );
    this.addDisposeListener( disposeListener );
    updateActionsState();
  }

  void processAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    switch( aActionId ) {
      case ACTID_ZOOM_OUT: {
        changeThumbSize( plViewer.thumbSize().prevSize() );
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        changeThumbSize( APPRM_THUMBSZ_FRAMES_IN_PANELS.defaultValue().asValobj() );
        break;
      }
      case ACTID_ZOOM_IN: {
        changeThumbSize( plViewer.thumbSize().nextSize() );
        break;
      }
      case AID_PLAY: {
        playItem( plViewer.selectedItem() );
        break;
      }
      case AID_WORK_WITH_FRAMES: {
        IFrame frame = frameFromItem( plViewer.selectedItem() );
        if( frame != null ) {
          IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
          IEpisode e = unitEpisodes.items().findByKey( frame.episodeId() );
          if( e != null ) {
            DialogWorkWithFrames.openEpisode( tsContext(), frame, e );
          }
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  void updateActionsState() {
    toolbar.setActionEnabled( ACTID_ZOOM_OUT, plViewer.thumbSize() != EThumbSize.minSize() );
    toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL,
        plViewer.thumbSize() != APPRM_THUMBSZ_FRAMES_IN_PANELS.defaultValue().asValobj() );
    toolbar.setActionEnabled( ACTID_ZOOM_IN, plViewer.thumbSize() != EThumbSize.maxSize() );
    boolean isFrame = frameFromItem( plViewer.selectedItem() ) != null;
    toolbar.setActionEnabled( AID_PLAY, isFrame );
    toolbar.setActionEnabled( AID_WORK_WITH_FRAMES, isFrame );
  }

  IFrame frameFromItem( PlvItem aItem ) {
    if( aItem != null ) {
      IFrame imgdef = (IFrame)aItem.userData();
      if( imgdef.isDefined() ) {
        return imgdef;
      }
    }
    return null;
  }

  void reloadFramesList() {
    stopLoaderThread();
    plViewer.clearItems();
    plViewer.setThumbSize( readThumbSizeFromAppSettings() );
    if( !framesList.isEmpty() ) {
      frameLoaderThread = fileSystem.startThumbsLoading( framesList, plViewer.thumbSize(), callback, false );
    }
    updateActionsState();
  }

  void changeThumbSize( EThumbSize aNewSize ) {
    plViewer.clearItems();
    plViewer.setThumbSize( aNewSize );
    saveThumbSizeToAppSettings();
    reloadFramesList();
  }

  EThumbSize readThumbSizeFromAppSettings() {
    return APPRM_THUMBSZ_FRAMES_IN_PANELS.getValue( prefBundle.prefs() ).asValobj();
  }

  void saveThumbSizeToAppSettings() {
    isSelfParamChanging = true;
    prefBundle.prefs().setValobj( APPRM_THUMBSZ_FRAMES_IN_PANELS, plViewer.thumbSize() );
    isSelfParamChanging = false;
  }

  void playItem( PlvItem aItem ) {
    IFrame frame = frameFromItem( aItem );
    if( frame != null ) {
      prisexService.playEpisodeVideo( getFrameSvin( frame ) );
    }
  }

  Svin getFrameSvin( IFrame aFrame ) {
    // вернем кадр -5 + 30 сек интервал
    int startSec = aFrame.frameNo() / FPS - 5;
    if( startSec < 0 ) {
      startSec = 0;
    }
    Secint in = new Secint( startSec, startSec + 30 );
    return new Svin( aFrame.episodeId(), aFrame.cameraId(), in );
  }

  void addPlayMenu( PlvItem aSelectedItem ) {
    IFrame frame = frameFromItem( aSelectedItem );
    if( frame == null ) {
      return;
    }
    IPlayMenuSupport pms = tsContext().get( IPlayMenuSupport.class );
    IMenuCreator menuCreator = pms.getPlayMenuCreator( tsContext(), new IPlayMenuParamsProvider() {

      @Override
      public Svin playParams() {
        return getFrameSvin( frame );
      }

      @Override
      public int spotlightSec() {
        if( frame.isDefined() ) {
          return frame.secNo();
        }
        return -1;
      }

    } );
    toolbar.setActionMenu( AID_PLAY, menuCreator );
  }

  void stopLoaderThread() {
    if( frameLoaderThread != null ) {
      frameLoaderThread.close();
      frameLoaderThread = null;
      try {
        Thread.sleep( 200 );
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает список отображаемых кадров.
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - список отображаемых кадров, не бывает <code>null</code>
   */
  public IList<IFrame> getFramesList() {
    return framesList;
  }

  /**
   * Запускает процесс отображения списка кадров.
   *
   * @param aFrames {@link IList}&lt;{@link IFrame}&gt; - список отображаемых кадров или <code>null</code>
   */
  public void setFramesList( IList<IFrame> aFrames ) {
    stopLoaderThread();
    framesList.clear();
    if( aFrames != null && !aFrames.isEmpty() ) {
      framesList.addAll( aFrames );
      reloadFramesList();
    }
  }

}
