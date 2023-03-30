package com.hazard157.lib.core.quants.rating;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link ERating} quant.
 *
 * @author hazard157
 */
public final class QuantRating
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantRating() {
    super( QuantRating.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( ERating.KEEPER_ID, ERating.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new RatingM5Model() );
  }

}
