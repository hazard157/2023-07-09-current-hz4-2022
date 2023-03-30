package com.hazard157.psx.proj3.todos.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Хранитель объектов типа {@link IFulfilStage}.
 *
 * @author hazard157
 */
class FulfilStageKeeper
    extends AbstractEntityKeeper<IFulfilStage> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<IFulfilStage> KEEPER = new FulfilStageKeeper();

  private FulfilStageKeeper() {
    super( IFulfilStage.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IFulfilStage aEntity ) {
    aSw.writeTimestamp( aEntity.when() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.description() );
  }

  @Override
  protected IFulfilStage doRead( IStrioReader aSr ) {
    long when = aSr.readTimestamp();
    aSr.ensureSeparatorChar();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    return new FulfilStage( when, name, description );
  }

}
