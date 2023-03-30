package com.hazard157.psx.common.utils;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.utils.IHzResources.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.*;

/**
 * Расширяет методы {@link HmsUtils} работой с кадрами при частоте кадров в видеопотоке {@link IPsxHardConstants#FPS} .
 *
 * @author hazard157
 */
public class HhMmSsFfUtils {

  /**
   * Строковое представление отрицательных значений количества кадров в {@link HhMmSsFfUtils#mmssff(int)}.
   */
  public static final String STR_UNDEFINED_FRAMES_MMSSFF = "??:??.??"; //$NON-NLS-1$

  /**
   * Атомарное представление {@link #STR_UNDEFINED_FRAMES_MMSSFF}.
   */
  public static final IAtomicValue AV_STR_UNDEFINED_MMSSFF = avStr( STR_UNDEFINED_FRAMES_MMSSFF );

  /**
   * Максмальная значение в кадрах, которая может отображаться в виде ММ:СС.КК.
   */
  public static final int MAX_MMSSFF_VALUE = MAX_MMSS_VALUE * FPS + FPS - 1;

  /**
   * Максмальная значение в кадрах, которая может отображаться в виде ЧЧ:ММ:СС.КК.
   */
  public static final int MAX_HHMMSSFF_VALUE = MAX_HHMMSS_VALUE * FPS + FPS - 1;

  /**
   * Строка, используемая для значения количества кадров -1 в методе {@link #mmssff(int)}.
   */
  private static final String STR_MINUS_1_HH_MM_FF = "00:00.-1"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Методы форматирования времени
  //

  /**
   * Возвращает строку вида "ЧЧ:ММ:СС.КК" из количества индекса (количества) кадра.
   * <p>
   * Если длительность меньше часа, возвращает "ММ:СС.КК".
   *
   * @param aFrameNo int - номер кадра
   * @return String - строка вида "ЧЧ:ММ:СС.КК" или "ММ:СС.КК"
   * @throws TsIllegalArgumentRtException aFrameNo < 0
   */
  @SuppressWarnings( { "nls", "boxing" } )
  public static String hhmmssff( int aFrameNo ) {
    int f = aFrameNo % FPS;
    int secs = aFrameNo / FPS;
    int h = secs / 3600;
    secs -= h * 3600;
    int m = secs / 60;
    int s = secs - m * 60;
    if( h == 0 ) {
      return String.format( "%02d:%02d.%02d", m, s, f );
    }
    return String.format( "%02d:%02d:%02d.%02d", h, m, s, f );
  }

  /**
   * Возвращает строку вида "ММ:СС.КК" из количества секунд и номера кадра в секунде.
   *
   * @param aSecs int - количество секунд
   * @param aFraction int - номер кадра в поседней секунде в пределах 0.. {@link IPsxHardConstants#FPS}-1
   * @return String - строка вида "ММ:СС.КК"
   * @throws TsIllegalArgumentRtException aFrameNo < 0 или aFrameNo >= {@link IPsxHardConstants#FPS}
   * @throws TsIllegalArgumentRtException aSecs < 0 или aSecs >= {@link #MAX_MMSS_VALUE}
   */
  public static String mmssff( int aSecs, int aFraction ) {
    TsIllegalArgumentRtException.checkTrue( aFraction < 0 || aFraction >= FPS );
    TsNullArgumentRtException.checkTrue( aSecs < 0 || aSecs >= MAX_MMSS_VALUE );
    int m = aSecs / 60;
    int s = aSecs % 60;
    return String.format( "%02d:%02d.%02d", Integer.valueOf( m ), Integer.valueOf( s ), Integer.valueOf( aFraction ) ); //$NON-NLS-1$
  }

  /**
   * Возвращает строку вида "ММ:СС.КК" из общего количества кадров.
   * <p>
   * Для недопустимых значении возвращает строку {@link #STR_UNDEFINED_FRAMES_MMSSFF}.
   *
   * @param aFrameNo int - номер кадра
   * @return String - строка вида "ММ:СС.КК"
   */
  public static String mmssff( int aFrameNo ) {
    if( aFrameNo < 0 || aFrameNo > MAX_MMSSFF_VALUE ) {
      return STR_UNDEFINED_FRAMES_MMSSFF;
    }
    int secs = aFrameNo / FPS;
    int fraction = aFrameNo % FPS;
    return mmssff( secs, fraction );
  }

  /**
   * Записывает количество кадров текстом вида "ММ:СС.КК".
   *
   * @param aSw {@link IStrioWriter} - писатель текстового представления
   * @param aFrames int - количество кадров
   * @throws TsNullArgumentRtException aSw = null
   * @throws TsIllegalArgumentRtException aFrames < 0
   * @throws TsIllegalArgumentRtException aFrames >= {@link #MAX_MMSS_VALUE} * {@link IPsxHardConstants#FPS}
   */
  public static void writeMmSsFf( IStrioWriter aSw, int aFrames ) {
    if( aSw == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aFrames < -1 || aFrames > MAX_MMSS_VALUE * FPS ) {
      throw new TsIllegalArgumentRtException();
    }
    if( aFrames == -1 ) {
      aSw.writeAsIs( STR_MINUS_1_HH_MM_FF );
      return;
    }
    int secs = aFrames / FPS;
    Integer mm = Integer.valueOf( secs / 60 );
    Integer ss = Integer.valueOf( secs % 60 );
    Integer ff = Integer.valueOf( aFrames % FPS );
    aSw.writeAsIs( String.format( "%02d:%02d.%02d", mm, ss, ff ) ); //$NON-NLS-1$
  }

