package com.hazard157.lib.core.quants.visumple;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link IVisumplesList} keeper.
 * <p>
 * Read value may be safely converted to {@link VisumplesList}.
 *
 * @author hazard157
 */
public class VisumplesListKeeper
    extends AbstractEntityKeeper<IVisumplesList> {

  /**
   * Идентификатор регистрации хранителя в {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)}.
   */
  public static final String KEEPER_ID = "VisumplesList"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<IVisumplesList> KEEPER = new VisumplesListKeeper();

  private VisumplesListKeeper() {
    super( IVisumplesList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IVisumplesList aEntity ) {
    StrioUtils.writeCollection( aSw, TsLibUtils.EMPTY_STRING, aEntity, VisumpleKeeper.KEEPER, true );
  }

  @Override
  protected IVisumplesList doRead( IStrioReader aSr ) {
    IList<Visumple> ll = StrioUtils.readCollection( aSr, TsLibUtils.EMPTY_STRING, VisumpleKeeper.KEEPER );
    return new VisumplesList( ll );
  }

}
