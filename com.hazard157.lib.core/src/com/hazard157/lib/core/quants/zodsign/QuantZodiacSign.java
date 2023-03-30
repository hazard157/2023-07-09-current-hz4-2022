package com.hazard157.lib.core.quants.zodsign;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link EZodiacSign} quant.
 *
 * @author hazard157
 */
public final class QuantZodiacSign
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantZodiacSign() {
    super( QuantZodiacSign.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EZodiacSign.KEEPER_ID, EZodiacSign.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new ZodiacSignM5Model() );
  }

}
