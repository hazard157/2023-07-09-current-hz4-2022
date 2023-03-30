package com.hazard157.psx.proj3.sourcevids.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Хранитель объектов типа {@link ISourceVideo}.
 *
 * @author hazard157
 */
class SourceVideoKeeper
    extends AbstractEntityKeeper<ISourceVideo> {

  /**
   * Хранение {@link ISourceVideo} в текстовом представлении.
   */
  public static final IEntityKeeper<ISourceVideo> KEEPER = new SourceVideoKeeper();

  private SourceVideoKeeper() {
    super( ISourceVideo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, ISourceVideo aEntity ) {
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    SourceVideoInfoKeeper.KEEPER.write( aSw, aEntity.info() );
  }

  @Override
  protected ISourceVideo doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    SourceVideoInfo info = SourceVideoInfoKeeper.KEEPER.read( aSr );
    return new SourceVideo( id, info );
  }

}
