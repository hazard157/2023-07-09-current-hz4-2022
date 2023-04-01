package com.hazard157.psx.proj3.ng.incident;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.ng.incident.IPsxResources.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Validator of the incident starting timestamp as {@link LocalDate}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class IncidentWhenValidator
    implements ITsValidator<LocalDate> {

  public static final ITsValidator<LocalDate> VALIDATOR = new IncidentWhenValidator();

  private IncidentWhenValidator() {
    // nop
  }

  @Override
  public ValidationResult validate( LocalDate aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.isBefore( MIN_PSX_DATE ) || aValue.isAfter( MAX_PSX_DATE ) ) {
      return ValidationResult.error( FMT_ERR_INV_INCIDENT_DATE, aValue.toString() );
    }
    return ValidationResult.SUCCESS;
  }

}
