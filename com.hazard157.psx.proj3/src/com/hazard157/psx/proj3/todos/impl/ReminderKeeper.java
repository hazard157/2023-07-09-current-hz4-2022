package com.hazard157.psx.proj3.todos.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Хранитель объектов типа {@link IReminder}.
 *
 * @author hazard157
 */
class ReminderKeeper
    extends AbstractEntityKeeper<IReminder> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<IReminder> KEEPER = new ReminderKeeper();

  private ReminderKeeper() {
    super( IReminder.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IReminder aEntity ) {
    aSw.writeBoolean( aEntity.isActive() );
    aSw.writeSeparatorChar();
    aSw.writeTimestamp( aEntity.remindTimestamp() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.message() );
  }

  @Override
  protected IReminder doRead( IStrioReader aSr ) {
    boolean isActive = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    long timestamp = aSr.readTimestamp();
    aSr.ensureSeparatorChar();
    String message = aSr.readQuotedString();
    return new Reminder( isActive, timestamp, message );
  }

}
