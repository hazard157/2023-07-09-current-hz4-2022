package com.hazard157.lib.core.quants.haircol;

import static com.hazard157.lib.core.quants.haircol.IHairColorConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.*;

/**
 * M5-Model of {@link EHairColor}.
 *
 * @author hazard157
 */
public class HairColorM5Model
    extends M5StridableEnumModelBase<EHairColor> {

  /**
   * Constructor.
   */
  public HairColorM5Model() {
    super( MID_HAIR_COLOR, EHairColor.class );
  }

}
