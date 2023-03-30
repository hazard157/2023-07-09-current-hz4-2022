package com.hazard157.lib.core.quants.zodsign;

import static com.hazard157.lib.core.quants.zodsign.IZodiacSignConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.*;

/**
 * M5-Model of {@link EZodiacSign}.
 *
 * @author hazard157
 */
public class ZodiacSignM5Model
    extends M5StridableEnumModelBase<EZodiacSign> {

  /**
   * Constructor.
   */
  public ZodiacSignM5Model() {
    super( MID_ZODIAC_SIGN, EZodiacSign.class );
  }

}
