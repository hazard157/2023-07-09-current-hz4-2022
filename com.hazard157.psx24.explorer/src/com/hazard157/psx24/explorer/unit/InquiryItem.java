package com.hazard157.psx24.explorer.unit;

import static com.hazard157.psx24.explorer.unit.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.explorer.filters.*;

/**
 * Фильтр выборки из эпизодов.
 * <p>
 * Это параметры по одному фильтру всех видов, объединенные по И.
 *
 * @author goga
 */
public final class InquiryItem {

  private final IMapEdit<EPqSingleFilterKind, ITsSingleFilterParams> fpMap  = new ElemMap<>();
  private final IMapEdit<EPqSingleFilterKind, Boolean>               notMap = new ElemMap<>();

  /**
   * Создает пустое описание, соответствующее {@link ITsCombiFilterParams#ALL}.
   */
  public InquiryItem() {
    for( EPqSingleFilterKind k : EPqSingleFilterKind.asList() ) {
      notMap.put( k, Boolean.FALSE );
    }
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link InquiryItem} - источник
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public InquiryItem( InquiryItem aSource ) {
    this();
    TsNullArgumentRtException.checkNull( aSource );
    for( EPqSingleFilterKind k : aSource.fpMap.keys() ) {
      fpMap.put( k, aSource.fpMap.getByKey( k ) );
      setInverted( k, aSource.isInverted( k ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает карту фильтров по ключу типа фидьтра {@link ITsSingleFilterParams#typeId()}.
   *
   * @return {@link IMap}&lt;{@link EPqSingleFilterKind},{@link ITsSingleFilterParams}&gt; - карта фильтров - "параметры
   *         фильтра"
   */
  public IMapEdit<EPqSingleFilterKind, ITsSingleFilterParams> fpMap() {
    return fpMap;
  }

  /**
   * Определяет, инвертирован ли указанный фильтр.
   *
   * @param aKind {@link EPqSingleFilterKind} - фильтр
   * @return boolean - признак инвертирования
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean isInverted( EPqSingleFilterKind aKind ) {
    return notMap.getByKey( aKind ).booleanValue();
  }

  /**
   * Задает, инвертирован ли указанный фильтр.
   *
   * @param aKind {@link EPqSingleFilterKind} - фильтр
   * @param aInverted boolean - признак инвертирования
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setInverted( EPqSingleFilterKind aKind, boolean aInverted ) {
    notMap.put( aKind, Boolean.valueOf( aInverted ) );
  }

  /**
   * Возвращает все параемтры фильтра в виде единого набора.
   * <p>
   * Дляпустого фильтра возвращает {@link ITsCombiFilterParams#NONE}.
   *
   * @return {@link ITsCombiFilterParams} - фильтр выборки в виде единого набора параметров
   */
  public ITsCombiFilterParams getFilterParams() {
    ITsCombiFilterParams result = ITsCombiFilterParams.ALL;
    for( EPqSingleFilterKind kind : EPqSingleFilterKind.asList() ) {
      ITsSingleFilterParams sp = fpMap.findByKey( kind );
      if( sp != null ) {
        // сначала из единичного фильтра создадим комбифльтр с учетом инфертирования
        boolean isInverted = isInverted( kind );
        ITsCombiFilterParams cp = TsCombiFilterParams.createSingle( sp, isInverted );
        // теперь добавим его в итоговый фильтр
        result = TsCombiFilterParams.createCombi( result, ELogicalOp.AND, cp );
      }
    }
    return result;
    // ITsCombiFilterParams result = ITsCombiFilterParams.NONE;
    // for( EPqSingleFilterKind kind : EPqSingleFilterKind.asList() ) {
    // ITsSingleFilterParams p = fpMap.findByKey( kind );
    // if( p != null ) {
    // if( result == ITsCombiFilterParams.NONE ) {
    // result = TsCombiFilterParams.createSingle( p );
    // }
    // else {
    // result = TsCombiFilterParams.createCombi( result, ELogicalOp.AND, p );
    // }
    // }
    // }
    // return result;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    if( fpMap.isEmpty() ) {
      return STR_ALL_FP;
    }
    StringBuilder fpSb = new StringBuilder();
    for( int i = 0; i < fpMap.size(); i++ ) {
      EPqSingleFilterKind k = fpMap.keys().get( i );
      String s = EPqSingleFilterKind.makeHumanReadableString( fpMap.getByKey( k ) );
      if( isInverted( k ) ) {
        s = "NOT(" + s + ")";
      }
      fpSb.append( s );
      if( i < fpMap.size() - 1 ) {
        fpSb.append( "  &&  " );
      }
    }
    return fpSb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof InquiryItem that ) {
      return fpMap.equals( that.fpMap );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + fpMap.hashCode();
    return result;
  }

}
