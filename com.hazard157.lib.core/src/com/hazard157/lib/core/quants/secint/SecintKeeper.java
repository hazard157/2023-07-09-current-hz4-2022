package com.hazard157.lib.core.quants.secint;

import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель сущностей типа {@link Secint}.
 *
 * @author hazard157
 */
public class SecintKeeper
    extends AbstractEntityKeeper<Secint> {

  /**
   * Идентификатор регистрации хранителя в {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)}.
   */
  public static final String KEEPER_ID = "Secint"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<Secint> KEEPER = new SecintKeeper();

  private SecintKeeper() {
    super( Secint.class, EEncloseMode.ENCLOSES_BASE_CLASS, Secint.NULL );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, Secint aEntity ) {
    writeMmSs( aSw, aEntity.start() );
    aSw.writeChar( CHAR_ITEM_SEPARATOR );
    writeMmSs( aSw, aEntity.end() );
  }

  @Override
  protected Secint doRead( IStrioReader aSr ) {
    int start = readMmSs( aSr );
    aSr.ensureChar( CHAR_ITEM_SEPARATOR );
    int end = readMmSs( aSr );
    return new Secint( start, end );
  }

}
