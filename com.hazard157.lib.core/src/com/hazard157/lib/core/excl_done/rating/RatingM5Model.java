package com.hazard157.lib.core.excl_done.rating;

import static com.hazard157.lib.core.excl_done.rating.IRatingConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.M5StridableEnumModelBase;

/**
 * M5-Model of {@link ERating}.
 *
 * @author hazard157
 */
public class RatingM5Model
    extends M5StridableEnumModelBase<ERating> {

  /**
   * Constructor.
   */
  public RatingM5Model() {
    super( MID_RATING, ERating.class );
  }

}
