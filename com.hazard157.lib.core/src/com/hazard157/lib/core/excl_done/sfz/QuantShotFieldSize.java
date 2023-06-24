package com.hazard157.lib.core.excl_done.sfz;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link EShotFieldSize} quant.
 *
 * @author hazard157
 */
public final class QuantShotFieldSize
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantShotFieldSize() {
    super( QuantShotFieldSize.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EShotFieldSize.KEEPER_ID, EShotFieldSize.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new ShotFieldSizeM5Model() );
  }

}
