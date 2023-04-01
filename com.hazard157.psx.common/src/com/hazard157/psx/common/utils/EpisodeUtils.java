package com.hazard157.psx.common.utils;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.utils.IHzResources.*;

import java.time.*;
import java.util.*;
import java.util.regex.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.*;

/**
 * Common stuff to work with episodes.
 *
 * @author hazard157
 */
public class EpisodeUtils {

  /**
   * Символ, с которого начинтаеся ИД-имя с закодированной датой.
   * <p>
   * ИД-имя с датой имеет вид <b>"{@value #CHAR_EPISODE_ID_PREFIX}YYYY_MM_DD".</b>
   */
  public static final char CHAR_EPISODE_ID_PREFIX = 'e';

  /**
   * Идентификатор заведомонесуществующего эпизода.
   * <p>
   * Идентификатор имеет валидный формат и допустимую дату.
   */
  public static final String EPISODE_ID_NONE = "e1995_01_01"; //$NON-NLS-1$

  /**
   * Отображение неверной даты в {@link #ymdFromTime(long)}.
   */
  public static final String UNKNOWN_DATE_STR = "????-??-??"; //$NON-NLS-1$

  private static final Calendar defaultCalendar = Calendar.getInstance();

  static final int EPISODE_ID_LENGTH = EPISODE_ID_NONE.length();

  static final Pattern EPISODE_ID_PATTERN =
      Pattern.compile( "e(199\\d|2\\d\\d\\d)_(0[1-9]|10|11|12)_(0[1-9]|1[0-9]|2[0-9]|30|31)" ); //$NON-NLS-1$

