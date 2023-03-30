package com.hazard157.psx24.catnote.m5.category;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * Lifecycle manager for {@link NbCategoryM5Model}.
 *
 * @author goga
 */
class NbCategoryM5LifecycleManager
    extends M5LifecycleManager<INbCategory, INbNotebook> {

  public NbCategoryM5LifecycleManager( IM5Model<INbCategory> aModel, INbNotebook aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IOptionSet makeParams( IM5Bunch<INbCategory> aValues ) {
    IOptionSetEdit p = new OptionSet();
    p.setValue( TSID_ID, aValues.getAsAv( FID_ID ) );
    p.setValue( TSID_NAME, aValues.getAsAv( FID_NAME ) );
    p.setValue( TSID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ) );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<INbCategory> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().rootCategory().svs().validator().canCreateItem( id, p );
  }

  @Override
  protected INbCategory doCreate( IM5Bunch<INbCategory> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().rootCategory().createItem( id, p );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<INbCategory> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().rootCategory().svs().validator().canEditItem( aValues.originalEntity().id(), id, p );
  }

  @Override
  protected INbCategory doEdit( IM5Bunch<INbCategory> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().rootCategory().editItem( aValues.originalEntity().id(), id, p );
  }

  @Override
  protected ValidationResult doBeforeRemove( INbCategory aEntity ) {
    return master().rootCategory().svs().validator().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( INbCategory aEntity ) {
    master().rootCategory().removeItem( aEntity.id() );
  }

  @Override
  protected IList<INbCategory> doListEntities() {
    return master().rootCategory().items();
  }

}
