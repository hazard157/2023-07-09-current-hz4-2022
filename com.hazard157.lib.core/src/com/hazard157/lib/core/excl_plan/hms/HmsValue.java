package com.hazard157.lib.core.excl_plan.hms;

import static com.hazard157.lib.core.excl_plan.hms.ITsResources.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;

import java.util.regex.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IHmsValue}.
 *
 * @author hazard157
 */
public final class HmsValue
    implements IHmsValue {

  // private static final String TEMPLATE_MMSS = "[ ]*\\d{1,2}[\\p{Punct}} ][0-5]?\\d[ ]*"; //$NON-NLS-1$ //
  // private static final String TEMPLATE_HHMMSS = "[ ]*\\d{1,2}[\\p{Punct}} ][0-5]?\\d[\\p{Punct}} ][0-5]?\\d[ ]*";
  // //$NON-NLS-1$

  private static final String  TEMPLATE_MMSS_ANY      = "[0-5][0-9].[0-9][0-9]";                  //$NON-NLS-1$
  private static final String  TEMPLATE_HHMMSS_ANY    = "[0-5][0-9].[0-9][0-9].[0-9][0-9]";       //$NON-NLS-1$
  private static final String  TEMPLATE_MMSS_STRICT   = "[0-5][0-9]:[0-9][0-9]";                  //$NON-NLS-1$
  private static final String  TEMPLATE_HHMMSS_STRICT = "[0-5][0-9]:[0-9][0-9]:[0-9][0-9]";       //$NON-NLS-1$
  private static final Pattern patternMmSsAny         = Pattern.compile( TEMPLATE_MMSS_ANY );
  private static final Pattern patternHhMmSsAny       = Pattern.compile( TEMPLATE_HHMMSS_ANY );
  private static final Pattern patternMmSsStrict      = Pattern.compile( TEMPLATE_MMSS_STRICT );
  private static final Pattern patternHhMmSsStrict    = Pattern.compile( TEMPLATE_HHMMSS_STRICT );

  protected GenericChangeEventer eventer;

  private Pattern patternMmSs;
  private Pattern patternHhMmSs;
  private boolean isStrict;

  private boolean hoursUsed;
  private int     minValue;
  private int     maxValue;
  private String  minValueText;
  private String  maxValueText;

  private int value = 0;

  /**
   * Конструктор с начальными значениями.
   *
   * @param aHoursUsed boolean - текстовое представление использует значение часов
   * @param aMinValue int - минимально допустимое значение в секундах
   * @param aMaxValue int - максимально допустимое значение в секундах
   * @param aStrictMode boolean - точный режим, разрешаются разделители только символ ':', иначе - любой
   * @throws TsIllegalArgumentRtException aMinValue < 0
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   */
  public HmsValue( boolean aHoursUsed, int aMinValue, int aMaxValue, boolean aStrictMode ) {
    eventer = new GenericChangeEventer( this );
    configure( aHoursUsed, aMinValue, aMaxValue, aStrictMode );
  }

  /**
   * Конструктор с указанием формата.
   * <p>
   * Максимальное значение устанавливается (в зависимости от формата) в {@link HmsUtils#MAX_HHMMSS_VALUE} или
   * {@link HmsUtils#MAX_MMSS_VALUE}. Минимальное значение - 0.
   *
   * @param aHoursUsed boolean - текстовое представление использует значение часов
   * @param aStrictMode boolean - точный режим, разрешаются разделители только символ ':', иначе - любой
   * @throws TsIllegalArgumentRtException aMinValue < 0
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   */
  public HmsValue( boolean aHoursUsed, boolean aStrictMode ) {
    this( aHoursUsed, 0, aHoursUsed ? MAX_HHMMSS_VALUE : MAX_MMSS_VALUE, aStrictMode );
  }

  /**
   * Конструктор по умолчанию.
   * <p>
   * Просто вызывает конструктор {@link HmsValue#HmsValue(boolean, boolean) HmsValue(<b>true</b>,<b>true</b>)}.
   */
  public HmsValue() {
    this( true, true );
  }

  /**
   * Конструктор копирования.
   * <p>
   * Копирует также настройки, задаваемые методом {@link IHmsValue#configure(boolean, int, int, boolean)}.
   *
   * @param aSource {@link IHmsValue} - исходное значение
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public HmsValue( IHmsValue aSource ) {
    this();
    TsNullArgumentRtException.checkNull( aSource );
    configure( aSource.isHoursUsed(), aSource.minValue(), aSource.maxValue(), aSource.isStrictMode() );
    setValue( aSource.getValue() );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private int internalGetPartValue( EHmsPart aPart ) {
    switch( aPart ) {
      case HH:
        return getValue() / 3600;
      case MM:
        return (getValue() % 3600) / 60;
      case SS:
        return getValue() % 60;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  private void internalSetValue( int aValue ) {
    TsValidationFailedRtException.checkError( validateValue( aValue ) );
    if( value != aValue ) {
      value = aValue;
      eventer.fireChangeEvent();
    }
  }

  private int getMaxHh() {
    if( hoursUsed ) {
      return MAX_HHH;
    }
    return 0;
  }

  private int getMaxMm() {
    if( hoursUsed ) {
      return 59;
    }
    return MAX_MMM;
  }

  // private static int parseAsMmSs( String aText ) {
  // ICharInputStream chIn = new CharInputStreamString( aText );
  // IStridReader sr = new StridReader( chIn );
  // int mm = sr.readInt();
  // sr.nextChar( ESkipMode.SKIP_NONE ); // а не sr.ensureChar( ':' );
  // int ss = sr.readInt();
  // return 60 * mm + ss;
  // }
  //
  // private static int parseAsHhMmSs( String aText ) {
  // ICharInputStream chIn = new CharInputStreamString( aText );
  // IStridReader sr = new StridReader( chIn );
  // int hh = sr.readInt();
  // sr.nextChar( ESkipMode.SKIP_NONE ); // а не sr.ensureChar( ':' );
  // int mm = sr.readInt();
  // sr.nextChar( ESkipMode.SKIP_NONE ); // а не sr.ensureChar( ':' );
  // int ss = sr.readInt();
  // return 3600 * hh + 60 * mm + ss;
  // }

  private static int parseAsMmSs( String aText ) {
    int mm = Integer.parseInt( aText.substring( 0, 2 ) );
    int ss = Integer.parseInt( aText.substring( 3 ) );
    return 60 * mm + ss;
  }

  private static int parseAsHhMmSs( String aText ) {
    int hh = Integer.parseInt( aText.substring( 0, 2 ) );
    int mm = Integer.parseInt( aText.substring( 3, 5 ) );
    int ss = Integer.parseInt( aText.substring( 6 ) );
    return 3600 * hh + 60 * mm + ss;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( IHmsValue aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return Integer.compare( this.getValue(), aThat.getValue() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IHmsValue
  //

  @Override
  public boolean isHoursUsed() {
    return hoursUsed;
  }

  @Override
  public int minValue() {
    return minValue;
  }

  @Override
  public int maxValue() {
    return maxValue;
  }

  @Override
  public boolean isStrictMode() {
    return isStrict;
  }

  @Override
  public void configure( boolean aHoursUsed, int aMinValue, int aMaxValue, boolean aStrictMode ) {
    // режим
    isStrict = aStrictMode;
    if( isStrict ) {
      patternMmSs = patternMmSsStrict;
      patternHhMmSs = patternHhMmSsStrict;
    }
    else {
      patternMmSs = patternMmSsAny;
      patternHhMmSs = patternHhMmSsAny;
    }
    // конфигурируем
    hoursUsed = aHoursUsed;
    TsIllegalArgumentRtException.checkTrue( aMinValue < 0, FMT_ERR_MIN_VAL_LT_0, Integer.valueOf( aMinValue ) );
    TsIllegalArgumentRtException.checkTrue( aMaxValue < 0, FMT_ERR_MAX_VAL_LT_0, Integer.valueOf( aMaxValue ) );
    int maxMax = aHoursUsed ? MAX_HHMMSS_VALUE : MAX_MMSS_VALUE;
    String maxMaxText = hhmmss( maxMax );
    TsIllegalArgumentRtException.checkTrue( aMinValue > maxMax, FMT_ERR_MIN_VAL_GT_MAX, Integer.valueOf( aMinValue ),
        maxMaxText );
    TsIllegalArgumentRtException.checkTrue( aMaxValue > maxMax, FMT_ERR_MAX_VAL_GT_MAX, Integer.valueOf( aMaxValue ),
        maxMaxText );
    minValue = aMinValue;
    maxValue = aMaxValue;
    if( hoursUsed ) {
      minValueText = hhhmmss( minValue );
      maxValueText = hhhmmss( maxValue );
    }
    else {
      minValueText = mmmss( minValue );
      maxValueText = mmmss( maxValue );
    }
    // если значение остается в пределах, не меняем его
    int val = value;
    if( val >= minValue && val <= maxValue ) {
      return;
    }
    // загоняем значение в пределы
    if( val < minValue ) {
      val = minValue;
    }
    if( val > maxValue ) {
      val = maxValue;
    }
    internalSetValue( val );
  }

  @Override
  public String maxValueText() {
    return maxValueText;
  }

  @Override
  public String minValueText() {
    return minValueText;
  }

  @Override
  public String getText() {
    if( hoursUsed ) {
      return hhhmmss( value );
    }
    return mmmss( value );
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public int getPartValue( EHmsPart aPart ) {
    TsNullArgumentRtException.checkNull( aPart );
    return internalGetPartValue( aPart );
  }

  @Override
  public void setValue( int aValue ) {
    internalSetValue( aValue );
  }

  @Override
  public void setValue( int aHh, int aMm, int aSs ) {
    TsIllegalArgumentRtException.checkTrue( aHh < 0 || aMm < 0 || aSs < 0 );
    TsIllegalArgumentRtException.checkTrue( aHh > getMaxHh() );
    TsIllegalArgumentRtException.checkTrue( aMm > getMaxMm() );
    int val = 3600 * aHh + 60 * aMm + aSs;
    internalSetValue( val );
  }

  @Override
  public void setPartValue( EHmsPart aPart, int aValue ) {
    TsNullArgumentRtException.checkNull( aPart );
    TsIllegalArgumentRtException.checkTrue( aValue < 0 );
    int hh = getPartValue( EHmsPart.HH );
    int mm = getPartValue( EHmsPart.MM );
    int ss = getPartValue( EHmsPart.SS );
    switch( aPart ) {
      case HH:
        TsIllegalArgumentRtException.checkTrue( aValue > getMaxHh() );
        hh = aValue;
        break;
      case MM:
        TsIllegalArgumentRtException.checkTrue( aValue > getMaxMm() );
        mm = aValue;
        break;
      case SS:
        TsIllegalArgumentRtException.checkTrue( aValue > 59 );
        ss = aValue;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    int val = 3600 * hh + 60 * mm + ss;
    internalSetValue( val );
  }

  @Override
  public void changePartValue( EHmsPart aPart, int aDelta ) {
    TsNullArgumentRtException.checkNull( aPart );
    TsIllegalArgumentRtException.checkTrue( !hoursUsed && aPart == EHmsPart.HH );
    long hh = getPartValue( EHmsPart.HH );
    long mm = getPartValue( EHmsPart.MM );
    long ss = getPartValue( EHmsPart.SS );
    switch( aPart ) {
      case HH:
        hh += aDelta;
        if( hh < 0 ) {
          hh = 0;
        }
        if( hh > getMaxHh() ) {
          hh = getMaxHh();
        }
        break;
      case MM:
        mm += aDelta;
        if( mm < 0 ) {
          mm = 0;
        }
        if( mm > getMaxMm() ) {
          mm = getMaxMm();
        }
        break;
      case SS:
        ss += aDelta;
        if( ss < 0 ) {
          ss = 0;
        }
        if( ss > 59 ) {
          ss = 59;
        }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    int val = (int)(3600 * hh + 60 * mm + ss);
    internalSetValue( val );
  }

  @Override
  public void changeValue( EHmsPart aPart, int aDelta ) {
    TsNullArgumentRtException.checkNull( aPart );
    TsIllegalArgumentRtException.checkTrue( !hoursUsed && aPart == EHmsPart.HH );
    long hh = getPartValue( EHmsPart.HH );
    long mm = getPartValue( EHmsPart.MM );
    long ss = getPartValue( EHmsPart.SS );
    switch( aPart ) {
      case HH:
        hh += aDelta;
        break;
      case MM:
        mm += aDelta;
        break;
      case SS:
        ss += aDelta;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    long val = 3600 * hh + 60 * mm + ss;
    if( val < minValue ) {
      val = minValue;
    }
    if( val > maxValue ) {
      val = maxValue;
    }
    internalSetValue( (int)val );
  }

  @Override
  public void changeValue( int aDelta ) {
    int val = getValue() + aDelta;
    if( val < minValue ) {
      val = minValue;
    }
    if( val > maxValue ) {
      val = maxValue;
    }
    internalSetValue( val );
  }

  @Override
  public void setValue( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    int val = -1;
    if( patternMmSs.matcher( aText ).matches() ) {
      val = parseAsMmSs( aText );
    }
    else {
      if( patternHhMmSs.matcher( aText ).matches() ) {
        val = parseAsHhMmSs( aText );
      }
      else {
        throw new TsIllegalArgumentRtException( ERR_MSG_INV_TEXT, aText );
      }
    }
    TsValidationFailedRtException.checkError( validateValue( val ) );
    internalSetValue( val );
  }

  @Override
  public ValidationResult validateText( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    int val = -1;
    if( patternMmSs.matcher( aText ).matches() ) {
      val = parseAsMmSs( aText );
    }
    else {
      if( patternHhMmSs.matcher( aText ).matches() ) {
        val = parseAsHhMmSs( aText );
      }
      else {
        return ValidationResult.error( ERR_MSG_INV_TEXT, aText );
      }
    }
    return validateValue( val );
  }

  @SuppressWarnings( "boxing" )
  @Override
  public ValidationResult validateValue( int aValue ) {
    if( aValue < minValue ) {
      return ValidationResult.error( FMT_ERR_VAL_LT_MIN, aValue, minValue, minValueText );
    }
    if( aValue > maxValue ) {
      return ValidationResult.error( FMT_ERR_VAL_GT_MAX, aValue, maxValue, maxValueText );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return getText();
  }

}
