package com.hazard157.lib.core.quants.secint.valed;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Просмотрщик {@link EAtomicType#VALOBJ}, содержащий {@link Secint}.
 *
 * @author hazard157
 */
public class ValedAvSecintReadOnly
    extends AbstractValedControl<IAtomicValue, Control> {

  private final ValedSecintReadOnly vs;

  protected ValedAvSecintReadOnly( ITsGuiContext aTsContext ) {
    super( aTsContext );
    vs = new ValedSecintReadOnly( aTsContext );
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
