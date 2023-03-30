package com.hazard157.psx.proj3.trailers.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;

import com.hazard157.psx.proj3.movies.*;
import com.hazard157.psx.proj3.trailers.*;

/**
 * Хранитель объектов типа {@link Trailer}.
 *
 * @author hazard157
 */
class TrailerKeeper
    extends AbstractEntityKeeper<Trailer> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<Trailer> KEEPER = new TrailerKeeper();

  private TrailerKeeper() {
    super( Trailer.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  private static final String KW_CHUNKS = "Chunks"; //$NON-NLS-1$

  @Override
  protected void doWrite( IStrioWriter aSw, Trailer aEntity ) {
    aSw.incNewLine();
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    TrailerInfoKeeper.KEEPER.write( aSw, aEntity.info() );
    aSw.writeEol();
    StrioUtils.writeCollection( aSw, KW_CHUNKS, aEntity.chunks(), ChunkKeeper.KEEPER, true );
    aSw.decNewLine();
  }

  @Override
  protected Trailer doRead( IStrioReader aSr ) {
    String trailerId = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    TrailerInfo info = TrailerInfoKeeper.KEEPER.read( aSr );
    Trailer t = new Trailer( trailerId, info );
    t.chunks().setAll( StrioUtils.readCollection( aSr, KW_CHUNKS, ChunkKeeper.KEEPER ) );
    return t;
  }

}
