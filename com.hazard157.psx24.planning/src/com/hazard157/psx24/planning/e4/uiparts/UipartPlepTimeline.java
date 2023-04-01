package com.hazard157.psx24.planning.e4.uiparts;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;

import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx24.planning.e4.services.*;
import com.hazard157.psx24.planning.glib.*;

/**
 * Вью просмотра графика планируемого эпизода.
 *
 * @author hazard157
 */
public class UipartPlepTimeline
    extends MwsAbstractPart {

  private final ICurrentEntityChangeListener<IPlep> currentPlepChangeListener = aCurrent -> updateOnCurrentPlep();

  @Inject
  ICurrentPlepService currentPlepService;

  PanelPlepTimeline panel;

  @Override
  protected void doInit( Composite aParent ) {
    currentPlepService.addCurrentEntityChangeListener( currentPlepChangeListener );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new PanelPlepTimeline( aParent, ctx );
    updateOnCurrentPlep();
  }

  void updateOnCurrentPlep() {
    IPlep plep = currentPlepService.current();
    panel.setCurrentPlep( plep );
  }

}
