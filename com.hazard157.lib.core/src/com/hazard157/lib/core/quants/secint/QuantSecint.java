package com.hazard157.lib.core.quants.secint;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

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
    TsValobjUtils.registerKeeperIfNone( SecintKeeper.KEEPER_ID, SecintKeeper.KEEPER );
    TsValobjUtils.registerKeeperIfNone( SecintsListKeeper.ID, SecintsListKeeper.KEEPER );
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
