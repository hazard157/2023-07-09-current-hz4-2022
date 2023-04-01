package com.hazard157.psx24.timeline.e4.uiparts;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.currep.*;
import com.hazard157.psx24.timeline.main.*;

/**
 * View: timeline.
 *
 * @author hazard157
 */
public class UipartTimeline
    extends MwsAbstractPart {

  ICurrentEntityChangeListener<IEpisode> episodeChangeListener = aCurrent -> updateOnCurrEpisode();

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  PanelEpisodeTimeline panel;

  @Override
  protected void doInit( Composite aParent ) {
    currentEpisodeService.addCurrentEntityChangeListener( episodeChangeListener );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new PanelEpisodeTimeline( aParent, ctx );
    updateOnCurrEpisode();
  }

  void updateOnCurrEpisode() {
    IEpisode ep = currentEpisodeService.current();
    panel.setEpisode( ep );

  }

}
