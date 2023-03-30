package com.hazard157.psx.common.stuff;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Entities related to the episode shooting camera.
 *
 * @author hazard157
 */
public interface ICameraIdable {

  /**
   * Return the camera ID.
   * <p>
   * Always returns valid IDpath.
   *
   * @return String - the camera ID or {@link IStridable#NONE_ID}
   */
  String cameraId();

  /**
   * Determines if camera ID is specified.
   *
   * @return boolean - the flag that the camera is specified
   */
  default boolean hasCam() {
    return !cameraId().equals( IStridable.NONE_ID );
  }

}
