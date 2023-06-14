package com.hazard157.psx.proj3.incident;

import static com.hazard157.psx.proj3.incident.IPrisexIncidentConstants.*;
import static com.hazard157.psx.proj3.incident.IPsxResources.*;

import java.time.*;
import java.util.regex.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.incident.when.*;

/**
 * Validates {@link String} to be syntactically valid PRISEX incident ID.
 * <p>
 * Incident ID has format "cYYYY_DD_MM" where character 'c' is different for different kinds of incident ('e' fir
 * episode, 'g' - for gaze, and more kinds may be added).
 *
 * @author hazard157
 */
class IncidentIdValidator
    implements ITsValidator<String> {

  private static final int INCIDENT_ID_LENGTH = GENERIC_INCIDENT_ID.length();

  private static final String DATE_PART_PATTERN = "(199\\d|2\\d\\d\\d)_(0[1-9]|10|11|12)_(0[1-9]|1[0-9]|2[0-9]|30|31)"; //$NON-NLS-1$

  private final boolean allowNonId;
  private final Pattern idPattern;

  /**
   * Constructor.
   *
   * @param aPrefixChar char - the first character of the incident ID
   * @param aAllowNullId - <code>true</code> to allow {@link IStridable#NONE_ID}
   */
  IncidentIdValidator( char aPrefixChar, boolean aAllowNullId ) {
    allowNonId = aAllowNullId;
    String regex = aPrefixChar + DATE_PART_PATTERN;
    idPattern = Pattern.compile( regex );
  }

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( allowNonId ) {
      if( aValue.equals( IStridable.NONE_ID ) ) {
        return ValidationResult.SUCCESS;
      }
    }
    if( aValue.length() == INCIDENT_ID_LENGTH ) {
      if( idPattern.matcher( aValue ).matches() ) {
        LocalDate incidentDate = EPsxIncidentKind.internalIncidentId2date( aValue );
        return IncidentDateValidator.VALIDATOR.validate( incidentDate );
      }
    }
    return ValidationResult.error( FMT_ERR_INV_INCIDENT_ID, aValue );
  }

}
