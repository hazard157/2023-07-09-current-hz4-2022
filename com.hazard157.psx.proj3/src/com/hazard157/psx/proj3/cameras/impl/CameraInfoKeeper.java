package com.hazard157.psx.proj3.cameras.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.cameras.*;

/**
 * Хранитель объектов типа {@link CameraInfo}.
 *
 * @author hazard157
 */
class CameraInfoKeeper
    extends AbstractEntityKeeper<CameraInfo> {

  /**
   * Хранение {@link CameraInfo} в текстовом представлении.
   */
  static final IEntityKeeper<CameraInfo> KEEPER = new CameraInfoKeeper();

  private CameraInfoKeeper() {
    super( CameraInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, CameraInfo aEntity ) {
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.description() );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( aEntity.isStillAvailable() );
    aSw.writeSeparatorChar();
    ECameraKind.KEEPER.write( aSw, aEntity.kind() );
  }

  @Override
  protected CameraInfo doRead( IStrioReader aSr ) {
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String descr = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    boolean isAvailable = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    ECameraKind kind = ECameraKind.KEEPER.read( aSr );
    return new CameraInfo( name, descr, isAvailable, kind );
  }

}
