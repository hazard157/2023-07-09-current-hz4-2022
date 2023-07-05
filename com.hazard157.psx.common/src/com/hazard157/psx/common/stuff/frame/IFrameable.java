package com.hazard157.psx.common.stuff.frame;

/**
 * Mix-in interface for entities having illustrative frame.
 *
 * @author hazard157
 */
public interface IFrameable {

  /**
   * Returns a frame illustrating this entity.
   *
   * @return {@link IFrame} - illustrating frame, may be {@link IFrame#NONE}, but not null
   */
  IFrame frame();

}
