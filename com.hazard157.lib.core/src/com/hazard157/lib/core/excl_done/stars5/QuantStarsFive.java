package com.hazard157.lib.core.excl_done.stars5;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link EStarsFive} quant.
 *
 * @author hazard157
 */
public final class QuantStarsFive
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantStarsFive() {
    super( QuantStarsFive.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EStarsFive.KEEPER_ID, EStarsFive.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new StarsFiveM5Model() );
  }

}
