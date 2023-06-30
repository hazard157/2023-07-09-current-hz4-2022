package com.hazard157.prisex24.e4.uiparts.welcome;

import org.eclipse.e4.core.di.annotations.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.common.incub.fs.*;

/**
 * Command play film selected in {@link UipartWelcomeFilmsThumbs}.
 *
 * @author hazard157
 */
public class CmdPlayFilm {

  @Execute
  void exec( IWelcomePerspectiveController aWelcomePerspectiveController, IMediaPlayerService aMps ) {
    OptedFile sel = aWelcomePerspectiveController.uipartFilms().selectedItem();
    if( sel != null ) {
      aMps.playVideoFile( sel.file() );
    }
  }

  @CanExecute
  boolean canExec( IWelcomePerspectiveController aWelcomePerspectiveController ) {
    return aWelcomePerspectiveController.uipartFilms().selectedItem() != null;
  }

}
