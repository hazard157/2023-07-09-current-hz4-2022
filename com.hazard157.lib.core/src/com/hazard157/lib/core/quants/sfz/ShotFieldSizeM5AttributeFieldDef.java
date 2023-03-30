package com.hazard157.lib.core.quants.sfz;

import static com.hazard157.lib.core.quants.sfz.IShotFieldSizeConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tslib.av.EAtomicType;

/**
 * {@link EAtomicType#VALOBJ} attribute field of type {@link EShotFieldSize}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class ShotFieldSizeM5AttributeFieldDef<T>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public ShotFieldSizeM5AttributeFieldDef() {
    super( FID_SHOT_FIELD_SIZE, DDEF_SHOT_FIELD_SIZE );
    // FIXME OPDEF_EDITOR_FACTORY_NAME.setValue( params(), avStr( ValedAvValobjRadioPropEnumStars.EDITOR_NAME ) );
  }

}
