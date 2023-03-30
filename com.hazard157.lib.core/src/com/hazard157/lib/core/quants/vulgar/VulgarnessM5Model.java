package com.hazard157.lib.core.quants.vulgar;

import static com.hazard157.lib.core.quants.vulgar.IVulgarnessConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.M5StridableEnumModelBase;

/**
 * M5-Model of {@link EVulgarness}.
 *
 * @author hazard157
 */
public class VulgarnessM5Model
    extends M5StridableEnumModelBase<EVulgarness> {

  /**
   * Constructor.
   */
  public VulgarnessM5Model() {
    super( MID_VULGARNESS, EVulgarness.class );
  }

}
