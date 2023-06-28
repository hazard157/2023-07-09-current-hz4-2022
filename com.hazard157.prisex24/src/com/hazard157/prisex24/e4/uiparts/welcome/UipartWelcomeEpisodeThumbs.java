package com.hazard157.prisex24.e4.uiparts.welcome;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.pgv.*;

import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.glib.*;
import com.hazard157.prisex24.glib.locations.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * UIpart: welcome episode thumbs grid.
 *
 * @author hazard157
 */
public class UipartWelcomeEpisodeThumbs
    extends PsxAbstractUipart {

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  private IPicsGridViewer<IEpisode> pgViewer;

  @Override
  protected void doInit( Composite aParent ) {
    pgViewer = new PicsGridViewer<>( aParent, tsContext() );
    pgViewer.setVisualsProvider( new EpisodeVisualsProvider( tsContext() ) );
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    prefBundle( PBID_WELCOME ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    pgViewer.addTsSelectionListener( ( src, sel ) -> currentEpisodeService.setCurrent( sel ) );
    pgViewer.addTsDoubleClickListener( ( src, sel ) -> whenThumbDoubleClicked( sel ) );
    currentEpisodeService.addCurrentEntityChangeListener( curr -> pgViewer.setSelectedItem( curr ) );
    initViewerContent();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void initViewerContent() {
    IEpisode sel = pgViewer.selectedItem();
    EThumbSize thumbSize = APPREF_THUMB_SIZE_IN_GRIDS.getValue( prefBundle( PBID_HZ_COMMON ).prefs() ).asValobj();
    pgViewer.setThumbSize( thumbSize );
    pgViewer.setItems( unitEpisodes().items() );
    pgViewer.setSelectedItem( sel );
  }

  private void whenThumbDoubleClicked( IEpisode aSel ) {
    if( aSel != null ) {
      mwsLocationService().locate( EpisodePropertyLocator.ofEpisode( aSel ) );
    }
  }

}
