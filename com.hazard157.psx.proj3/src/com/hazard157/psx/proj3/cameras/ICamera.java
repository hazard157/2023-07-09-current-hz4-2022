package com.hazard157.psx.proj3.cameras;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * The shooting camera.
 *
 * @author hazard157
 */
public interface ICamera
    extends IStridable {

  /**
   * Returns the indication that the camera is still available for filming.
   *
   * @return boolean - a sign that the camera is still available for filming
   */
  boolean isCamAvailable();

  /**
   * Returns the camera kind.
   *
   * @return {@link ECameraKind} - camera kind
   */
  ECameraKind kind();

}
