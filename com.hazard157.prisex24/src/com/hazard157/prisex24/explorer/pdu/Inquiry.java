package com.hazard157.prisex24.explorer.pdu;

import static com.hazard157.prisex24.explorer.pdu.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Explorer selection -a sequence of filters applied to episodes.
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
   * Constructor.
   *
   * @param aId String - the episode ID
   * @param aInfo {@link InquiryInfo} - info about inquiry
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException ID is not an IDpath
   */
  public Inquiry( String aId, InquiryInfo aInfo ) {
    super( aId, aInfo );
    fpList.addCollectionChangeListener( collectionChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
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
   * Returns a list of filter parameters in the order they are applied.
   *
   * @return {@link INotifierListEdit}&lt;{@link InquiryItem}&gt; - editable list of filter options
   */
  public INotifierListEdit<InquiryItem> items() {
    return fpList;
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

}
