package com.hazard157.psx.proj3.incident.when;

import static com.hazard157.psx.proj3.incident.when.IPrisexWhenConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Defines how to implement {@link IWhenable} for {@link IParameterized} entities.
 *
 * @author hazard157
 */
public interface IParamsWhenable
    extends IParameterized, IWhenable {

  @Override
  default LocalDateTime when() {
    return OPDEF_WHEN.getValue( params() ).asValobj();
  }

  /**
   * Sets the timestamp to the specified entity params.
   *
   * @param aEntityParams {@link IOptionSetEdit} - the entity params
   * @param aValue {@link LocalDateTime} - the value to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static void setInterval( IOptionSetEdit aEntityParams, LocalDateTime aValue ) {
    TsNullArgumentRtException.checkNulls( aEntityParams, aValue );
    aEntityParams.setValobj( OPDEF_WHEN, aValue );
  }

}
