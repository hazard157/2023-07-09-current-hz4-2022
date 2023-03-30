package com.hazard157.psx.proj3.gaze.impl;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.gaze.impl.IHzResources.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;

import java.time.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.ng.incident.*;

/**
 * Helper methods to work with gaze data.
 *
 * @author goga
 */
public class GazeUtils {

  /**
   * Gaze ID {@link String} validator.
   */
  public static final ITsValidator<String> GAZE_ID_STR_VALIDATOR = GazeUtils::validateGazeId;

  /**
   * Gaze ID {@link IAtomicValue} validator.
   */
  public static final ITsValidator<IAtomicValue> GAZE_ID_AV_VALIDATOR = aValue -> {
    String epId = aValue.asString();
    return validateGazeId( epId );
  };

  /**
   * Character to start gaze ID with encoded date.
   * <p>
   * ID name with date looks like <b>"{@value #CHAR_GAZE_ID_PREFIX}YYYY_MM_DD".</b>
   */
  public static final char CHAR_GAZE_ID_PREFIX = 'g';

  /**
   * Validates gaze ID for correctness and returns the result.
   *
   * @param aGazeId String - gaze ID to be checked
   * @return {@link ValidationResult} - the validationresult
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult validateGazeId( String aGazeId ) {
    ValidationResult vr = doValidateGazeId( aGazeId );
    if( vr.isOk() ) {
      LocalDate ld = doGetGazeLocalDate( aGazeId );
      return IncidentWhenValidator.VALIDATOR.validate( ld );
    }
    return vr;
  }

  /**
   * Creates gaze ID from date.
   *
   * @param aGazeDate {@link LocalDate} - the date
   * @return String - gaze ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException date is out of range
   */
  public static String localDate2GazeId( LocalDate aGazeDate ) {
    TsNullArgumentRtException.checkNull( aGazeDate );
    if( aGazeDate.isBefore( MIN_PSX_DATE ) || aGazeDate.isAfter( MAX_PSX_DATE ) ) {
      throw new TsIllegalArgumentRtException();
    }
    return String.format( "%c%04d_%02d_%02d", Character.valueOf( CHAR_GAZE_ID_PREFIX ), //$NON-NLS-1$
        Integer.valueOf( aGazeDate.getYear() ), Integer.valueOf( aGazeDate.getMonthValue() ),
        Integer.valueOf( aGazeDate.getDayOfMonth() ) );
  }

  /**
   * Extracts date {@link LocalDate} from the gaze ID.
   *
   * @param aGazeId String - the gaze ID
   * @return {@link LocalDate} - extracted date
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid gaze ID
   * @throws TsIllegalArgumentRtException date is out of range
   */
  public static LocalDate gazeId2LocalDate( String aGazeId ) {
    GAZE_ID_STR_VALIDATOR.checkValid( aGazeId );
    return doGetGazeLocalDate( aGazeId );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static ValidationResult doValidateGazeId( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    if( aId.length() < 11 ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_TOO_SHORT, aId );
    }
    if( aId.length() > 11 ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_TOO_LONG, aId );
    }
    if( aId.charAt( 0 ) != CHAR_GAZE_ID_PREFIX ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_INV_START, aId );
    }
    if( !isAsciiDigit( aId.charAt( 1 ) ) || !isAsciiDigit( aId.charAt( 2 ) ) || !isAsciiDigit( aId.charAt( 3 ) )
        || !isAsciiDigit( aId.charAt( 4 ) ) ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_FORMAT, aId );
    }
    if( aId.charAt( 5 ) != '_' ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_FORMAT, aId );
    }
    if( !isAsciiDigit( aId.charAt( 6 ) ) || !isAsciiDigit( aId.charAt( 7 ) ) ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_FORMAT, aId );
    }
    if( aId.charAt( 8 ) != '_' ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_FORMAT, aId );
    }
    if( !isAsciiDigit( aId.charAt( 9 ) ) || !isAsciiDigit( aId.charAt( 10 ) ) ) {
      return ValidationResult.error( FMT_ERR_INV_GAZE_ID_FORMAT, aId );
    }
    return ValidationResult.SUCCESS;
  }

  private static LocalDate doGetGazeLocalDate( String aGazeId ) {
    int year = Integer.parseInt( aGazeId.substring( 1, 5 ) );
    int month = Integer.parseInt( aGazeId.substring( 6, 8 ) );
    int day = Integer.parseInt( aGazeId.substring( 9 ) );
    return LocalDate.of( year, month, day );
  }

  /**
   * No subclassing.
   */
  private GazeUtils() {
    // nop
  }

}
