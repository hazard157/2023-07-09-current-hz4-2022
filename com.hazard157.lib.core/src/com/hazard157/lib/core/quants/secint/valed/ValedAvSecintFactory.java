package com.hazard157.lib.core.quants.secint.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Фабрика редакторов {@link EAtomicType#VALOBJ} типа {@link Secint}.
 *
 * @author goga
 */
@SuppressWarnings( "unchecked" )
public class ValedAvSecintFactory
    extends AbstractValedControlFactory {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvSecint"; //$NON-NLS-1$

  /**
   * Экземпояр-синглтон фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new ValedAvSecintFactory();

  /**
   * Конструктор.
   */
  public ValedAvSecintFactory() {
    super( FACTORY_NAME );
  }

  @Override
  protected AbstractValedControl<IAtomicValue, Control> doCreateEditor( ITsGuiContext aContext ) {
    return new ValedAvSecint( aContext );
  }

  @Override
  protected AbstractValedControl<IAtomicValue, Control> doCreateViewer( ITsGuiContext aContext ) {
    return new ValedAvSecintReadOnly( aContext );
  }

}
