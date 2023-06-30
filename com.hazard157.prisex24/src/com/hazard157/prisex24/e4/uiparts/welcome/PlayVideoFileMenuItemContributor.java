package com.hazard157.prisex24.e4.uiparts.welcome;

import java.io.*;

import org.eclipse.e4.core.di.annotations.*;

import com.hazard157.common.e4.services.mps.*;

/**
 * Instances of this class are menu item contributions for Play video file drop-down menu.
 *
 * @author hazard157
 */
public class PlayVideoFileMenuItemContributor {

  private final File videoFile;

  PlayVideoFileMenuItemContributor( File aVideoFile ) {
    videoFile = aVideoFile;
  }

  @Execute
  void exec( IMediaPlayerService aMps ) {
    if( videoFile != null ) {
      aMps.playVideoFile( videoFile );
    }
  }

}
