package com.hazard157.lib.core.excl_plan.secint.valed;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Просмотрщик, соответствующий редактору {@link ValedAvIntHhmmss}.
 *
 * @author hazard157
 */
public class ValedAvIntHhmmssReadOnly
    extends AbstractValedControl<IAtomicValue, Control> {

  private Text         text;
  private IAtomicValue av = AV_0;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedAvIntHhmmssReadOnly( ITsGuiContext aContext ) {
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
  protected IAtomicValue doGetUnvalidatedValue() {
    return av;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    if( aValue == null || aValue == IAtomicValue.NULL ) {
      av = AV_1;
    }
    else {
      av = aValue;
    }
    if( text == null ) {
      return;
    }
    int secs = -1;
    if( aValue != IAtomicValue.NULL ) {
      secs = av.asInt();
    }
    String msg = HmsUtils.STR_UNDEFINED_HHMMSS;
    if( secs > 0 && secs <= HmsUtils.MAX_HHMMSS_VALUE ) {
      if( secs < (59 * 60 + 59) && params().getBool( ValedAvIntHhmmss.IS_ONLY_MMSS ) ) {
        msg = HmsUtils.mmmss( secs );
      }
      else {
        msg = HmsUtils.hhhmmss( secs );
      }
    }
    text.setText( msg );
  }

  @Override
  protected void doClearValue() {
    av = IAtomicValue.NULL;
    text.setText( TsLibUtils.EMPTY_STRING );
  }

}
