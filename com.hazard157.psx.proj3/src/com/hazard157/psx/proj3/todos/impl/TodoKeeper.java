package com.hazard157.psx.proj3.todos.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Хранитель объектов типа {@link ITodo}.
 *
 * @author hazard157
 */
public class TodoKeeper
    extends AbstractEntityKeeper<ITodo> {

  private static final String KW_STAGES = "FulfilStages"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ITodo> KEEPER = new TodoKeeper();

  TodoKeeper() {
    super( ITodo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, ITodo aEntity ) {
    aSw.writeTimestamp( aEntity.id() );
    aSw.writeSeparatorChar();
    EPriority.KEEPER.write( aSw, aEntity.priority() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.text() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.note() );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( aEntity.isDone() );
    aSw.writeSeparatorChar();
    // Long[] is stores as SET not ARRAY for backward compatibility with PSX4.exe
    aSw.writeChar( CHAR_SET_BEGIN );
    for( Long l : aEntity.relatedTodoIds() ) {
      aSw.writeLong( l.longValue() );
      aSw.writeSeparatorChar();
    }
    aSw.writeChar( CHAR_SET_END );
    aSw.writeSeparatorChar();
    ReminderKeeper.KEEPER.write( aSw, aEntity.reminder() );
    aSw.writeSeparatorChar();
    StrioUtils.writeCollection( aSw, KW_STAGES, aEntity.fulfilStages(), FulfilStageKeeper.KEEPER );
  }

  @Override
  protected ITodo doRead( IStrioReader aSr ) {
    long id = aSr.readTimestamp();
    aSr.ensureSeparatorChar();
    Todo t = new Todo( id );
    t.setPriority( EPriority.KEEPER.read( aSr ) );
    aSr.ensureSeparatorChar();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    t.setTexts( name, description );
    t.setDone( aSr.readBoolean() );
    aSr.ensureSeparatorChar();
    // Long[] is stores as SET not ARRAY for backward compatibility with PSX4.exe
    ILongListEdit relatedIds = new LongArrayList();
    if( aSr.readSetBegin() ) {
      do {
        relatedIds.add( aSr.readLong() );
      } while( aSr.readSetNext() );
    }
    for( int i = 0; i < relatedIds.size(); i++ ) {
      t.addRelatedTodo( relatedIds.getValue( i ) );
    }
    aSr.ensureSeparatorChar();
    IReminder reminder = ReminderKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    t.setReminder( reminder );
    t.fulfilStages().setAll( StrioUtils.readCollection( aSr, KW_STAGES, FulfilStageKeeper.KEEPER ) );
    return t;
  }

}
