package com.hazard157.lib.core.excl_plan.secint.valed;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Редактор {@link EAtomicType#VALOBJ}, содержащий {@link Secint}.
 *
 * @author hazard157
 */
public class ValedAvSecint
    extends AbstractValedControl<IAtomicValue, Control> {

  /**
   * Минимальное значение начала интервала.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: значение в секундах (вклюительно)<br>
   * Значение по умолчанию: 0
   */
  public static final IDataDef MIN_START = ValedSecint.MIN_START;

  /**
   * Максимальное значение окончания интервала.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: значение в секундах (вклюительно)<br>
   * Значение по умолчанию: {@link HmsUtils#MAX_MMSS_VALUE} - 1
   */
  public static final IDataDef MAX_END = ValedSecint.MAX_END;

  private final ValedSecint vs;

  protected ValedAvSecint( ITsGuiContext aTsContext ) {
    super( aTsContext );
    vs = new ValedSecint( aTsContext );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return vs.createControl( aParent );
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    vs.setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    return vs.canGetValue();
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    Secint value = vs.getValue();
    if( value != null ) {
      return avValobj( value );
    }
    return IAtomicValue.NULL;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    if( aValue == null || aValue == IAtomicValue.NULL ) {
      vs.setValue( null );
    }
    else {
      vs.setValue( aValue.asValobj() );
    }
  }

  @Override
  protected void doClearValue() {
    vs.clearValue();
  }

}
