package com.hazard157.prisex24.e4.uiparts.welcome;

import com.hazard157.prisex24.*;

/**
 * Controller of the welcome perspective.
 * <p>
 * Instance is placed in the windows level context.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IWelcomePerspectiveController {

  /**
   * Toggles {@link IPrisex24CoreConstants#APPREF_WELCOME_IS_FORCE_STILL} preference value.
   */
  void toggleStillEpisodeThumbs();

  UipartWelcomeEpisodeThumbs uipartEpisodes();

  UipartWelcomeFilmsThumbs uipartFilms();

  void setEpisodesPart( UipartWelcomeEpisodeThumbs aUipart );

  void setFilmsPart( UipartWelcomeFilmsThumbs aUipart );

}
