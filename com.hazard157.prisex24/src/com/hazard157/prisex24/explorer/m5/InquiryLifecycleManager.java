package com.hazard157.prisex24.explorer.m5;

import static com.hazard157.prisex24.explorer.m5.InquiryM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.prisex24.explorer.pdu.*;

/**
 * Менеджер ЖЦ сущностей {@link Inquiry}.
 *
 * @author hazard157
 */
class InquiryLifecycleManager
    extends M5LifecycleManager<Inquiry, IUnitExplorer> {

  public InquiryLifecycleManager( IM5Model<Inquiry> aModel, IUnitExplorer aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  private static final InquiryInfo makeInfo( IM5Bunch<Inquiry> aValues ) {
    String name = NAME.getFieldValue( aValues ).asString();
    String descr = DESCRIPTION.getFieldValue( aValues ).asString();
    return new InquiryInfo( name, descr );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<Inquiry> aValues ) {
    String id = ID.getFieldValue( aValues ).asString();
    InquiryInfo info = makeInfo( aValues );
    return master().canCreateItem( id, info );
  }

  @Override
  protected Inquiry doCreate( IM5Bunch<Inquiry> aValues ) {
    String id = ID.getFieldValue( aValues ).asString();
    InquiryInfo info = makeInfo( aValues );
    return master().createItem( id, info );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<Inquiry> aValues ) {
    InquiryInfo info = makeInfo( aValues );
    return master().canEditItem( aValues.originalEntity().id(), aValues.originalEntity().id(), info );
  }

  @Override
  protected Inquiry doEdit( IM5Bunch<Inquiry> aValues ) {
    InquiryInfo info = makeInfo( aValues );
    aValues.originalEntity().setInfo( info );
    return aValues.originalEntity();
  }

  @Override
  protected ValidationResult doBeforeRemove( Inquiry aEntity ) {
    return master().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( Inquiry aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<Inquiry> doListEntities() {
    return master().items();
  }

}
