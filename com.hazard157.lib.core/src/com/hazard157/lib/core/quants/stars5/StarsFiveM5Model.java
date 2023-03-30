package com.hazard157.lib.core.quants.stars5;

import static com.hazard157.lib.core.quants.stars5.IStarsFiveConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.M5StridableEnumModelBase;

/**
 * M5-Model of {@link EStarsFive}.
 *
 * @author hazard157
 */
public class StarsFiveM5Model
    extends M5StridableEnumModelBase<EStarsFive> {

  /**
   * Constructor.
   */
  public StarsFiveM5Model() {
    super( MID_STARS_FIVE, EStarsFive.class );
  }

}
