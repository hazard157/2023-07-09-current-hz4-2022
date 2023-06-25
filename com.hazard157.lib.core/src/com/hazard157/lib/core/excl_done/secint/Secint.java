package com.hazard157.lib.core.excl_done.secint;

import static com.hazard157.lib.core.excl_done.secint.IPsxResources.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Интервал времени в видеоклипе, исчисляемый в секундах.
 * <p>
 * Oтсчет секунд начинается с 0. Секунда должна быть неотрицательным числом в диапазоне допустимых значений
 * продолжительности до {@link HmsUtils#MAX_MMSS_VALUE}. Время окончания интервала не может быть раньше начала.
 * <p>
 * Это неизменяемый класс.
 * <p>
 * Реализует интерфейс {@link Comparable}, сортируя по времени начала {@link #start()}, а при их равенстве - времени
 * окончания {@link #end()}.
 *
 * @author hazard157
 */
public class Secint
    implements Comparable<Secint> {

  /**
   * Максимально возможный интервал от 0 и длительностью {@link HmsUtils#MAX_MMSS_VALUE}.
   * <p>
   * Любой экземпляр интервала {@link Secint} содержится внутри этой константы.
   */
  public static final Secint MAXIMUM = new Secint( 0, MAX_MMSS_VALUE - 1 );

  /**
   * Минимальный интервал длительностью 1, начинающейся в 0.
   */
  public static final Secint ZERO = new Secint( 0, 0 );

  /**
   * String representation of the {@link #NULL}.
   */
  public static final String STR_NULL = "Secint.NULL"; //$NON-NLS-1$

  /**
   * "Нулевой" объект, для использования вместо <code>null</code> там, где Java-API не допускает <code>null</code>.
   */
  public static final Secint NULL = new Secint( 0, 0 ) {

    @Override
    public boolean isNull() {
      return true;
    }

    @Override
    public String toString() {
      return STR_NULL;
    }

  };

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "Secint"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static IEntityKeeper<Secint> KEEPER =
      new AbstractEntityKeeper<>( Secint.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, Secint aEntity ) {
          writeAutoHms( aSw, aEntity.start() );
          aSw.writeSeparatorChar();
          writeAutoHms( aSw, aEntity.end() );
        }

        @Override
        protected Secint doRead( IStrioReader aSr ) {
          int start = readAutoHms( aSr );
          aSr.ensureSeparatorChar();
          int end = readAutoHms( aSr );
          return new Secint( start, end );
        }

      };

  private final int start;
  private final int end;

  /**
   * Создает экземпляр со всеми инвариантами.
   *
   * @param aStart int - начальная (включительно) секунда интервала (должно быть >= 0)
   * @param aEnd int - конечная (включительно) секунда интервала (должно быть >= aStart)
   * @throws TsValidationFailedRtException не прошла проверка {@link #checkCanCreate(int, int)}
   */
  public Secint( int aStart, int aEnd ) {
    TsValidationFailedRtException.checkError( checkCanCreate( aStart, aEnd ) );
    start = aStart;
    end = aEnd;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Проверяет, является ли этот объект {@link #NULL}-ом.
   *
   * @return boolean - признак "нулевого" объекта {@link Secint#NULL}
   */
  public boolean isNull() {
    return false;
  }

  /**
   * Возвращает секунду (включительно) начала интервала.
   *
   * @return int - начало интервала в секундах
   */
  public int start() {
    return start;
  }

  /**
   * Возвращает секунду (включительно) окончания интервала.
   *
   * @return int - конец интервала в секундах
   */
  public int end() {
    return end;
  }

  /**
   * Возвращает длительность интервала в секундах.
   *
   * @return int - длительность интервала в секундах
   */
  public int duration() {
    return end - start + 1;
  }

  /**
   * Returns the middle second of this interval.
   *
   * @return int - middle second
   */
  public int middle() {
    return start + duration() / 2;
  }

  /**
   * Определяет, находится ли момент времени внутри интервала (включая обе границы интервала).
   *
   * @param aSecs int - секунда с начала эпизода
   * @return boolean - признак нахождения в интервале
   */
  public boolean contains( int aSecs ) {
    return (aSecs >= start) && (aSecs <= end);
  }

  /**
   * Определяет, находится ли аргумент-интервал внутри этого интервала (включая обе границы интервала).
   *
   * @param aStart int - начальная секунда аргумента-интервала
   * @param aEnd int - конечная секунда аргумента-интервала
   * @return boolean - признак нахождения в интервале
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public boolean contains( int aStart, int aEnd ) {
    TsIllegalArgumentRtException.checkTrue( aEnd < aStart );
    return contains( aStart ) && contains( aEnd );
  }

  /**
   * Определяет, находится ли аргумент-интервал внутри этого интервала (включая обе границы интервала).
   * <p>
   * Для любого интервала <code>in</code> метод <code>in.contains(in)</code> возвращает <code>true</code>.
   *
   * @param aInterval {@link Secint} - аргумент-интервал
   * @return boolean - признак нахождения в интервале
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean contains( Secint aInterval ) {
    TsNullArgumentRtException.checkNull( aInterval );
    return contains( aInterval.start ) && contains( aInterval.end );
  }

  /**
   * Определяет, пересекается ли аргумент-интервал с этим интервалом (включая обе границы интервала).
   * <p>
   * Определяет, находится ли аргумент-интервал внутри этого интервала (включая обе границы интервала).
   *
   * @param aStart int - начальная секунда аргумента-интервала
   * @param aEnd int - конечная секунда аргумента-интервала
   * @return boolean - признак нахождения в интервале
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public boolean intersects( int aStart, int aEnd ) {
    TsIllegalArgumentRtException.checkTrue( aEnd < aStart );
    return ((aEnd >= start) && (aStart <= end));
  }

  /**
   * Определяет, пересекается ли аргумент-интервал с этим интервалом (включая обе границы интервала).
   *
   * @param aInterval {@link Secint} - аргумент-интервал
   * @return boolean - признак пересечения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean intersects( Secint aInterval ) {
    TsNullArgumentRtException.checkNull( aInterval );
    return intersects( aInterval.start, aInterval.end );
  }

  /**
   * Определяет, соприкасается ли (без пересечения) аргумент с этим интервалом.
   *
   * @param aInterval {@link Secint} - аргумент-интервал
   * @return boolean - признак соприкосновения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean touches( Secint aInterval ) {
    TsNullArgumentRtException.checkNull( aInterval );
    return (aInterval.start() == end() + 1) || (start() == aInterval.end() + 1);
  }

  /**
   * Определяет является ли указанная секунда позже (правее) интервала.
   *
   * @param aSec int - запрошенная секунда
   * @return boolean - признак, что aSec > {@link #end()}
   */
  public boolean isAfter( int aSec ) {
    return aSec > end;
  }

  /**
   * Определяет является ли указанная секунда раньше (левее) нтервала.
   *
   * @param aSec int - запрошенная секунда
   * @return boolean - признак, что aSec < {@link #start()}
   */
  public boolean isBefore( int aSec ) {
    return aSec < start;
  }

  /**
   * Загоняет значение в интервал.
   *
   * @param aValue int - значение
   * @return int - значение, которое находится в интервале
   */
  public int inRange( int aValue ) {
    if( aValue < start ) {
      return start;
    }
    if( aValue > end ) {
      return end;
    }
    return aValue;
  }

  /**
   * Создает интервал внутри этого интервала.
   *
   * @param aStart int - начально инетрвала
   * @param aEnd int - окончание инетрвала
   * @return {@link Secint} - интервал, который находится внутри этого интервала
   */
  public Secint inRange( int aStart, int aEnd ) {
    int s = aStart;
    int e = aEnd;
    if( e < s ) {
      e = s;
    }
    if( s > end ) {
      return new Secint( end, end );
    }
    if( e < start ) {
      return new Secint( start, start );
    }
    if( s < start ) {
      s = start;
    }
    if( e < s ) {
      e = s;
    }
    if( e > end ) {
      e = end;
    }
    return new Secint( s, e );
  }

  /**
   * Загоняет аргумент в этот интервал.
   * <p>
   * Для пересекающихся интервалов возвращает результат пересечение. Если аргумент поностью выходит за пределы этого
   * интервала, возвращает интервал единичной длины, начало и коночание которого равно {@link #start()} или
   * {@link #end()} этого интервала. То есть, в этом случае возвращаемое значение не пересекается с аргументом.
   * <p>
   * Если аргумент содержится в этом интервале, метод возвращает аргумент.
   *
   * @param aIn {@link Secint} - аргумент
   * @return {@link Secint} - интервал, который находится внутри этого интервала
   * @throws TsNullArgumentRtException аргумент = null
   */
  public Secint inRange( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    if( aIn.start >= start && aIn.end <= end ) {
      return aIn;
    }
    return new Secint( inRange( aIn.start ), inRange( aIn.end ) );
  }

  // ------------------------------------------------------------------------------------
  // Статическое API класса
  //

  /**
   * Создает интервал, объединяющий интервалы.
   *
   * @param aIn1 {@link Secint} - первый инетрвал
   * @param aIn2 {@link Secint} - второй инетрвал
   * @return {@link Secint} - объединение интервалов
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static Secint union( Secint aIn1, Secint aIn2 ) {
    TsNullArgumentRtException.checkNulls( aIn1, aIn2 );
    int start = Math.min( aIn1.start, aIn2.start );
    int end = Math.max( aIn1.end, aIn2.end );
    return new Secint( start, end );
  }

  /**
   * Создает интервал, являюшейся пересечением интервалов или возвращает null если аргументы не персекаются.
   *
   * @param aIn1 {@link Secint} - первый инетрвал
   * @param aIn2 {@link Secint} - второй инетрвал
   * @return {@link Secint} - пересечение интервалов или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static Secint intersection( Secint aIn1, Secint aIn2 ) {
    TsNullArgumentRtException.checkNulls( aIn1, aIn2 );
    int start = Math.max( aIn1.start, aIn2.start );
    int end = Math.min( aIn1.end, aIn2.end );
    if( start > end ) {
      return null;
    }
    return new Secint( start, end );
  }

  private static String sec2str( int aSec ) {
    if( aSec >= 0 ) {
      if( aSec < MAX_MMSS_VALUE ) {
        return HmsUtils.mmss( aSec );
      }
      if( aSec < MAX_HHMMSS_VALUE ) {
        return HmsUtils.hhmmss( aSec );
      }
    }
    return Integer.toString( aSec );
  }

  /**
   * Проверяет допустимость аргументов для создания интервала конструктором {@link Secint#Secint(int, int)}.
   * <p>
   * Оба аргумента должны быть >=0, <= {@link HmsUtils#MAX_MMSS_VALUE} и окончание aEnd должно быть больше или равно
   * началу aStart. При соблудении этих условий метод вернет {@link ValidationResult#SUCCESS}, иначе будет возвращено
   * {@link EValidationResultType#ERROR}.
   *
   * @param aStart int - начальная (включительно) секунда интервала (должно быть >= 0)
   * @param aEnd int - конечная (включительно) секунда интервала (должно быть >= aStart)
   * @return {@link ValidationResult} - результат проверки
   */
  public static ValidationResult checkCanCreate( int aStart, int aEnd ) {
    if( aStart < 0 ) {
      return ValidationResult.error( FMT_ERR_CANT_CREATE_START_LT_0, sec2str( aStart ) );
    }
    if( aEnd < 0 ) {
      return ValidationResult.error( FMT_ERR_CANT_CREATE_END_LT_0, sec2str( aEnd ) );
    }
    if( aStart > MAX_MMSS_VALUE ) {
      return ValidationResult.error( FMT_ERR_CANT_CREATE_START_GT_MAX, sec2str( aStart ), MAX_MMSS_VALUE_STR );
    }
    if( aEnd > MAX_MMSS_VALUE ) {
      return ValidationResult.error( FMT_ERR_CANT_CREATE_END_GT_MAX, sec2str( aEnd ), MAX_MMSS_VALUE_STR );
    }
    if( aStart > aEnd ) {
      return ValidationResult.error( FMT_ERR_CANT_CREATE_START_GT_END, sec2str( aStart ), sec2str( aEnd ) );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Returns full textual representation as "[ start, end ] - (dur)"
   *
   * @return String - the full textual representation
   */
  @SuppressWarnings( "nls" )
  public String fullStr() {
    if( isNull() ) {
      return "Secint.NULL";
    }
    return String.format( "[ %s, %s ] - (%s)", mmss( start ), mmss( end ), mmss( duration() ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    if( isNull() ) {
      return "Secint.NULL";
    }
    return String.format( "[ %s, %s ]", mmss( start ), mmss( end ) );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof Secint obj ) {
      return start == obj.start && end == obj.end;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + start;
    result = TsLibUtils.PRIME * result + end;
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( Secint o ) {
    int c = start - o.start;
    if( c != 0 ) {
      return c;
    }
    return end - o.end;
  }

}
