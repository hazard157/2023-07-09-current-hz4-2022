package com.hazard157.psx.proj3.incident;

import static com.hazard157.psx.proj3.incident.IPsxResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Validates {@link EAtomicType#STRING} to be syntactically valid and in time range ID of incident.
 *
 * @author hazard157
 */
class IncidentIdAvValidator
    implements ITsValidator<IAtomicValue> {

  private final ITsValidator<String> idValidator;

  /**
   * Constructor.
   *
   * @param aIdValidator {@link ITsValidator}&lt;String&gt; - String validator of incident ID
   */
  IncidentIdAvValidator( ITsValidator<String> aIdValidator ) {
    TsNullArgumentRtException.checkNull( aIdValidator );
    idValidator = aIdValidator;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.atomicType() != EAtomicType.STRING ) {
      return ValidationResult.error( FMT_ERR_INV_INC_AV_ID_NOT_STR, aValue.asString() );
    }
    return idValidator.validate( aValue.asString() );
  }

}