  /**
   * Из строки формата [ЧЧ:]ММ:СС[.КК] сбирает целое число - номер кадра (исходя из {@link IPsxHardConstants#FPS}).
   *
   * @param aHmsf String - входная строка
   * @return int - номер кадра
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат строки
   */
  public static int hmsf2no( String aHmsf ) {
    TsNullArgumentRtException.checkNull( aHmsf );
    int len = aHmsf.length();
    int h = 0, m = 0, s = 0, f = 0;
    switch( len ) {
      case 5: // "ММ:СС"
        checkAsciiDigit( aHmsf, 0, 1, 3, 4 );
        checkHmsDelim( aHmsf, 2 );
        m = parseIntTwoDigitsAt( aHmsf, 0 );
        s = parseIntTwoDigitsAt( aHmsf, 3 );
        break;
      case 8: // "ЧЧ:ММ:СС" или "ММ:СС.КК"
        checkAsciiDigit( aHmsf, 0, 1, 3, 4, 6, 7 );
        checkHmsDelim( aHmsf, 2 );
        if( aHmsf.charAt( 5 ) == ':' ) { // "ЧЧ:ММ:СС"
          h = parseIntTwoDigitsAt( aHmsf, 0 );
          m = parseIntTwoDigitsAt( aHmsf, 3 );
          s = parseIntTwoDigitsAt( aHmsf, 6 );
        }
        else { // "ММ:СС.КК"
          checkFramesDelim( aHmsf, 5 );
          m = parseIntTwoDigitsAt( aHmsf, 0 );
          s = parseIntTwoDigitsAt( aHmsf, 3 );
          f = parseIntTwoDigitsAt( aHmsf, 6 );
        }
        break;
      case 11: // ЧЧ:ММ:СС.КК
        checkAsciiDigit( aHmsf, 0, 1, 3, 4, 6, 7, 9, 10 );
        checkHmsDelim( aHmsf, 2, 5 );
        checkFramesDelim( aHmsf, 8 );
        h = parseIntTwoDigitsAt( aHmsf, 0 );
        m = parseIntTwoDigitsAt( aHmsf, 3 );
        s = parseIntTwoDigitsAt( aHmsf, 6 );
        f = parseIntTwoDigitsAt( aHmsf, 9 );
        break;
      default:
        throw new TsIllegalArgumentRtException();
    }
    return (h * 3600 + m * 60 + s) * FPS + f;
  }

  private static final int parseIntTwoDigitsAt( String aStr, int aStartIndex ) {
    try {
      String s = aStr.substring( aStartIndex, aStartIndex + 2 );
      return Integer.parseInt( s );
    }
    catch( NumberFormatException ex ) {
      throw new TsIllegalArgumentRtException( ex );
    }
  }

  private static final void checkHmsDelim( String aStr, int... aPos ) {
    int len = aStr.length();
    for( int i = 0; i < aPos.length; i++ ) {
      int p = aPos[i];
      TsIllegalArgumentRtException.checkTrue( p >= len );
      TsIllegalArgumentRtException.checkTrue( aStr.charAt( p ) != ':' );
    }
  }

  private static final void checkFramesDelim( String aStr, int... aPos ) {
    int len = aStr.length();
    for( int i = 0; i < aPos.length; i++ ) {
      int p = aPos[i];
      TsIllegalArgumentRtException.checkTrue( p >= len );
      TsIllegalArgumentRtException.checkTrue( aStr.charAt( p ) != '.' );
    }
  }

  private static final void checkAsciiDigit( String aStr, int... aPos ) {
    int len = aStr.length();
    for( int i = 0; i < aPos.length; i++ ) {
      int p = aPos[i];
      TsIllegalArgumentRtException.checkTrue( p >= len );
      TsIllegalArgumentRtException.checkFalse( isAsciiDigit( aStr.charAt( p ) ) );
    }
  }

  /**
   * Читает количество секунд, ожидая текст вида "ММ:СС".
   *
   * @param aSr {@link IStrioReader} - чситатель текстового представления
   * @return int - колчиство секунд
   * @throws TsNullArgumentRtException aSr = null
   * @throws StrioRtException нарушен формат
   */
  public static int readMmSsFf( IStrioReader aSr ) {
    if( aSr == null ) {
      throw new TsNullArgumentRtException();
    }
    char c1 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c1 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FF_FORMAT );
    }
    char c2 = aSr.nextChar();
    if( !isAsciiDigit( c2 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FF_FORMAT );
    }
    aSr.ensureChar( ':' );
    char c3 = aSr.nextChar();
    if( !isAsciiDigit( c3 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FF_FORMAT );
    }
    char c4 = aSr.nextChar();
    if( !isAsciiDigit( c4 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FF_FORMAT );
    }
    aSr.ensureChar( '.' );
    char c5 = aSr.nextChar();
    if( c5 == '-' ) {
      aSr.ensureChar( '1' );
      return -1;
    }
    if( !isAsciiDigit( c5 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FF_FORMAT );
    }
    char c6 = aSr.nextChar();
    if( !isAsciiDigit( c6 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FF_FORMAT );
    }
    int mm = (c1 - '0') * 10 + (c2 - '0');
    int ss = (c3 - '0') * 10 + (c4 - '0');
    int ff = (c5 - '0') * 10 + (c6 - '0');
    return (mm * 60 + ss) * FPS + ff;
  }

  /**
   * Запрет на создание экземпляров.
   */
  private HhMmSsFfUtils() {
    // nop
  }

}
