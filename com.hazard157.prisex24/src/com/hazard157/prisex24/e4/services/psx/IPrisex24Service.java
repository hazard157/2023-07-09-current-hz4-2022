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

}
