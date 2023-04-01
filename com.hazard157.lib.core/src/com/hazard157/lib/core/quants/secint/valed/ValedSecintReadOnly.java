package com.hazard157.lib.core.quants.secint.valed;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Просмотрщик объектов типа {@link Secint}.
 *
 * @author hazard157
 */
public class ValedSecintReadOnly
    extends AbstractValedControl<Secint, Control> {

  private Secint value = Secint.ZERO;
  private Text   text  = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedSecintReadOnly( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    text = new Text( aParent, SWT.BORDER );
    text.setEditable( false );
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected Secint doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( Secint aValue ) {
    value = aValue;
    text.setText( value != null ? value.toString() : TsLibUtils.EMPTY_STRING );
  }

  @Override
  protected void doClearValue() {
    text.setText( TsLibUtils.EMPTY_STRING );
  }

}
