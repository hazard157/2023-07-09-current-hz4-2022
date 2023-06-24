package com.hazard157.lib.core.excl_done.radioprop;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.controls.av.AbstractAvWrapperValedControl;
import org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * {@link EAtomicType#VALOBJ} of type {@link IRadioPropEnum} editor wrapping {@link ValedRadioPropEnumIcons}.
 *
 * @author hazard157
 * @param <T> - value type of the wrapped valed
 */
public class ValedAvValobjRadioPropEnumIcons<T extends Enum<T> & IRadioPropEnum>
    extends AbstractAvWrapperValedControl<T> {

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedAvValobjRadioPropEnumIcons( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedRadioPropEnumIcons.FACTORY );
  }

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @param aEnumClass {@link Class}&lt;V&gt; - the enumeration class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjRadioPropEnumIcons( ITsGuiContext aTsContext, Class<T> aEnumClass ) {
    super( prepareContext( aTsContext, aEnumClass ), EAtomicType.VALOBJ, ValedRadioPropEnumIcons.FACTORY );
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
