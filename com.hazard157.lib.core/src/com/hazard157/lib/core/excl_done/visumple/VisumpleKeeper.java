package com.hazard157.lib.core.excl_done.visumple;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetKeeper;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link Visumple}.
 *
 * @author hazard157
 */
public class VisumpleKeeper
    extends AbstractEntityKeeper<Visumple> {

  /**
   * Идентификатор регистрации хранителя в {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)}.
   */
  public static final String KEEPER_ID = "Visumple"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<Visumple> KEEPER = new VisumpleKeeper();

  private VisumpleKeeper() {
    super( Visumple.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, Visumple aEntity ) {
    aSw.writeQuotedString( aEntity.filePath() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
  }

  @Override
  protected Visumple doRead( IStrioReader aSr ) {
    String filePath = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
    return new Visumple( filePath, params );
  }

}
