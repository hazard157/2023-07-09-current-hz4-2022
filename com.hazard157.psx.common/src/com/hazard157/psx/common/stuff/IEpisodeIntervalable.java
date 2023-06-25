package com.hazard157.psx.common.stuff;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Entities that are related to the time interval of an episode.
 *
 * @author hazard157
 */
public interface IEpisodeIntervalable {

  /**
   * Returns the time interval in episode.
   *
   * @return {@link Secint} - the time interval in episode
   */
  Secint interval();

}
