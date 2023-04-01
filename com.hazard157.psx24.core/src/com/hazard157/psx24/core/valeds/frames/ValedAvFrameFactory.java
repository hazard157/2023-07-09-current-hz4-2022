package com.hazard157.psx24.core.valeds.frames;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Фабрика редактора полей {@link IFrameable#frame()}.
 *
 * @author hazard157
 */
public class ValedAvFrameFactory
    extends AbstractValedControlFactory {

  /**
   * Factory name.
   */
  public static final String FACTORY_NAME = "ValedAvFrameFactory"; //$NON-NLS-1$

  /**
   * Factory singleton.
   */
  public static final ValedAvFrameFactory FACTORY = new ValedAvFrameFactory();

  private ValedAvFrameFactory() {
    super( FACTORY_NAME );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
    return new ValedAvFrameEditor( aContext );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IValedControl<IAtomicValue> doCreateViewer( ITsGuiContext aContext ) {
    return new ValedAvFrameViewer( aContext );
  }

}
