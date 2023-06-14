package com.hazard157.psx.proj3.incident.when;

import static com.hazard157.psx.proj3.incident.when.IPrisexWhenConstants.*;
import static com.hazard157.psx.proj3.incident.when.IPsxResources.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Validates {@link LocalDateTime} to have valid value as incident starting time.
 *
 * @author hazard157
 */
class IncidentWhenValidator
    implements ITsValidator<LocalDateTime> {

  /**
   * The validator instance.
   */
  public static final ITsValidator<LocalDateTime> VALIDATOR = new IncidentWhenValidator();

  /**
   * Constructor.
   */
  IncidentWhenValidator() {
    // nop
  }

  @Override
  public ValidationResult validate( LocalDateTime aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    LocalDate ld = aValue.toLocalDate();
    if( ld.compareTo( MIN_PSX_DATE ) < 0 || ld.compareTo( MAX_PSX_DATE ) > 0 ) {
      return ValidationResult.error( FMT_ERR_INV_INCIDENT_WHEN, ld.toString() );
    }
    return ValidationResult.SUCCESS;
  }

}
