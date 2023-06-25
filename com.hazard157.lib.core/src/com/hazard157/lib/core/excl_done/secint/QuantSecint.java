package com.hazard157.lib.core.excl_done.secint;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Квант работы с интервалами в виедоматериалах.
 *
 * @author hazard157
 */
public class QuantSecint
    extends AbstractQuant {

  /**
   * Конструктор.
   */
  public QuantSecint() {
    super( QuantSecint.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( Secint.KEEPER_ID, Secint.KEEPER );
    TsValobjUtils.registerKeeperIfNone( SecintsList.KEEPER_ID, SecintsList.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // FIXME ISecintM5Constants.init( appContext() );
    // m5().addModel( new SecintM5Model() );
    // ValedControlFactoriesRegistry vcfRegistry = appContext().get( ValedControlFactoriesRegistry.class );
    // vcfRegistry.registerFactory( ValedSecintFactory.FACTORY );
    // vcfRegistry.registerFactory( ValedAvSecintFactory.FACTORY );
  }

}
