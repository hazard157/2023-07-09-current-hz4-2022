package com.hazard157.psx24.intro.e4.uiparts;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.intro.IPsxIntroGuiConstants.*;
import static com.hazard157.psx24.intro.IPsxIntroSharedResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.e4.services.currep.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.e4.services.psxgui.*;

/**
 * Welcome view with eisodes thumbs.
 *
 * @author hazard157
 */
public class UipartIntro
    extends MwsAbstractPart
    implements IPsxGuiContextable {

  private static final String ACTID_TOGGLE_FORCE_STILL = "act.force_toggle_still"; //$NON-NLS-1$

  private static final ITsActionDef ACTDEF_TOGGLE_FORCE_STILL = TsActionDef.ofCheck2( ACTID_TOGGLE_FORCE_STILL, //
      STR_IS_FORCE_STILL_FRAME, STR_IS_FORCE_STILL_FRAME_D, ICONID_AK_SINGLE );

  private final ITsVisualsProvider<IEpisode> visualsProvider = new ITsVisualsProvider<>() {

    @Override
    public String getName( IEpisode aItem ) {
      String fmtStr = "%1$te %1$tB %1$tY"; //$NON-NLS-1$
      if( APPRM_IS_LABEL_AS_YMD.getValue( prefBundle.prefs() ).asBool() ) {
        fmtStr = "%tF"; //$NON-NLS-1$
      }
      return String.format( fmtStr, Long.valueOf( aItem.info().when() ) );
    }

    @Override
    public TsImage getThumb( IEpisode aItem, EThumbSize aThumbSize ) {
      boolean isStillForced = APPRM_IS_FORCE_STILL_FRAME.getValue( prefBundle.prefs() ).asBool();
      File ff = fileSystem.findFrameFile( aItem.frame() );
      TsImage mi = null;
      if( ff != null ) {
        mi = imageManager().findThumb( ff, aThumbSize );
      }
      if( mi == null ) {
        mi = getNoneImageForEpisode( aThumbSize );
      }
      if( mi.isAnimated() && isStillForced ) {
        mi = TsImage.create( mi.image() ); // SWT image will be disposed when anumated mi disposes
      }
      return mi;
    }

  };

  private final ITsCollectionChangeListener appSettingsChangeListener = ( aSource, aOp, aItem ) -> {
    // String opId = (String)aItem;
    // if( opId == null || opId.equals( APPRM_THUMB_SIZE.id() ) || opId.equals( APPRM_IS_LABEL_AS_YMD.id() ) ) {
    initViewerContent();
    // }
  };

  private final IGenericChangeListener episodesUnitChangeListener = aSource -> initViewerContent();

  private final ITsActionHandler toolbarListener = this::processAction;

  IPrefBundle prefBundle;

  ICurrentEpisodeService currentEpisodeService;
  IPlayMenuSupport       playMenuSupport;

  TsToolbar                 toolbar;
  IPicsGridViewer<IEpisode> plViewer;
  IPsxFileSystem            fileSystem;

  boolean isLocalCurrEpChange = false;

  @Override
  protected void doInit( Composite aParent ) {
    IAppPreferences apprefs = tsContext().get( IAppPreferences.class );
    prefBundle = apprefs.getBundle( PSX_INTRO_APREF_BUNDLE_ID );
    prefBundle.prefs().addCollectionChangeListener( appSettingsChangeListener );
    unitEpisodes().genericChangeEventer().addListener( episodesUnitChangeListener );
    playMenuSupport = tsContext().get( IPlayMenuSupport.class );
    currentEpisodeService = tsContext().get( ICurrentEpisodeService.class );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    aParent.setLayout( new BorderLayout() );
    toolbar = TsToolbar.create( aParent, tsContext(), EIconSize.IS_16X16, new TsActionDef( AI_PLAY_MENU ),
        ACDEF_SEPARATOR, ACDEF_ZOOM_IN, ACDEF_ZOOM_ORIGINAL, ACDEF_ZOOM_OUT, ACDEF_SEPARATOR, //
        ACTDEF_TOGGLE_FORCE_STILL );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    plViewer = new PicsGridViewer<>( aParent, tsContext() );
    plViewer.setVisualsProvider( visualsProvider );
    plViewer.getControl().setLayoutData( BorderLayout.CENTER );
    EThumbSize thumbSize = APPRM_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj();
    plViewer.setThumbSize( thumbSize );
    plViewer.addTsSelectionListener( ( aSource, aSelectedItem ) -> setCurrEpisode( aSelectedItem ) );
    plViewer.addTsDoubleClickListener( ( aSource, aSelectedItem ) -> {
      IPrisexGuiService pgs = tsContext().get( IPrisexGuiService.class );
      setCurrEpisode( aSelectedItem );
      pgs.switchToEpisodesListPerspective();
    } );
    currentEpisodeService.addCurrentEntityChangeListener( aCurrent -> onCurrEpisodeChange() );
    initViewerContent();
    onCurrEpisodeChange();
  }

  private TsImage getNoneImageForEpisode( EThumbSize aThumbSize ) {
    int sz = aThumbSize.size();
    Image img = iconManager().loadStdIcon( ICON_PSX_NONE_EPISODE_IMAGE, EIconSize.findIncluding( sz, sz ) );
    return TsImage.create( img );
  }

  void processAction( String aActionId ) {
    EThumbSize thumbSize = APPRM_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj();
    switch( aActionId ) {
      case ACTID_ZOOM_IN: {
        APPRM_THUMB_SIZE.setValue( prefBundle.prefs(), avValobj( thumbSize.nextSize() ) );
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        APPRM_THUMB_SIZE.setValue( prefBundle.prefs(), APPRM_THUMB_SIZE.defaultValue() );
        break;
      }
      case ACTID_ZOOM_OUT: {
        APPRM_THUMB_SIZE.setValue( prefBundle.prefs(), avValobj( thumbSize.prevSize() ) );
        break;
      }
      case AID_PLAY: {
        IEpisode selEpisode = plViewer.selectedItem();
        if( selEpisode != null ) {
          IPrisexService prisexService = tsContext().get( IPrisexService.class );
          prisexService.playEpisodeVideo( new Svin( selEpisode.id() ) );
        }
        break;
      }
      case ACTID_TOGGLE_FORCE_STILL: {
        e4Helper().execCmd( CMDID_TOGGLE_FORCE_STILL );
        updateActionsState();
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  void updateActionsState() {
    IEpisode selEpisode = plViewer.selectedItem();
    boolean isSel = selEpisode != null;
    toolbar.setActionEnabled( AID_PLAY, isSel );
    boolean isStillForced = APPRM_IS_FORCE_STILL_FRAME.getValue( prefBundle.prefs() ).asBool();
    toolbar.setActionChecked( ACTID_TOGGLE_FORCE_STILL, isStillForced );
    updatePlayMenu();
  }

  void setCurrEpisode( IEpisode aEpisode ) {
    isLocalCurrEpChange = true;
    try {
      currentEpisodeService.setCurrent( aEpisode );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    finally {
      isLocalCurrEpChange = false;
    }
    e4Helper().updateHandlersCanExecuteState();
    updateActionsState();
  }

  void onCurrEpisodeChange() {
    if( isLocalCurrEpChange ) {
      return;
    }
    plViewer.setSelectedItem( currentEpisodeService.current() );
    updateActionsState();
  }

  private void updatePlayMenu() {
    final IEpisode episode = plViewer.selectedItem();
    if( episode != null ) {
      final IPlayMenuParamsProvider playParamsProvider = new IPlayMenuParamsProvider() {

        @Override
        public int spotlightSec() {
          IFrame f = episode.frame();
          if( f.isDefined() ) {
            return f.secNo();
          }
          return -1;
        }

        @Override
        public Svin playParams() {
          return new Svin( episode.id() );
        }

      };
      if( playMenuSupport != null ) {
        toolbar.setActionMenu( AID_PLAY, playMenuSupport.getPlayMenuCreator( tsContext(), playParamsProvider ) );
      }
    }
    else {
      toolbar.setActionMenu( AID_PLAY, null );
    }
  }

  void initViewerContent() {
    EThumbSize thumbSize = APPRM_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj();
    plViewer.setThumbSize( thumbSize );
    plViewer.setItems( null );
    plViewer.setItems( unitEpisodes().items() );
  }

  @Override
  protected void whenPartActivated() {
    onCurrEpisodeChange();
  }

  @Override
  protected void whenPartBroughtToTop() {
    onCurrEpisodeChange();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Кеширует в {@link ITsImageManager} изображения экрана приветствия.
   * <p>
   * Можно вызвать заранее, чтобы убыстрыть появление вью.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  public static void preloadNeededImages( IEclipseContext aWinContext ) {
    IPrefBundle prefBundle = aWinContext.get( IPrefBundle.class );
    IUnitEpisodes unitEpisodes = aWinContext.get( IUnitEpisodes.class );
    IPsxFileSystem fileSystem = aWinContext.get( IPsxFileSystem.class );
    EThumbSize thumbSize = APPRM_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj();
    IListEdit<File> ll = new ElemArrayList<>( 256 );
    for( IEpisode e : unitEpisodes.items() ) {
      File ff = fileSystem.findFrameFile( e.frame() );
      if( ff != null ) {
        ll.add( ff );
      }
    }
    ImageThumbsPreloader runner = new ImageThumbsPreloader( new TsGuiContext( aWinContext ), thumbSize, ll );
    Display display = aWinContext.get( Display.class );
    display.asyncExec( runner );

  }

}
