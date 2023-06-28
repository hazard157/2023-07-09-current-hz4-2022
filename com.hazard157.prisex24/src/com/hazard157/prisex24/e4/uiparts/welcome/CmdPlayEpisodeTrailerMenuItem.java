package com.hazard157.prisex24.e4.uiparts.welcome;

import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.psx.common.utils.*;

/**
 * Instances of this class are menu item contribiutions for Playe episde traile drop-down menu.
 *
 * @author hazard157
 */
public class CmdPlayEpisodeTrailerMenuItem {

  private final IPrefBundle prefBundle;
  private final String      trailerId;

  CmdPlayEpisodeTrailerMenuItem( String aTrailerId, IPrefBundle aPrefBundle ) {
    trailerId = TrailerUtils.checkValidTrailerId( aTrailerId );
    prefBundle = aPrefBundle;
  }

  // FIXME >?>>
  // @Execute
  // void exec( IPsxFileSystem aFs, IMediaPlayerService aMps ) {
  // File trailerFile = aFs.trailerFilesManager().findTrailerFile( trailerId );
  // if( trailerFile != null ) {
  // boolean isFullScreen = APPRM_PLAY_FULL_SCREEN.getValue( prefBundle.params() ).asBool();
  // aMps.playVideoFile( trailerFile, isFullScreen );
  // }
  // }

}
