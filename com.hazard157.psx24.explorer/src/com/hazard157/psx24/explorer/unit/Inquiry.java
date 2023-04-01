package com.hazard157.psx24.explorer.unit;

import static com.hazard157.psx24.explorer.unit.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Выборка обозревателя - последовательность фильтров, применяемых к эпизодам.
 *
 * @author hazard157
 */
public class Inquiry
    extends AbstractSinentity<InquiryInfo> {

  private final ITsCollectionChangeListener collectionChangeListener =
      ( aSource, aOp, aItem ) -> changeHelper().fireChangeEvent();

  private final INotifierListEdit<InquiryItem> fpList =
      new NotifierListEditWrapper<>( new ElemLinkedBundleList<InquiryItem>() );

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор эпизода
   * @param aInfo {@link InquiryInfo} - инофрмация об эпизоде
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException неверный идентификатор эпизода
   */
  public Inquiry( String aId, InquiryInfo aInfo ) {
    super( aId, aInfo );
    fpList.addCollectionChangeListener( collectionChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String nmName() {
    return info().name();
  }

  @Override
  public String description() {
    return info().description();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает список параметров фильтров в порядке их применения.
   *
   * @return {@link INotifierListEdit}&lt;{@link InquiryItem}&gt; - редактируемый список параметров фильтров
   */
  public INotifierListEdit<InquiryItem> items() {
    return fpList;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

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

}
