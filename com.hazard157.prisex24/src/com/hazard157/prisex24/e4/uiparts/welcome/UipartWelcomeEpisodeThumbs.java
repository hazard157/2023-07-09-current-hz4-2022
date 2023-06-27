package com.hazard157.prisex24.e4.uiparts.welcome;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.pgv.*;

import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.glib.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * UIpart: welcome episode thumbs grid.
 *
 * @author hazard157
 */
public class UipartWelcomeEpisodeThumbs
    extends PsxAbstractUipart {

  private IPicsGridViewer<IEpisode> plViewer;

  @Override
  protected void doInit( Composite aParent ) {
    plViewer = new PicsGridViewer<>( aParent, tsContext() );
    plViewer.setVisualsProvider( new EpisodeVisualsProvider( tsContext() ) );
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    prefBundle( PBID_WELCOME ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    initViewerContent();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void initViewerContent() {
    IEpisode sel = plViewer.selectedItem();
    EThumbSize thumbSize = APPREF_THUMB_SIZE_IN_GRIDS.getValue( prefBundle( PBID_HZ_COMMON ).prefs() ).asValobj();
    plViewer.setThumbSize( thumbSize );
    plViewer.setItems( unitEpisodes().items() );
    plViewer.setSelectedItem( sel );
  }

}
