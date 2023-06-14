package com.hazard157.psx.proj3.incident.when;

import static com.hazard157.psx.proj3.incident.when.IPrisexWhenConstants.*;
import static com.hazard157.psx.proj3.incident.when.IPsxResources.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Validates {@link LocalDate} to have valid value as incident starting time.
 *
 * @author hazard157
 */
public class IncidentDateValidator
    implements ITsValidator<LocalDate> {

  /**
   * The validator instance.
   */
  public static final ITsValidator<LocalDate> VALIDATOR = new IncidentDateValidator();

  /**
   * Constructor.
   */
  IncidentDateValidator() {
    // nop
  }

  @Override
  public ValidationResult validate( LocalDate aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.compareTo( MIN_PSX_DATE ) < 0 || aValue.compareTo( MAX_PSX_DATE ) > 0 ) {
      return ValidationResult.error( FMT_ERR_INV_INCIDENT_DATE, aValue.toString() );
    }
    return ValidationResult.SUCCESS;
  }

}
