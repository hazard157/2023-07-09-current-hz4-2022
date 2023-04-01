package com.hazard157.psx.proj3.bricks.beq.impl;

import static com.hazard157.psx.proj3.bricks.beq.impl.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.bricks.beq.*;
import com.hazard157.psx.proj3.bricks.beq.filters.*;

/**
 * Реализация {@link IBeqFilter}.
 *
 * @author hazard157
 */
public final class BeqFilter
    implements IBeqFilter {

  private final IMapEdit<EBeqSingleFilterKind, ITsSingleFilterParams> fpMap    = new ElemMap<>();
  private boolean                                                     inverted = false;

  /**
   * Создает пустое описание, соответствующее {@link ITsCombiFilterParams#ALL}.
   */
  public BeqFilter() {
    for( EBeqSingleFilterKind k : EBeqSingleFilterKind.asList() ) {
      fpMap.put( k, ITsSingleFilterParams.NONE );
    }
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link IBeqFilter} - источник
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public BeqFilter( IBeqFilter aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    for( EBeqSingleFilterKind k : aSource.fpMap().keys() ) {
      fpMap.put( k, aSource.fpMap().getByKey( k ) );
    }
    inverted = aSource.isInverted();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IBeqFilter
  //

  @Override
  public IMapEdit<EBeqSingleFilterKind, ITsSingleFilterParams> fpMap() {
    return fpMap;
  }

  @Override
  public boolean isInverted() {
    return inverted;
  }

  @Override
  public ITsCombiFilterParams makeFilterParams() {
    ITsCombiFilterParams fp = ITsCombiFilterParams.NONE;
    for( EBeqSingleFilterKind kind : EBeqSingleFilterKind.asList() ) {
      ITsSingleFilterParams p = fpMap.findByKey( kind );
      if( p != null && p != ITsSingleFilterParams.NONE ) {
        if( fp == ITsCombiFilterParams.NONE ) {
          fp = TsCombiFilterParams.createSingle( p, inverted );
        }
        else {
          fp = TsCombiFilterParams.createCombi( fp, ELogicalOp.AND, p, inverted );
        }
      }
    }
    return fp;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Задает значение {@link #isInverted()}.
   *
   * @param aInverted boolean - признак применения НЕТ к фильтру
   */
  public void setInverted( boolean aInverted ) {
    inverted = aInverted;
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
      String s = EBeqSingleFilterKind.makeHumanReadableString( fpMap.values().get( i ) );
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
    if( aThat instanceof BeqFilter that ) {
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
