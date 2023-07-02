package com.hazard157.psx.proj3.mingle.impl;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.mingle.impl.IHzResources.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;

import java.time.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.incident.when.*;

/**
 * Helper methods to work with mingle data.
 *
 * @author hazard157
 */
public class MingleUtils {

  /**
   * Mingle ID {@link String} validator.
   */
  public static final ITsValidator<String> MINGLE_ID_STR_VALIDATOR = MingleUtils::validateMingleId;

  /**
   * Mingle ID {@link IAtomicValue} validator.
   */
  public static final ITsValidator<IAtomicValue> MINGLE_ID_AV_VALIDATOR = aValue -> {
    String epId = aValue.asString();
    return validateMingleId( epId );
  };

  /**
   * Character to start mingle ID with encoded date.
   * <p>
   * ID name with date looks like <b>"{@value #CHAR_MINGLE_ID_PREFIX}YYYY_MM_DD".</b>
   */
  public static final char CHAR_MINGLE_ID_PREFIX = 'g';

  /**
   * Validates mingle ID for correctness and returns the result.
   *
   * @param aMingleId String - mingle ID to be checked
   * @return {@link ValidationResult} - the validationresult
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult validateMingleId( String aMingleId ) {
    ValidationResult vr = doValidateMingleId( aMingleId );
    if( vr.isOk() ) {
      LocalDate ld = doGetMingleLocalDate( aMingleId );
      return IncidentDateValidator.VALIDATOR.validate( ld );
    }
    return vr;
  }

  /**
   * Creates mingle ID from date.
   *
   * @param aMingleDate {@link LocalDate} - the date
   * @return String - mingle ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException date is out of range
   */
  public static String localDate2MingleId( LocalDate aMingleDate ) {
    TsNullArgumentRtException.checkNull( aMingleDate );
    if( aMingleDate.isBefore( MIN_PSX_DATE ) || aMingleDate.isAfter( MAX_PSX_DATE ) ) {
      throw new TsIllegalArgumentRtException();
    }
    return String.format( "%c%04d_%02d_%02d", Character.valueOf( CHAR_MINGLE_ID_PREFIX ), //$NON-NLS-1$
        Integer.valueOf( aMingleDate.getYear() ), Integer.valueOf( aMingleDate.getMonthValue() ),
        Integer.valueOf( aMingleDate.getDayOfMonth() ) );
  }

  /**
   * Extracts date {@link LocalDate} from the mingle ID.
   *
   * @param aMingleId String - the mingle ID
   * @return {@link LocalDate} - extracted date
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid mingle ID
   * @throws TsIllegalArgumentRtException date is out of range
   */
  public static LocalDate mingleId2LocalDate( String aMingleId ) {
    MINGLE_ID_STR_VALIDATOR.checkValid( aMingleId );
    return doGetMingleLocalDate( aMingleId );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static ValidationResult doValidateMingleId( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    if( aId.length() < 11 ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_TOO_SHORT, aId );
    }
    if( aId.length() > 11 ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_TOO_LONG, aId );
    }
    if( aId.charAt( 0 ) != CHAR_MINGLE_ID_PREFIX ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_INV_START, aId );
    }
    if( !isAsciiDigit( aId.charAt( 1 ) ) || !isAsciiDigit( aId.charAt( 2 ) ) || !isAsciiDigit( aId.charAt( 3 ) )
        || !isAsciiDigit( aId.charAt( 4 ) ) ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_FORMAT, aId );
    }
    if( aId.charAt( 5 ) != '_' ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_FORMAT, aId );
    }
    if( !isAsciiDigit( aId.charAt( 6 ) ) || !isAsciiDigit( aId.charAt( 7 ) ) ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_FORMAT, aId );
    }
    if( aId.charAt( 8 ) != '_' ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_FORMAT, aId );
    }
    if( !isAsciiDigit( aId.charAt( 9 ) ) || !isAsciiDigit( aId.charAt( 10 ) ) ) {
      return ValidationResult.error( FMT_ERR_INV_MINGLE_ID_FORMAT, aId );
    }
    return ValidationResult.SUCCESS;
  }

  private static LocalDate doGetMingleLocalDate( String aMingleId ) {
    int year = Integer.parseInt( aMingleId.substring( 1, 5 ) );
    int month = Integer.parseInt( aMingleId.substring( 6, 8 ) );
    int day = Integer.parseInt( aMingleId.substring( 9 ) );
    return LocalDate.of( year, month, day );
  }

  /**
   * No subclassing.
   */
  private MingleUtils() {
    // nop
  }

}
