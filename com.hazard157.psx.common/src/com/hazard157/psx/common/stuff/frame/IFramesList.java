package com.hazard157.psx.common.stuff.frame;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * The list of frame.
 * <p>
 * This is a list {@link IList} with additional helper methods and it's own {@link FramesList#KEEPER}.
 *
 * @author hazard157
 */

public interface IFramesList
    extends IList<IFrame> {

  /**
   * Finds the frame closest to the specified second from the list.
   *
   * @param aSec int - the second
   * @param aPreferredCamId String - preferred camera ID or {@link IStridable#NONE_ID}
   * @return {@link IFrame} - the nearest frame, or <code>null</code> if the list is empty
   */
  IFrame findNearest( int aSec, String aPreferredCamId );

}
