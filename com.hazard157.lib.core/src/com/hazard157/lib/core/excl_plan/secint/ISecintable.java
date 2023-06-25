package com.hazard157.lib.core.excl_plan.secint;

import com.hazard157.common.quants.secint.Secint;

/**
 * Mix-in interface for entities having seconds interval .
 *
 * @author hazard157
 */
public interface ISecintable {

  /**
   * Returns the interval of the entity.
   *
   * @return {@link Secint} - the interval
   */
  Secint interval();

}
