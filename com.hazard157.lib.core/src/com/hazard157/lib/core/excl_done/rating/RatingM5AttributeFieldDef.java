package com.hazard157.lib.core.excl_done.rating;

import static com.hazard157.lib.core.excl_done.rating.IRatingConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tslib.av.EAtomicType;

/**
 * {@link EAtomicType#VALOBJ} attribute field of type {@link ERating}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class RatingM5AttributeFieldDef<T>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public RatingM5AttributeFieldDef() {
    super( FID_RATING, DDEF_RATING );
    // FIXME OPDEF_EDITOR_FACTORY_NAME.setValue( params(), avStr( ValedAvValobjRadioPropEnumStars.EDITOR_NAME ) );
  }

}
