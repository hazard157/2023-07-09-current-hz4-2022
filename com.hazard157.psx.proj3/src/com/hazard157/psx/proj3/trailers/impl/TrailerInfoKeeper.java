package com.hazard157.psx.proj3.trailers.impl;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.trailers.*;

/**
 * Хранитель объектов типа {@link TrailerInfo}.
 *
 * @author hazard157
 */
class TrailerInfoKeeper
    extends AbstractEntityKeeper<TrailerInfo> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<TrailerInfo> KEEPER = new TrailerInfoKeeper();

  private TrailerInfoKeeper() {
    super( TrailerInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, TrailerInfo aEntity ) {
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.description() );
    aSw.writeSeparatorChar();
    HmsUtils.writeMmSs( aSw, aEntity.plannedDuration() );
  }

  @Override
  protected TrailerInfo doRead( IStrioReader aSr ) {
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    int plannedDuration = HmsUtils.readMmSs( aSr );
    return new TrailerInfo( name, description, plannedDuration );
  }

}
