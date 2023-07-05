package com.hazard157.prisex24.explorer.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.explorer.pdu.*;

class InquiryItemLifecycleManager
    extends M5LifecycleManager<InquiryItem, Inquiry> {

  public InquiryItemLifecycleManager( IM5Model<InquiryItem> aModel, Inquiry aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  @Override
  protected InquiryItem doCreate( IM5Bunch<InquiryItem> aValues ) {
    InquiryItem fp = InquiryItemM5Model.FP.getFieldValue( aValues );
    master().items().add( fp );
    return fp;
  }

  @Override
  protected InquiryItem doEdit( IM5Bunch<InquiryItem> aValues ) {
    int index = master().items().indexOf( aValues.originalEntity() );
    if( index >= 0 ) {
      InquiryItem fp = InquiryItemM5Model.FP.getFieldValue( aValues );
      master().items().set( index, fp );
      return fp;
    }
    return null;
  }

  @Override
  protected void doRemove( InquiryItem aEntity ) {
    master().items().remove( aEntity );
  }

  @Override
  protected IList<InquiryItem> doListEntities() {
    return master().items();
  }

}
