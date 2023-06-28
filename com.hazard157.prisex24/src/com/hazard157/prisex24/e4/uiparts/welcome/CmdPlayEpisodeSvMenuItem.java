package com.hazard157.prisex24.e4.uiparts.welcome;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.psx.common.utils.*;

/**
 * Instances of this class are menu item contributions for Play episode trailer drop-down menu.
 *
 * @author hazard157
 */
public class CmdPlayEpisodeSvMenuItem {

  private final IPrefBundle prefBundle;
  private final String      sourceVideoId;

  CmdPlayEpisodeSvMenuItem( String aSourceVideoId, IPrefBundle aPrefBundle ) {
    sourceVideoId = SourceVideoUtils.checkValidSourceVideoId( aSourceVideoId );
    prefBundle = aPrefBundle;
  }

  @Execute
  void exec( IPsxCofs aCofs, IMediaPlayerService aMps ) {
    // FIXME File svFile = aCofs.sourceVideoFilesManager().findSourceVideoFile( sourceVideoId );
    // if( svFile != null ) {
    // aMps.playVideoFile( svFile );
    // }
  }

}
