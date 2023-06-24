package com.hazard157.lib.core.excl_done.vulgar;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link EVulgarness} quant.
 *
 * @author hazard157
 */
public final class QuantVulgarness
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantVulgarness() {
    super( QuantVulgarness.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EVulgarness.KEEPER_ID, EVulgarness.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new VulgarnessM5Model() );
  }

}
