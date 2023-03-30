package com.hazard157.lib.core.quants.eyecol;

import static com.hazard157.lib.core.quants.eyecol.IEyeColorConstants.*;
import static com.hazard157.lib.core.quants.eyecol.IHzResources.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.*;

/**
 * M5-Model of {@link EEyeColor}.
 *
 * @author hazard157
 */
public class EyeColorM5Model
    extends M5StridableEnumModelBase<EEyeColor> {

  /**
   * Constructor.
   */
  public EyeColorM5Model() {
    super( MID_EYE_COLOR, EEyeColor.class );
    setNameAndDescription( STR_N_M5M_EYE_COLOR, STR_D_M5M_EYE_COLOR );
  }

}
