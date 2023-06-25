package com.hazard157.lib.core.excl_done.secint.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;

import com.hazard157.lib.core.excl_done.secint.*;

/**
 * Фабрика редакторов объектов типа {@link Secint}.
 *
 * @author hazard157
 */
@SuppressWarnings( "unchecked" )
public class ValedSecintFactory
    extends AbstractValedControlFactory {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".Secint"; //$NON-NLS-1$

  /**
   * Экземпояр-синглтон фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new ValedSecintFactory();

  /**
   * Конструктор.
   */
  public ValedSecintFactory() {
    super( FACTORY_NAME );
  }

  @Override
  protected AbstractValedControl<Secint, Control> doCreateEditor( ITsGuiContext aContext ) {
    return new ValedSecint( aContext );
  }

  @Override
  protected AbstractValedControl<Secint, Control> doCreateViewer( ITsGuiContext aContext ) {
    return new ValedSecintReadOnly( aContext );
  }

}
