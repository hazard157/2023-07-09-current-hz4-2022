package com.hazard157.psx.proj3.episodes.proplines.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Хранитель объектов типа {@link PlaneGuide}.
 *
 * @author hazard157
 */
public class PlaneGuideKeeper
    extends AbstractEntityKeeper<PlaneGuide> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<PlaneGuide> KEEPER = new PlaneGuideKeeper();

  private PlaneGuideKeeper() {
    super( PlaneGuide.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, PlaneGuide aEntity ) {
    aSw.writeAsIs( aEntity.cameraId() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    Frame.KEEPER.write( aSw, aEntity.frame() );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( aEntity.isNaturallyLong() );
  }

  @Override
  protected PlaneGuide doRead( IStrioReader aSr ) {
    String camId = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IFrame frame = Frame.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    boolean isLong = aSr.readBoolean();
    return new PlaneGuide( camId, name, frame, isLong );
  }

}
