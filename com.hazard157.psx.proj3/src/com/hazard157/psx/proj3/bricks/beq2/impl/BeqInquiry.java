package com.hazard157.psx.proj3.bricks.beq2.impl;

import static com.hazard157.psx.proj3.bricks.beq2.impl.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;

import com.hazard157.psx.proj3.bricks.beq2.*;

/**
 * Выборка обозревателя - последовательность фильтров, применяемых к эпизодам.
 *
 * @author goga
 */
public class BeqInquiry
    implements IBeqInquiry {

  private final ITsCollectionChangeListener collectionChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      eventer.fireChangeEvent();
    }
  };

  final GenericChangeEventer eventer;

  private final INotifierListEdit<IBeqFilter> fpList =
      new NotifierListEditWrapper<>( new ElemLinkedBundleList<IBeqFilter>() );

  /**
   * Конструктор.
   */
  public BeqInquiry() {
    eventer = new GenericChangeEventer( this );
    fpList.addCollectionChangeListener( collectionChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    if( fpList.isEmpty() ) {
      return STR_EMPTY_INQUIRY;
    }
    StringBuilder sb = new StringBuilder();
    for( int i = 0, n = fpList.size(); i < n; i++ ) {
      sb.append( "(" );
      sb.append( fpList.get( i ).toString() );
      sb.append( ")" );
      if( i < n - 1 ) {
        sb.append( "  &&  " );
      }
    }
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IBeqInquiry
  //

  @Override
  public INotifierListEdit<IBeqFilter> items() {
    return fpList;
  }

  @Override
  public ITsCombiFilterParams makeFilterParams() {
    ITsCombiFilterParams result = ITsCombiFilterParams.NONE;
    for( IBeqFilter bf : fpList ) {
      ITsCombiFilterParams fp = bf.makeFilterParams();
      if( result == ITsCombiFilterParams.NONE ) {
        result = fp;
      }
      else {
        result = TsCombiFilterParams.createCombi( result, ELogicalOp.AND, fp );
      }
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
