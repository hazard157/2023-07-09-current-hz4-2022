package com.hazard157.prisex24.e4.uiparts.welcome;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;

/**
 * {@link IWelcomePerspectiveController} implementation.
 *
 * @author hazard157
 */
public class WelcomePerspectiveController
    implements IWelcomePerspectiveController, IPsxGuiContextable {

  private final ITsGuiContext tsContext;

  private UipartWelcomeEpisodeThumbs uipartEpisodes = null;
  private UipartWelcomeFilmsThumbs   uipartFilms    = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public WelcomePerspectiveController( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IWelcomePerspectiveController
  //

  @Override
  public void toggleStillEpisodeThumbs() {
    IPrefBundle pb = prefBundle( PBID_WELCOME );
    boolean isStillForced = APPREF_WELCOME_IS_FORCE_STILL.getValue( pb.prefs() ).asBool();
    pb.prefs().setBool( APPREF_WELCOME_IS_FORCE_STILL, !isStillForced );
    e4Helper().updateHandlersCanExecuteState();
  }

  @Override
  public UipartWelcomeEpisodeThumbs uipartEpisodes() {
    return uipartEpisodes;
  }

  @Override
  public UipartWelcomeFilmsThumbs uipartFilms() {
    return uipartFilms;
  }

  @Override
  public void setEpisodesPart( UipartWelcomeEpisodeThumbs aUipart ) {
    uipartEpisodes = aUipart;
  }

  @Override
  public void setFilmsPart( UipartWelcomeFilmsThumbs aUipart ) {
    uipartFilms = aUipart;
  }

}
