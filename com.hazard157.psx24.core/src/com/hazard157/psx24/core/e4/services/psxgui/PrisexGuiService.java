package com.hazard157.psx24.core.e4.services.psxgui;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IPrisexGuiService} implementation.
 *
 * @author hazard157
 */
public class PrisexGuiService
    implements IPrisexGuiService, ITsGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - the context
   */
  public PrisexGuiService( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPrisexGuiService
  //

  @Override
  public void switchToEpisodesListPerspective() {
    e4Helper().switchToPerspective( PERSPID_EPISODES, null );
    e4Helper().updateHandlersCanExecuteState();
  }

}
