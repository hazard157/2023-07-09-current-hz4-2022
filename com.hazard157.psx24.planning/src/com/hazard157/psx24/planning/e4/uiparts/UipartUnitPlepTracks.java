package com.hazard157.psx24.planning.e4.uiparts;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;

import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx24.planning.e4.services.*;
import com.hazard157.psx24.planning.m5.*;

/**
 * Вью просмотра и правки {@link IPlep#tracks()} текущего {@link ICurrentPlepService#current()}.
 *
 * @author goga
 */
public class UipartUnitPlepTracks
    extends MwsAbstractPart {

  private final ICurrentEntityChangeListener<IPlep> currentPlepChangeListener = aCurrent -> updateOnCurrentPlep();

  private final ITsSelectionChangeListener<ITrack> selectedTrackChangeListener =
      ( aSource, aSelectedItem ) -> this.currentTrackService.setCurrent( aSelectedItem );

  @Inject
  ICurrentPlepService currentPlepService;

  @Inject
  ICurrentTrackService currentTrackService;

  IM5CollectionPanel<ITrack> tracksPanel;

  @Override
  protected void doInit( Composite aParent ) {
    currentPlepService.addCurrentEntityChangeListener( currentPlepChangeListener );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5Model<ITrack> tracksModel = m5().getModel( TrackM5Model.MODEL_ID, ITrack.class );
    tracksPanel = tracksModel.panelCreator().createCollEditPanel( ctx, null, null );
    tracksPanel.createControl( aParent );
    tracksPanel.addTsSelectionListener( selectedTrackChangeListener );
    updateOnCurrentPlep();
  }

  void updateOnCurrentPlep() {
    IPlep plep = currentPlepService.current();
    if( plep != null ) {
      IM5Model<ITrack> tracksModel = m5().getModel( TrackM5Model.MODEL_ID, ITrack.class );
      IM5LifecycleManager<ITrack> lm = tracksModel.getLifecycleManager( plep );
      tracksPanel.setItemsProvider( lm.itemsProvider() );
      tracksPanel.setLifecycleManager( lm );
    }
    else {
      tracksPanel.setItemsProvider( null );
      tracksPanel.setLifecycleManager( null );
    }
    tracksPanel.refresh();
    currentTrackService.setCurrent( tracksPanel.selectedItem() );
  }

}
