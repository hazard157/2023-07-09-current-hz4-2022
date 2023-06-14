package com.hazard157.psx.proj3.incident.when;

import java.time.*;

/**
 * Mixin interface of entities having timestamp.
 *
 * @author hazard157
 */
public interface IWhenable {

  /**
   * Returns the timestamp.
   *
   * @return {@link LocalDateTime} - the timestamp
   */
  LocalDateTime when();

}
