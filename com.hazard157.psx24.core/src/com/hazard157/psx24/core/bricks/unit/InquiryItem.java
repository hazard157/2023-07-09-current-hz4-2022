package com.hazard157.psx24.core.bricks.unit;

import static com.hazard157.psx24.core.bricks.unit.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.core.bricks.pq.filters.*;

/**
 * Filter selection from episodes.
 * <p>
 * These are the parameters for one filter of all types, combined by AND.
 *
 * @author hazard157
 */
public final class InquiryItem {

  private final IMapEdit<EPqSingleFilterKind, ITsSingleFilterParams> fpMap  = new ElemMap<>();
  private final IMapEdit<EPqSingleFilterKind, Boolean>               notMap = new ElemMap<>();

  /**
   * Creates item corresponding to {@link ITsCombiFilterParams#ALL}.
   */
  public InquiryItem() {
    for( EPqSingleFilterKind k : EPqSingleFilterKind.asList() ) {
      notMap.put( k, Boolean.FALSE );
    }
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link InquiryItem} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
   * Returns all filters making this item.
   *
   * @return {@link IMapEdit}&lt;{@link EPqSingleFilterKind},{@link ITsSingleFilterParams}&gt; - the filters map
   */
  public IMapEdit<EPqSingleFilterKind, ITsSingleFilterParams> fpMap() {
    return fpMap;
  }

  /**
   * Determines if filter of the specified kind is inverted.
   *
   * @param aKind {@link EPqSingleFilterKind} - the filter kind
   * @return boolean - inverted sign
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean isInverted( EPqSingleFilterKind aKind ) {
    return notMap.getByKey( aKind ).booleanValue();
  }

  /**
   * Sets the {@link #isInverted(EPqSingleFilterKind)} sign.
   *
   * @param aKind {@link EPqSingleFilterKind} - the filter kind
   * @param aInverted boolean - inverted sign
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setInverted( EPqSingleFilterKind aKind, boolean aInverted ) {
    notMap.put( aKind, Boolean.valueOf( aInverted ) );
  }

  /**
   * Creates and returns the parameters for {@link ITsFilter} creation..
   * <p>
   * For an empty item returns {@link ITsCombiFilterParams#NONE}.
   *
   * @return {@link ITsCombiFilterParams} - parameters to create {@link ITsFilter}
   */
  public ITsCombiFilterParams getFilterParams() {
    ITsCombiFilterParams result = ITsCombiFilterParams.ALL;
    for( EPqSingleFilterKind kind : EPqSingleFilterKind.asList() ) {
      ITsSingleFilterParams sp = fpMap.findByKey( kind );
      if( sp != null ) {
        // first, from a single filter, we create a combi-filter, taking into account the inversion
        boolean isInverted = isInverted( kind );
        ITsCombiFilterParams cp = TsCombiFilterParams.createSingle( sp, isInverted );
        // add to resulting params
        result = TsCombiFilterParams.createCombi( result, ELogicalOp.AND, cp );
      }
    }
    return result;
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
