package com.hazard157.prisex24.e4.uiparts.welcome;

import org.eclipse.e4.core.di.annotations.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.common.incub.fs.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Command play default trailer of the episode selected in {@link UipartWelcomeEpisodeThumbs}.
 *
 * @author hazard157
 */
public class CmdPlayEpisode {

  @Execute
  void exec( IWelcomePerspectiveController aWelcomePerspectiveController, IPsxCofs aCofs, IMediaPlayerService aMps ) {
    IEpisode sel = aWelcomePerspectiveController.uipartEpisodes().selectedItem();
    if( sel != null ) {
      String traileName = sel.info().defaultTrailerId();
      OptedFile f = aCofs.cofsTrailers().findTrailerFile( sel.id(), traileName );
      if( f != null ) {
        aMps.playVideoFile( f.file() );
      }
    }
  }

  @CanExecute
  boolean canExec( IWelcomePerspectiveController aWelcomePerspectiveController ) {
    return aWelcomePerspectiveController.uipartEpisodes().selectedItem() != null;
  }

}
