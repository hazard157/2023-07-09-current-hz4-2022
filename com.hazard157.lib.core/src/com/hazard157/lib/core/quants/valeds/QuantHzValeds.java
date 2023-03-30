package com.hazard157.lib.core.quants.valeds;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.valeds.radioprop.*;

/**
 * Quant HZ valeds quant.
 *
 * @author hazard157
 */
public class QuantHzValeds
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantHzValeds() {
    super( QuantHzValeds.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    //
    IValedControlFactoriesRegistry vfReg = aWinContext.get( IValedControlFactoriesRegistry.class );
    TsInternalErrorRtException.checkNull( vfReg );
    vfReg.registerFactory( ValedAvValobjRadioPropEnumStars.FACTORY );
    vfReg.registerFactory( ValedRadioPropEnumStars.FACTORY );
  }

}
