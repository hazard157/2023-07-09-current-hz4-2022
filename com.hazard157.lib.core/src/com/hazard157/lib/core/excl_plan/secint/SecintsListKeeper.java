package com.hazard157.lib.core.excl_plan.secint;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link ISecintsList}.
 *
 * @author hazard157
 */
public class SecintsListKeeper
    extends AbstractEntityKeeper<ISecintsList> {

  /**
   * Идентификатор регистрации хранителя в {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)}.
   */
  public static final String ID = "SecintsList"; //$NON-NLS-1$

  /**
   * Жкземпляр-синглтон хранителя.
   */
  public static IEntityKeeper<ISecintsList> KEEPER = new SecintsListKeeper();

  private SecintsListKeeper() {
    super( ISecintsList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  @Override
  protected void doWrite( IStrioWriter aDw, ISecintsList aEntity ) {
    StrioUtils.writeCollection( aDw, TsLibUtils.EMPTY_STRING, aEntity, SecintKeeper.KEEPER, false );
  }

  @Override
  protected ISecintsList doRead( IStrioReader aDr ) {
    IList<Secint> ll = StrioUtils.readCollection( aDr, TsLibUtils.EMPTY_STRING, SecintKeeper.KEEPER );
    return new SecintsList( ll );
  }

}
