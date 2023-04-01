package com.hazard157.lib.core.bricks.stripent.m5;

import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5Model;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.bricks.stripent.IStripent;
import com.hazard157.lib.core.bricks.stripent.IStripentManager;

/**
 * Base M5-model of {@link IStripent} entities.
 * <p>
 * Warning: model does <b>not</b> adds any declared field definition to the {@link #fieldDefs()}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class StripentM5ModelBase<T extends IStripent>
    extends M5Model<T> {

  /**
   * Attribute {@link IStripent#id()}.
   */
  public final M5AttributeFieldDef<T> ID = new M5StdFieldDefId<>();

  /**
   * Attribute {@link IStripent#nmName()}.
   */
  public final M5AttributeFieldDef<T> NAME = new M5StdFieldDefName<>();

  /**
   * Attribute {@link IStripent#description()}.
   */
  public final M5AttributeFieldDef<T> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Конструктор.
   * <p>
   * Warning: constructor does <b>not</b> adds any declared field definition to the {@link #fieldDefs()}.
   *
   * @param aId String - идентификатор (ИД-путь) модели
   * @param aModelledClass {@link Class} - класс (тип) моделированных объектов
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public StripentM5ModelBase( String aId, Class<T> aModelledClass ) {
    super( aId, aModelledClass );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IM5LifecycleManager<T> doCreateLifecycleManager( Object aMaster ) {
    if( aMaster instanceof IStripentManager ) {
      Class<?> smClass = ((IStripentManager<?>)aMaster).creator().entityClass();
      Class<T> m5Class = entityClass();
      TsIllegalArgumentRtException.checkFalse( m5Class.isAssignableFrom( smClass ) /* TODO message */ );
      return new StripentM5LifecycleManagerBase<>( this, (IStripentManager<T>)aMaster );
    }
    throw new TsIllegalArgumentRtException( /* TODO message */ );
  }

}
