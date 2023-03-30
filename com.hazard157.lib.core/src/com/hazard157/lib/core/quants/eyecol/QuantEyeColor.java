package com.hazard157.lib.core.quants.eyecol;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link EEyeColor} quant.
 *
 * @author hazard157
 */
public final class QuantEyeColor
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantEyeColor() {
    super( QuantEyeColor.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EEyeColor.KEEPER_ID, EEyeColor.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new EyeColorM5Model() );
  }

}