  /**
   * Episode datetime {@link LocalDateTime} validator.
   * <p>
   * Checks if episode date is in range {@link IPsxHardConstants#MIN_PSX_DATE} ..
   * {@link IPsxHardConstants#MAX_PSX_DATE}.
   */
  public static final ITsValidator<LocalDateTime> EPISODE_WHEN_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    LocalDate ld = aValue.toLocalDate();
    if( ld.compareTo( MIN_PSX_DATE ) < 0 || ld.compareTo( MAX_PSX_DATE ) > 0 ) {
      return ValidationResult.error( FMT_ERR_INV_PSX_DATE, ld.toString() );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Episode date {@link LocalDate} validator.
   * <p>
   * Checks if episode date is in range {@link IPsxHardConstants#MIN_PSX_DATE} ..
   * {@link IPsxHardConstants#MAX_PSX_DATE}.
   */
  public static final ITsValidator<LocalDate> EPISODE_DATE_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.compareTo( MIN_PSX_DATE ) < 0 || aValue.compareTo( MAX_PSX_DATE ) > 0 ) {
      return ValidationResult.error( FMT_ERR_INV_PSX_DATE, aValue.toString() );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Episode identifier validator.
   * <p>
   * Checks if episode identifier matches 'eYYYY-MM-DD' format and then checks date with
   * {@link #EPISODE_DATE_VALIDATOR}.
   */
  public static final ITsValidator<String> EPISODE_ID_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.length() == EPISODE_ID_LENGTH ) {
      if( EPISODE_ID_PATTERN.matcher( aValue ).matches() ) {
        LocalDate episodeDate = internalId2date( aValue );
        return EPISODE_DATE_VALIDATOR.validate( episodeDate );
      }
    }
    return ValidationResult.error( FMT_ERR_INV_EP_ID, aValue );
  };

  /**
   * Извлекате дату из идентификатора эпизода.
   *
   * @param aEpisodeId String - идентифиукатор эпизода
   * @return {@link LocalDate} - дата эпзода
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка {@link #EPISODE_ID_VALIDATOR}
   */
  public static LocalDate id2date( String aEpisodeId ) {
    EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    int year = Integer.parseInt( aEpisodeId.substring( 1, 5 ) );
    int month = Integer.parseInt( aEpisodeId.substring( 6, 8 ) );
    int dayOfMon = Integer.parseInt( aEpisodeId.substring( 9 ) );
    return LocalDate.of( year, month, dayOfMon );
  }

  /**
   * Извлекает дату {@link LocalDate} из идентификатора эпизода.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @return {@link LocalDate} - дата эпизода (в мсек с начала эпохи), на момент 00:00:00 даты
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат идентификатора эпизода
   * @throws TsIllegalArgumentRtException дата эпизода выходит за допустимые пределы
   */
  public static LocalDate episodeId2LocalDate( String aEpisodeId ) {
    EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    return internalGetEpisodeLocalDate( aEpisodeId );
  }

  /**
   * Создает ИД-имя эпизода из даты.
   *
   * @param aEpisodeDate {@link LocalDate} - дата в пределах {@link IPsxHardConstants#MIN_PSX_DATE} ..
   *          {@link IPsxHardConstants#MAX_PSX_DATE}
   * @return String - ИД-имя вида "eYYYY_MM_DD"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException дата выходит за допустимые пределы
   */
  public static String localDate2EpisodeId( LocalDate aEpisodeDate ) {
    TsNullArgumentRtException.checkNull( aEpisodeDate );
    if( aEpisodeDate.isBefore( MIN_PSX_DATE ) || aEpisodeDate.isAfter( MAX_PSX_DATE ) ) {
      throw new TsIllegalArgumentRtException();
    }
    return String.format( "%c%04d_%02d_%02d", Character.valueOf( CHAR_EPISODE_ID_PREFIX ), //$NON-NLS-1$
        Integer.valueOf( aEpisodeDate.getYear() ), Integer.valueOf( aEpisodeDate.getMonthValue() ),
        Integer.valueOf( aEpisodeDate.getDayOfMonth() ) );
  }

  /**
   * Возвращает строку вида "YYYY-MM-DD" из метки времени.
   * <p>
   * Метод является потоко-безопасным. Если метка времени выходит за допустимые пределы, возвращает
   * {@link #UNKNOWN_DATE_STR}.
   *
   * @param aTimestamp long - метка времени в пределах {@link IPsxHardConstants#MIN_TIMESTAMP} ..
   *          {@link IPsxHardConstants#MAX_TIMESTAMP}
   * @return String - строка вида "YYYY-MM-DD"
   */
  public static String ymdFromTime( long aTimestamp ) {
    if( aTimestamp < MIN_TIMESTAMP || aTimestamp > MAX_TIMESTAMP ) {
      return UNKNOWN_DATE_STR;
    }
    synchronized (defaultCalendar) {
      defaultCalendar.setTimeInMillis( aTimestamp );
      Integer year = Integer.valueOf( defaultCalendar.get( Calendar.YEAR ) );
      Integer month = Integer.valueOf( defaultCalendar.get( Calendar.MONTH ) + 1 );
      Integer day = Integer.valueOf( defaultCalendar.get( Calendar.DAY_OF_MONTH ) );
      return String.format( "%04d-%02d-%02d", year, month, day ); //$NON-NLS-1$
    }
  }

  /**
   * Возвращает строку вида "YYYY-MM-DD" из идентификатор эпизода.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @return String - строка вида "YYYY-MM-DD"
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент невалидный идентификатор эпизода
   */
  public static String ymdFromId( String aEpisodeId ) {
    if( EPISODE_ID_VALIDATOR.isValid( aEpisodeId ) ) {
      int year = Integer.parseInt( aEpisodeId.substring( 1, 5 ) );
      int month = Integer.parseInt( aEpisodeId.substring( 6, 8 ) );
      int day = Integer.parseInt( aEpisodeId.substring( 9 ) );
      defaultCalendar.setTimeInMillis( 0 );
      defaultCalendar.set( Calendar.YEAR, year );
      defaultCalendar.set( Calendar.MONTH, month - 1 );
      defaultCalendar.set( Calendar.DAY_OF_MONTH, day );
      return ymdFromTime( defaultCalendar.getTimeInMillis() );
    }
    return UNKNOWN_DATE_STR;
  }

  /**
   * Определяет, находится ли дата эпизода в допустимых пределах.
   *
   * @param aWhen long - дата эпизода (в мсек с начала эпохи)
   * @return boolean - признак, что дата в допустимых пределах
   */
  public static boolean isValidWhen( long aWhen ) {
    return aWhen >= MIN_TIMESTAMP && aWhen <= MAX_TIMESTAMP;
  }

  /**
   * Проверяет строковое представление даты эпизода на корректность.
   *
   * @param aEpisodeDate String - строковое представление даты эпизода вида YYYY-MM-DD
   * @return boolean - признак корректности идентификатора эпизода
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isValidEpisodeDateStr( String aEpisodeDate ) {
    TsNullArgumentRtException.checkNull( aEpisodeDate );
    try {
      LocalDate epDate = LocalDate.parse( aEpisodeDate );
      return EPISODE_DATE_VALIDATOR.isValid( epDate );
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      // nop
    }
    return false;
  }

  /**
   * Проверяет нахождение даты эпизода в допустимых пределах с выбрасываением исключения.
   *
   * @param aWhen long - дата эпизода (в мсек с начала эпохи)
   * @throws TsIllegalArgumentRtException дата выходит за допустимые пределы
   */
  public static void checkWhen( long aWhen ) {
    if( !isValidWhen( aWhen ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_WHEN, Long.valueOf( aWhen ) );
    }
  }

  /**
   * Создает ИД-имя эпизода из метки времени.
   *
   * @param aWhen long - метка времени в пределах {@link IPsxHardConstants#MIN_TIMESTAMP} ..
   *          {@link IPsxHardConstants#MAX_TIMESTAMP}
   * @return String - ИД-имя вида "eYYYY_MM_DD"
   * @throws TsIllegalArgumentRtException метка времени выходит за допустимые пределы
   */
  @SuppressWarnings( "boxing" )
  public static String when2EpisodeId( long aWhen ) {
    checkWhen( aWhen );
    Instant instant = Instant.ofEpochMilli( aWhen );
    LocalDate ld = LocalDate.ofInstant( instant, ZoneId.systemDefault() );
    return String.format( "%c%04d_%02d_%02d", Character.valueOf( CHAR_EPISODE_ID_PREFIX ), //$NON-NLS-1$
        ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  static LocalDate internalId2date( String aEpisodeId ) {
    int year = Integer.parseInt( aEpisodeId.substring( 1, 5 ) );
    int month = Integer.parseInt( aEpisodeId.substring( 6, 8 ) );
    int day = Integer.parseInt( aEpisodeId.substring( 9 ) );
    return LocalDate.of( year, month, day );
  }

  private static LocalDate internalGetEpisodeLocalDate( String aEpisodeId ) {
    int year = Integer.parseInt( aEpisodeId.substring( 1, 5 ) );
    int month = Integer.parseInt( aEpisodeId.substring( 6, 8 ) );
    int day = Integer.parseInt( aEpisodeId.substring( 9 ) );
    return LocalDate.of( year, month, day );
  }

  /**
   * Prihibit instance creation.
   */
  private EpisodeUtils() {
    // nop
  }

}
