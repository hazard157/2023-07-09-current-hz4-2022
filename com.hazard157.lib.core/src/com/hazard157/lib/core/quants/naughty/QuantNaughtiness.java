package com.hazard157.lib.core.quants.naughty;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link ENaughtiness} quant.
 *
 * @author hazard157
 */
public final class QuantNaughtiness
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantNaughtiness() {
    super( QuantNaughtiness.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( ENaughtiness.KEEPER_ID, ENaughtiness.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new NaughtinessM5Model() );
  }

}
