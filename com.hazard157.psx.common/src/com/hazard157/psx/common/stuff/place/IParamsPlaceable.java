package com.hazard157.psx.common.stuff.place;

import static com.hazard157.psx.common.stuff.place.IPlaceableConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Defines how to implement {@link IPlaceable} for {@link IParameterized} entities.
 *
 * @author hazard157
 */
public interface IParamsPlaceable
    extends IPlaceable, IParameterized {

  @Override
  default String place() {
    return DDEF_PLACE.getValue( params() ).asString();
  }

  /**
   * Sets the place to the specified entity params.
   *
   * @param aEntityParams {@link IOptionSetEdit} - the entity params
   * @param aValue {@link IStringList} - the value to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static void setRating( IOptionSetEdit aEntityParams, IStringList aValue ) {
    TsNullArgumentRtException.checkNulls( aEntityParams, aValue );
    aEntityParams.setValobj( DDEF_PLACE, aValue );
  }

}
