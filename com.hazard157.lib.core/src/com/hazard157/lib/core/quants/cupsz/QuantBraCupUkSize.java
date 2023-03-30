package com.hazard157.lib.core.quants.cupsz;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link EBraCupUkSize} quant.
 *
 * @author hazard157
 */
public final class QuantBraCupUkSize
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantBraCupUkSize() {
    super( QuantBraCupUkSize.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EBraCupUkSize.KEEPER_ID, EBraCupUkSize.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new BraCupUkSizeM5Model() );
  }

}
