package com.hazard157.lib.core.quants.haircol;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link EHairColor} quant.
 *
 * @author hazard157
 */
public final class QuantHairColor
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantHairColor() {
    super( QuantHairColor.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( EHairColor.KEEPER_ID, EHairColor.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new HairColorM5Model() );
  }

}
