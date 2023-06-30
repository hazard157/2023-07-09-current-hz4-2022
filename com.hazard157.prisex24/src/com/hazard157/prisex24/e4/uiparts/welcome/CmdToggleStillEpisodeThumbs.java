package com.hazard157.prisex24.e4.uiparts.welcome;

import org.eclipse.e4.core.di.annotations.*;

import com.hazard157.prisex24.*;

/**
 * Command toggles {@link IPrisex24CoreConstants#APPREF_WELCOME_IS_FORCE_STILL} preference value.
 *
 * @author hazard157
 */
public class CmdToggleStillEpisodeThumbs {

  @Execute
  void exec( IWelcomePerspectiveController aWelcomePerspectiveController ) {
    aWelcomePerspectiveController.toggleStillEpisodeThumbs();
  }

}
