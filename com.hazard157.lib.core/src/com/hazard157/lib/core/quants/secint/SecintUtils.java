package com.hazard157.lib.core.quants.secint;

import org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Вспомогательные методы работы с {@link Secint}.
 *
 * @author hazard157
 */
public class SecintUtils {

  private static final String SECINT_IDNAME_PREFIX   = "in";                                   //$NON-NLS-1$
  private static final String SECINT_IDNAME_TEMPLATE = SECINT_IDNAME_PREFIX + "000000_235959"; //$NON-NLS-1$
  private static final int    UNDERSCORE_INDEX       = 8;

  /**
   * Определяет, содержится ли секунда в расширенном интервале.
   *
   * @param aIn {@link Secint} - интервал
   * @param aSec int - проверяемая секунда
   * @param aDeltaBelow int - секунды расширения {@link Secint#start()}, >0 - вниз, <0 - вверх
   * @param aDeltaOver int - секунды расширения {@link Secint#end()}, >0 - вверх, <0 - вниз
   * @return boolean - признак нахождения в расширенном интервале
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static boolean containsEx( Secint aIn, int aSec, int aDeltaBelow, int aDeltaOver ) {
    TsNullArgumentRtException.checkNull( aIn );
    int start = aIn.start() - aDeltaBelow;
    int end = aIn.end() + aDeltaOver;
    return (aSec >= start) && (aSec <= end);
  }

  /**
   * Возвращает представление {@link Secint} в виде ИД-имени типа "in000117_015959".
   *
   * @param aIn {@link Secint} - интервал
   * @return String - ИД-имя
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @SuppressWarnings( { "boxing", "nls" } )
  public static final String toIdName( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    int sh = aIn.start() / 3600;
    int sm = (aIn.start() - sh * 3600) / 60;
    int ss = aIn.start() % 60;
    int eh = aIn.end() / 3600;
    int em = (aIn.end() - eh * 3600) / 60;
    int es = aIn.end() % 60;
    return String.format( "in%02d%02d%02d_%02d%02d%02d", sh, sm, ss, eh, em, es );
  }

  /**
   * Проверят, что аргумент ИД-имя, имеет прафильный ормат.
   * <p>
   * Значения момента начала и окончания не проверяются.
   *
   * @param aIdName String - проверяемай строка
   * @return boolean - результат проверкиб <code>true</code> - строка имеет правильный формат
   */
  public static final boolean isValidSecintIdName( String aIdName ) {
    TsNullArgumentRtException.checkNull( aIdName );
    if( aIdName.length() != SECINT_IDNAME_TEMPLATE.length() ) {
      return false;
    }
    if( !aIdName.startsWith( SECINT_IDNAME_PREFIX ) ) {
      return false;
    }
    for( int i = SECINT_IDNAME_PREFIX.length(); i < SECINT_IDNAME_TEMPLATE.length(); i++ ) {
      char ch = aIdName.charAt( i );
      if( i == UNDERSCORE_INDEX ) {
        if( ch != '_' ) {
          return false;
        }
        continue;
      }
      if( !StrioUtils.isAsciiDigit( ch ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Восстанавливает {@link Secint} из ИД-имени, созданной методом {@link #toIdName(Secint)}.
   *
   * @param aIdName String - строка, созданная методром {@link #toIdName(Secint)}
   * @return {@link Secint} - восстановленный интервал
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException неверная строка
   */
  public static Secint toSecint( String aIdName ) {
    TsIllegalArgumentRtException.checkFalse( isValidSecintIdName( aIdName ) );
    int sh = Integer.valueOf( aIdName.substring( 2, 4 ) ).intValue();
    int sm = Integer.valueOf( aIdName.substring( 4, 6 ) ).intValue();
    int ss = Integer.valueOf( aIdName.substring( 6, 8 ) ).intValue();
    int eh = Integer.valueOf( aIdName.substring( 9, 11 ) ).intValue();
    int em = Integer.valueOf( aIdName.substring( 11, 12 ) ).intValue();
    int es = Integer.valueOf( aIdName.substring( 12, 14 ) ).intValue();
    int start = sh * 3600 + sm * 60 + ss;
    int end = eh * 3600 + em * 60 + es;
    return new Secint( start, end );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private SecintUtils() {
    // nop
  }

}
