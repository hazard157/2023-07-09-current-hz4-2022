package com.hazard157.prisex24.e4.uiparts.episodes;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.m5.srcvideo.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * UIpart: list of current episode source videos.
 *
 * @author hazard157
 */
public class UipartEpisodeSourceVideos
    extends AbstractEpisodeUipart {

  private final ITsSelectionChangeListener<ISourceVideo> soureceVideoSelectionChangeListener =
      ( aSource, aSelectedItem ) -> onSeourceVideoSelectionChanged( aSelectedItem );

  @Inject
  IUnitSourceVideos unitSourceVideos;

  @Inject
  IPsxSelectedSvinsService selectedSvinsService;

  IM5CollectionPanel<ISourceVideo> panel;

  @Override
  protected void doCreatePartContent( Composite aParent ) {
    IM5Model<ISourceVideo> model = m5().getModel( MID_SOURCE_VIDEO, ISourceVideo.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<ISourceVideo> lm = new EpisodeSourceVideoLifecycleManager( model, episode(), unitSourceVideos );
    panel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    panel.createControl( aParent );
    panel.addTsSelectionListener( soureceVideoSelectionChangeListener );
    doSetEpisode();
  }

  void onSeourceVideoSelectionChanged( ISourceVideo aSel ) {
    Svin svin = null;
    if( aSel != null ) {
      Secint in = new Secint( 0, aSel.duration() - 1 );
      svin = new Svin( aSel.episodeId(), aSel.cameraId(), in, aSel.frame() );
    }
    selectedSvinsService.setSvin( svin );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractEpisodeUipart
  //

  @Override
  protected void doSetEpisode() {
    IM5Model<ISourceVideo> model = m5().getModel( MID_SOURCE_VIDEO, ISourceVideo.class );
    IM5LifecycleManager<ISourceVideo> lm = new EpisodeSourceVideoLifecycleManager( model, episode(), unitSourceVideos );
    panel.setItemsProvider( lm.itemsProvider() );
    panel.setLifecycleManager( lm );
    panel.setSelectedItem( null );
    panel.refresh();
  }

  @Override
  protected void doHandleEpisodeContentChange() {
    doSetEpisode();
  }

}
