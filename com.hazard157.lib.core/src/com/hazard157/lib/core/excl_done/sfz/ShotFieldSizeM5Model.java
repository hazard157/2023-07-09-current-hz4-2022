package com.hazard157.lib.core.excl_done.sfz;

import static com.hazard157.lib.core.excl_done.sfz.IShotFieldSizeConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.M5StridableEnumModelBase;

/**
 * M5-Model of {@link EShotFieldSize}.
 *
 * @author hazard157
 */
public class ShotFieldSizeM5Model
    extends M5StridableEnumModelBase<EShotFieldSize> {

  /**
   * Constructor.
   */
  public ShotFieldSizeM5Model() {
    super( MID_SHOT_FIELD_SIZE, EShotFieldSize.class );
  }

}
