package com.hazard157.lib.core.quants.visumple3;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Квант работы с {@link Visumple3}.
 * <p>
 * Вызов {@link #close()} не обязателен.
 *
 * @author hazard157
 */
public class QuantVisumple3
    extends AbstractQuant {

  /**
   * Конструктор.
   */
  public QuantVisumple3() {
    super( QuantVisumple3.class.getSimpleName() );
    TsValobjUtils.registerKeeperIfNone( Visumple3.KEEPER_ID, Visumple3.KEEPER );
    TsValobjUtils.registerKeeperIfNone( Visumples3List.KEEPER_ID, Visumples3List.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
