package com.hazard157.prisex24.e4.services.psx;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Application-specific methods.
 *
 * @author hazard157
 */
public interface IPrisex24Service {

  /**
   * Finds frame file and loads thumbnail image.
   *
   * @param aFrame {@link IFrame} - the frame
   * @param aThumbSize {@link EThumbSize} - asked size of the thumbnail
   * @return {@link TsImage} - loaded thumbnail or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize );

  /**
   * Plays source video with the specified parameters.
   * <p>
   * If it is not possible to play the requested video, the method throws a dialog and returns without exception.
   *
   * @param aSvin {@link Svin} - play parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void playEpisodeVideo( Svin aSvin );

  /**
   * Displays directory chooser dialog and copies frame image to the destination chosen by the user.
   * <p>
   * Displays error message if image can not be copied, does not throws an exception.
   * <p>
   * Remembers last destination directory.
   *
   * @param aFrame {@link IFrame} - the frame to copy
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void copyFrameImage( IFrame aFrame );

  /**
   * Displays Kdenlive project selector appropriate for the specified frame and invokes Kdenlive on selected project.
   *
   * @param aFrame {@link IFrame} - the frame to indicate episode
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void runKdenliveFor( IFrame aFrame );

}
