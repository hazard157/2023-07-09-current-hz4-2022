package com.hazard157.lib.core.quants.vulgar;

import static com.hazard157.lib.core.quants.vulgar.IVulgarnessConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tslib.av.EAtomicType;

/**
 * {@link EAtomicType#VALOBJ} attribute field of type {@link EVulgarness}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class VulgarnessM5AttributeFieldDef<T>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public VulgarnessM5AttributeFieldDef() {
    super( FID_VULGARNESS, DDEF_VULGARNESS );
    // FIXME OPDEF_EDITOR_FACTORY_NAME.setValue( params(), avStr( ValedAvValobjRadioPropEnumStars.EDITOR_NAME ) );
  }

}
