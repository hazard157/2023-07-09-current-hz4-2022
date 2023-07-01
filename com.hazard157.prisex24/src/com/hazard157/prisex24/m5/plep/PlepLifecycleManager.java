package com.hazard157.prisex24.m5.plep;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.plep.PlepM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * LM for {@link PlepM5Model}.
 *
 * @author hazard157
 */
class PlepLifecycleManager
    extends M5LifecycleManager<IPlep, IUnitPleps> {

  public PlepLifecycleManager( IM5Model<IPlep> aModel, IUnitPleps aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  private static final PlepInfo makeInfo( IM5Bunch<IPlep> aValues ) {
    String name = NAME.getFieldValue( aValues ).asString();
    String descr = DESCRIPTION.getFieldValue( aValues ).asString();
    String place = PLACE.getFieldValue( aValues ).asString();
    return new PlepInfo( name, descr, place );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<IPlep> aValues ) {
    String id = ID.getFieldValue( aValues ).asString();
    PlepInfo info = makeInfo( aValues );
    return master().canCreateItem( id, info );
  }

  @Override
  protected IPlep doCreate( IM5Bunch<IPlep> aValues ) {
    String id = ID.getFieldValue( aValues ).asString();
    PlepInfo info = makeInfo( aValues );
    IList<String> llSteps = aValues.getAs( FID_PREP_STEPS, IList.class );
    IPlep plep = master().createItem( id, info );
    plep.preparationSteps().setAll( llSteps );
    return plep;
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<IPlep> aValues ) {
    String id = ID.getFieldValue( aValues ).asString();
    PlepInfo info = makeInfo( aValues );
    return master().canEditItem( aValues.originalEntity().id(), id, info );
  }

  @Override
  protected IPlep doEdit( IM5Bunch<IPlep> aValues ) {
    String id = ID.getFieldValue( aValues ).asString();
    PlepInfo info = makeInfo( aValues );
    IList<String> llSteps = aValues.getAs( FID_PREP_STEPS, IList.class );
    IPlep plep = master().editItem( aValues.originalEntity().id(), id, info );
    plep.preparationSteps().setAll( llSteps );
    return plep;
  }

  @Override
  protected ValidationResult doBeforeRemove( IPlep aEntity ) {
    return master().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( IPlep aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<IPlep> doListEntities() {
    return master().items();
  }

}
