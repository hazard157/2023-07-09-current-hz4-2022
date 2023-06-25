package com.hazard157.lib.core.excl_done.stripent.m5;

import static com.hazard157.lib.core.excl_done.stripent.m5.ITsResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.impl.M5LifecycleManager;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.helpers.IListReorderer;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

import com.hazard157.lib.core.excl_done.stripent.*;

/**
 * {@link IM5LifecycleManager} implementation base for {@link StripentM5ModelBase}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class StripentM5LifecycleManagerBase<T extends IStripent>
    extends M5LifecycleManager<T, IStripentManager<T>> {

  /**
   * Constuctor.
   *
   * @param aModel {@link IM5Model} - the model
   * @param aMaster {@link IStripentManager} - the owner manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StripentM5LifecycleManagerBase( IM5Model<T> aModel, IStripentManager<T> aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  private final T makeStripent( IM5Bunch<T> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSetEdit params = new OptionSet();
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( fdef.valueClass() == IAtomicValue.class ) {
        if( !fdef.id().equals( FID_ID ) ) {
          IAtomicValue av = aValues.getAsAv( fdef.id() );
          params.setValue( fdef.id(), av );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( FMT_WARN_NON_ATTR_FIELD_DEF, model().id(), fdef.id() );
      }
    }
    return master().creator().create( id, params );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<T> aValues ) {
    T s = makeStripent( aValues );
    return master().items().canAdd( s.id(), s );
  }

  @Override
  protected T doCreate( IM5Bunch<T> aValues ) {
    T s = makeStripent( aValues );
    master().items().add( s );
    return s;
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<T> aValues ) {
    T s = makeStripent( aValues );
    return master().items().canPut( s.id(), s );
  }

  @Override
  protected T doEdit( IM5Bunch<T> aValues ) {
    T s = makeStripent( aValues );
    master().items().put( s );
    return s;
  }

  @Override
  protected ValidationResult doBeforeRemove( T aEntity ) {
    return master().items().canRemove( aEntity.id() );
  }

  @Override
  protected void doRemove( T aEntity ) {
    master().items().removeById( aEntity.id() );
  }

  @Override
  protected IList<T> doListEntities() {
    return master().items();
  }

  @Override
  protected IListReorderer<T> doGetItemsReorderer() {
    return master().reorderer();
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

}
