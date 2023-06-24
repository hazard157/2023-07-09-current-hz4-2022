package com.hazard157.lib.core.excl_done.radioprop;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.controls.av.AbstractAvWrapperValedControl;
import org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.excl_done.*;

/**
 * {@link EAtomicType#VALOBJ} of type {@link IRadioPropEnum} editor wrapping {@link ValedRadioPropEnumStars}.
 *
 * @author hazard157
 * @param <T> - value type of the wrapped valed
 */
public class ValedAvValobjRadioPropEnumStars<T extends Enum<T> & IRadioPropEnum>
    extends AbstractAvWrapperValedControl<T> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjRadioPropEnumStars"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjRadioPropEnumStars<>( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedAvValobjRadioPropEnumStars( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedRadioPropEnumStars.FACTORY );
  }

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @param aEnumClass {@link Class}&lt;V&gt; - the enumeration class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjRadioPropEnumStars( ITsGuiContext aTsContext, Class<T> aEnumClass ) {
    super( prepareContext( aTsContext, aEnumClass ), EAtomicType.VALOBJ, ValedRadioPropEnumStars.FACTORY );
  }

  private static <T> ITsGuiContext prepareContext( ITsGuiContext aContext, Class<T> aEnumClass ) {
    TsNullArgumentRtException.checkNull( aEnumClass );
    IValedEnumConstants.REFDEF_ENUM_CLASS.setRef( aContext, aEnumClass );
    return aContext;
  }

  // ------------------------------------------------------------------------------------
  // AbstractAvWrapperValedControl
  //

  @Override
  protected IAtomicValue tv2av( T aTypedValue ) {
    return avValobj( aTypedValue );
  }

  @Override
  protected T av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asValobj();
  }

}
