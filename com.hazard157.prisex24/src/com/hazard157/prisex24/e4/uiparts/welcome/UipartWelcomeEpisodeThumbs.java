package com.hazard157.prisex24.e4.uiparts.welcome;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.e4.uiparts.welcome.IPsxResources.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import java.io.*;

import javax.inject.*;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.opfil.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.glib.*;
import com.hazard157.prisex24.glib.locations.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * UIpart: welcome episode thumbs grid.
 *
 * @author hazard157
 */
public class UipartWelcomeEpisodeThumbs
    extends PsxAbstractUipart
    implements ITsSelectionProvider<IEpisode> {

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  private IPicsGridViewer<IEpisode> pgViewer;

  @Override
  protected void doInit( Composite aParent ) {
    IWelcomePerspectiveController controller = tsContext().get( IWelcomePerspectiveController.class );
    controller.setEpisodesPart( this );
    pgViewer = new PicsGridViewer<>( aParent, tsContext() );
    pgViewer.setVisualsProvider( new EpisodeVisualsProvider( tsContext() ) );
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    prefBundle( PBID_WELCOME ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    pgViewer.addTsSelectionListener( ( src, sel ) -> whenThumbSelected( sel ) );
    pgViewer.addTsDoubleClickListener( ( src, sel ) -> whenThumbmDoubleClicked( sel ) );
    currentEpisodeService.addCurrentEntityChangeListener( curr -> pgViewer.setSelectedItem( curr ) );
    initViewerContent();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void initViewerContent() {
    preloadImages();
    IEpisode sel = pgViewer.selectedItem();
    EThumbSize thumbSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    boolean isForceStill = apprefValue( PBID_WELCOME, APPREF_WELCOME_IS_FORCE_STILL ).asBool();
    pgViewer.setFocreStill( isForceStill );
    pgViewer.setThumbSize( thumbSize );
    pgViewer.setItems( unitEpisodes().items() );
    pgViewer.setSelectedItem( sel );
  }

  protected void preloadImages() {
    // TODO add progress dialog
    EThumbSize thumbSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    for( IEpisode e : unitEpisodes().items() ) {
      File ff = cofsFrames().findFrameFile( e.frame() );
      if( ff != null ) {
        imageManager().findThumb( ff, thumbSize );
      }
    }
  }

  private void whenThumbmDoubleClicked( IEpisode aSel ) {
    if( aSel != null ) {
      mwsLocationService().locate( EpisodePropertyLocator.ofEpisode( aSel ) );
    }
  }

  private void whenThumbSelected( IEpisode aSel ) {
    currentEpisodeService.setCurrent( aSel );
    updateTrailesDropDownMenuOfViewToolButton( aSel );
    e4Helper().updateHandlersCanExecuteState();
  }

  private MToolItem findPlayEpisodeMenuToolButton() {
    // find button "Play episode"
    MWindow mainWindow = tsContext().get( MWindow.class );
    return e4Helper().findElement( mainWindow, TOOLITEMID_WELCOME_EPISODE_PLAY, MToolItem.class, //
        EModelService.IN_ACTIVE_PERSPECTIVE | EModelService.IN_PART );
  }

  void updateTrailesDropDownMenuOfViewToolButton( IEpisode aSelectedEpisode ) {
    MToolItem btn = findPlayEpisodeMenuToolButton();
    if( btn == null ) {
      return;
    }
    if( aSelectedEpisode == null ) {
      btn.setMenu( null );
      return;
    }
    // icons for trailer items to distinguish default one
    String defIconUri = iconManager().findStdIconBundleUri( ICONID_ARROW_RIGHT, EIconSize.IS_16X16 );
    String nonIconUri = iconManager().findStdIconBundleUri( ICONID_TRANSPARENT, EIconSize.IS_16X16 );
    // add all known trailers to menu
    IList<IOptedFile> trailerFiles = cofsTrailers().listEpisodeTrailerFiles( aSelectedEpisode.episodeId() );
    MMenu menu = MMenuFactory.INSTANCE.createMenu();
    for( IOptedFile trOp : trailerFiles ) {
      MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
      String name = TsFileUtils.extractBareFileName( trOp.file().getName() );
      dynamicItem.setLabel( String.format( FMT_T_PLAY_TRAILER, name ) );
      dynamicItem.setTooltip( String.format( FMT_P_PLAY_TRAILER, name ) );
      dynamicItem.setObject( new PlayVideoFileMenuItemContributor( trOp.file() ) );
      // distinguish default trailer
      if( name.equals( aSelectedEpisode.info().defaultTrailerId() ) ) {
        dynamicItem.setIconURI( defIconUri );
      }
      else {
        dynamicItem.setIconURI( nonIconUri );
      }
      menu.getChildren().add( dynamicItem );
    }
    btn.setMenu( menu );
    // add menu separator if needed
    IStringMap<ISourceVideo> svMap = unitSourceVideos().episodeSourceVideos( aSelectedEpisode.id() );
    if( menu.getChildren().size() > 0 && svMap.size() > 0 ) {
      menu.getChildren().add( MMenuFactory.INSTANCE.createMenuSeparator() );
    }
    // add all source videos to menu
    for( ISourceVideo sv : svMap ) {
      String svFullId = sv.id();
      MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
      File file = psxCofs().findSourceVideoFile( svFullId );
      String camId = SourceVideoUtils.extractCamId( svFullId );
      dynamicItem.setLabel( String.format( FMT_T_PLAY_SV, camId ) );
      if( file != null ) {
        dynamicItem.setTooltip( String.format( FMT_P_PLAY_SV, file.getName() ) );
        dynamicItem.setObject( new PlayVideoFileMenuItemContributor( file ) );
      }
      else {
        dynamicItem.setEnabled( false );
      }
      dynamicItem.setIconURI( nonIconUri );
      menu.getChildren().add( dynamicItem );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IEpisode> aListener ) {
    pgViewer.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IEpisode> aListener ) {
    pgViewer.removeTsSelectionListener( aListener );
  }

  @Override
  public IEpisode selectedItem() {
    return pgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IEpisode aItem ) {
    pgViewer.setSelectedItem( aItem );
  }

}
