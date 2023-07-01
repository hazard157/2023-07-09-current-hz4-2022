package com.hazard157.lib.core.excl_done.visumple;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Квант работы с {@link Visumple}.
 * <p>
 * Вызов {@link #close()} не обязателен.
 *
 * @author hazard157
 */
public class QuantVisumple
    extends AbstractQuant {

  /**
   * Конструктор.
   */
  public QuantVisumple() {
    super( QuantVisumple.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( VisumpleKeeper.KEEPER_ID, VisumpleKeeper.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // TODO Auto-generated method stub

  }

}
