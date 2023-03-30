package com.hazard157.psx.proj3.cameras.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.cameras.*;

/**
 * Хранитель объектов типа {@link Camera}.
 *
 * @author hazard157
 */
class CameraKeeper
    extends AbstractEntityKeeper<Camera> {

  /**
   * Хранение {@link Camera} в текстовом представлении.
   */
  public static final IEntityKeeper<Camera> KEEPER = new CameraKeeper();

  private CameraKeeper() {
    super( Camera.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, Camera aEntity ) {
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    CameraInfoKeeper.KEEPER.write( aSw, aEntity.info() );
  }

  @Override
  protected Camera doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    CameraInfo info = CameraInfoKeeper.KEEPER.read( aSr );
    return new Camera( id, info );
  }

}
