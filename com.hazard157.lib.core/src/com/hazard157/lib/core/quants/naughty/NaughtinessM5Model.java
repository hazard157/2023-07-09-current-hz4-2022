package com.hazard157.lib.core.quants.naughty;

import static com.hazard157.lib.core.quants.naughty.INaughtinessConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.M5StridableEnumModelBase;

/**
 * M5-Model of {@link ENaughtiness}.
 *
 * @author hazard157
 */
public class NaughtinessM5Model
    extends M5StridableEnumModelBase<ENaughtiness> {

  /**
   * Constructor.
   */
  public NaughtinessM5Model() {
    super( MID_NAUGHTINESS, ENaughtiness.class );
  }

}
