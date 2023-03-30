package com.hazard157.psx24.core.e4.uiparts.eps;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.movies.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.core.m5.trailer.*;

/**
 * Вью просмотра списка исходников эпизода.
 *
 * @author goga
 */
public class UipartEpisodeTrailers
    extends AbstractEpisodeUipart {

  private final ITsSelectionChangeListener<Trailer> soureceVideoSelectionChangeListener =
      ( aSource, aSelectedItem ) -> onTrailerSelectionChanged( aSelectedItem );

  @Inject
  IUnitTrailers unitTrailers;

  @Inject
  ICurrentFramesListService currentFramesListService;

  @Inject
  IPsxSelectedSvinsService selectedSvinsService;

  IM5CollectionPanel<Trailer> panel;

  @Override
  protected void doCreatePartContent( Composite aParent ) {
    IM5Model<Trailer> model = m5().getModel( EpisodeTrailerM5Model.MODEL_ID, Trailer.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = model.panelCreator().createCollEditPanel( ctx, null, null );
    panel.createControl( aParent );
    panel.addTsSelectionListener( soureceVideoSelectionChangeListener );
    doSetEpisode();
  }

  void onTrailerSelectionChanged( Trailer aSel ) {
    IList<IFrame> frames = IList.EMPTY;
    if( aSel != null ) {
      frames = aSel.listFramesOfChunks();
    }
    currentFramesListService.setCurrent( frames );
    // svins
    IListEdit<Svin> svins = new ElemArrayList<>();
    if( aSel != null ) {
      for( Chunk c : aSel.chunks() ) {
        svins.add( c.svin() );
      }
    }
    selectedSvinsService.setSvins( svins );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractEpisodeUipart
  //

  @Override
  protected void doSetEpisode() {
    IM5Model<Trailer> model = m5().getModel( EpisodeTrailerM5Model.MODEL_ID, Trailer.class );
    IM5LifecycleManager<Trailer> lm = null;
    IM5ItemsProvider<Trailer> ip = null;
    if( episode() != null ) {
      lm = new EpisodeTrailerLifecycleManager( model, episode(), unitTrailers );
      ip = lm.itemsProvider();
    }
    panel.setItemsProvider( ip );
    panel.setLifecycleManager( lm );
    panel.setSelectedItem( null );
    panel.refresh();
  }

  @Override
  protected void doHandleEpisodeContentChange() {
    doSetEpisode();
  }

}
