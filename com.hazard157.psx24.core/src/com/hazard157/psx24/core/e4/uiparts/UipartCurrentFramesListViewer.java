package com.hazard157.psx24.core.e4.uiparts;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.glib.dialogs.imgs.*;
import com.hazard157.psx24.core.glib.plv.*;

/**
 * Разделяемое вью, отображающее список {@link ICurrentFramesListService#current()}.
 *
 * @author hazard157
 */
public class UipartCurrentFramesListViewer
    extends MwsAbstractPart {

  private final ITsCollectionChangeListener appSettingsChangeListener = ( aSource, aOp, aItem ) -> {
    if( !this.isSelfParamChanging ) {
      String opId = (String)aItem;
      if( opId == null || opId.equals( APPRM_THUMBSZ_FRAMES_IN_PANELS.id() ) ) {
        changeThumbSize( readThumbSizeFromAppSettings() );
      }
    }
  };

  /**
   * Слушатель появления нового списка отображаемых изображения.
   */
  ICurrentEntityChangeListener<IList<IFrame>> currentImageListChangeListener = aCurrent -> reloadFramesList();

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

  /**
   * Обработки факта загрузки изображения.
   */
  final IFrameLoadedCallback callback = ( aFrame, aImage ) -> {
    if( aImage == null ) {
      return;
    }
    String l1, l2;
    l1 = HhMmSsFfUtils.mmssff( aFrame.frameNo() );
    l2 = aFrame.episodeId();
    PlvItem item = new PlvItem( aImage, l1, l2, aFrame );
    this.plViewer.addItem( item );
    updateActionsState();
  };

  private final ITsActionHandler toolbarListener = this::processAction;

  TsToolbar                 toolbar;
  PicturesListViewer        plViewer;
  IPrisexService            prisexService;
  IPsxFileSystem            fileSystem;
  IFrameLoaderThread        frameLoaderThread = null;
  ICurrentFramesListService currentFramesListService;
  IPrefBundle               prefBundle;

  boolean isSelfParamChanging = false;

  @Override
  protected void doInit( Composite aParent ) {
    aParent.setLayout( new BorderLayout() );
    prisexService = tsContext().get( IPrisexService.class );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    IAppPreferences aprefs = tsContext().get( IAppPreferences.class );
    prefBundle = aprefs.getBundle( PSX_COMMON_APREF_BUNDLE_ID );
    prefBundle.prefs().addCollectionChangeListener( appSettingsChangeListener );
    currentFramesListService = tsContext().get( ICurrentFramesListService.class );
    currentFramesListService.addCurrentEntityChangeListener( currentImageListChangeListener );
    // toolbar
    toolbar = TsToolbar.create( aParent, tsContext(), EIconSize.IS_16X16, //
        AI_THUMB_SIZEABLE_ZOOM_MENU, //
        AI_PLAY_MENU, ACDEF_SEPARATOR, //
        AI_WORK_WITH_FRAMES );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // pictureListViewer
    plViewer = new PicturesListViewer( aParent, EPlvLayoutMode.ROWS );
    plViewer.getControl().setLayoutData( BorderLayout.CENTER );
    plViewer.setAutoSelectNewItem( false );
    plViewer.setThumbSize( readThumbSizeFromAppSettings() );
    plViewer.setLabelsShown( true, true );
    plViewer.addTsDoubleClickListener( viewerDoubleClickListener );
    plViewer.addTsSelectionListener( viewerSelectionChangeListener );
    plViewer.setTooltipProvider( new IPlvTooltipProvider() {

      @SuppressWarnings( "nls" )
      @Override
      public Composite createToolTipContentArea( PlvItem aItem, Event aEvent, Composite aTpParent ) {
        CLabel label = new CLabel( aTpParent, SWT.SHADOW_NONE );
        Color bk = aEvent.widget.getDisplay().getSystemColor( SWT.COLOR_INFO_BACKGROUND );
        label.setBackground( bk );
        Color fg = aEvent.widget.getDisplay().getSystemColor( SWT.COLOR_INFO_FOREGROUND );
        label.setForeground( fg );

        // TODO AbstractPictureGridCanvas.AbstractPictureGridCanvas(...).new ToolTip() {...}.createToolTipContentArea()

        StringBuilder msg =
            new StringBuilder( "Coors = (" ).append( aEvent.x ).append( "," ).append( aEvent.y ).append( ")" );

        if( aItem != null ) {
          Object data = aItem.userData();
          if( data != null ) {
            msg.append( data.toString() );
          }
        }

        label.setText( msg.toString() );

        return label;
      }
    } );
    ThumbSizeableDropDownMenuCreator mc = new ThumbSizeableDropDownMenuCreator( plViewer, tsContext() ) {

      @Override
      public void doSetThumbSize( IThumbSizeable aSubject, EThumbSize aSize ) {
        changeThumbSize( aSize );
      }
    };
    mc.setAvalaiableThumbSizesRange( PSX_MIN_FRAME_THUMB_SIZE, PSX_MAX_FRAME_THUMB_SIZE );
    mc.setThumbSizesMenuItems( true );
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, mc );
    reloadFramesList();
    // if( currentFramesListService.current() != null ) {
    // frameLoaderThread =
    // fileSystem.startThumbsLoading( currentFramesListService.current(), plViewer.thumbSize(), callback, false );
    // }
    aParent.addDisposeListener( aE -> {
      if( frameLoaderThread != null ) {
        frameLoaderThread.close();
        frameLoaderThread = null;
      }
    } );
    updateActionsState();
  }

  EThumbSize readThumbSizeFromAppSettings() {
    return APPRM_THUMBSZ_FRAMES_IN_PANELS.getValue( prefBundle.prefs() ).asValobj();
  }

  void saveThumbSizeToAppSettings() {
    isSelfParamChanging = true;
    prefBundle.prefs().setValobj( APPRM_THUMBSZ_FRAMES_IN_PANELS, plViewer.thumbSize() );
    isSelfParamChanging = false;
  }

  void changeThumbSize( EThumbSize aNewSize ) {
    plViewer.clearItems();
    plViewer.setThumbSize( aNewSize );
    saveThumbSizeToAppSettings();
    reloadFramesList();
  }

  void reloadFramesList() {
    if( frameLoaderThread != null ) {
      frameLoaderThread.close();
      frameLoaderThread = null;
    }
    plViewer.clearItems();
    plViewer.setThumbSize( readThumbSizeFromAppSettings() );
    if( currentFramesListService.current() != null ) {
      frameLoaderThread =
          fileSystem.startThumbsLoading( currentFramesListService.current(), plViewer.thumbSize(), callback, false );
    }
    updateActionsState();
  }

  void processAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    switch( aActionId ) {
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        changeThumbSize( plViewer.defaultThumbSize() );
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

}
