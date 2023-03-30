package com.hazard157.psx.proj3.todos.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Реализация {@link ITodo}.
 *
 * @author hazard157
 */
public class Todo
    implements ITodo {

  private final ITsCollectionChangeListener childListsChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      eventer.fireChangeEvent();
    }
  };

  final GenericChangeEventer                         eventer;
  private final INotifierListBasicEdit<IFulfilStage> fulfilStages =
      new NotifierListBasicEditWrapper<>( new SortedElemLinkedBundleList<IFulfilStage>() );

  private final long               id;
  private EPriority                priority       = EPriority.NORMAL;
  private String                   text           = EMPTY_STRING;
  private String                   note           = EMPTY_STRING;
  private boolean                  done           = false;
  private final ILongListBasicEdit relatedTodoIds = new SortedLongLinkedBundleList();
  private IReminder                reminder       = IReminder.NONE;

  Todo( long aId ) {
    eventer = new GenericChangeEventer( this );
    fulfilStages.addCollectionChangeListener( childListsChangeListener );
    id = aId;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //
  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( ITodo aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return Long.compare( id, aThat.id() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITodo2
  //

  @Override
  public long id() {
    return id;
  }

  @Override
  public long creationTime() {
    return id;
  }

  @Override
  public EPriority priority() {
    return priority;
  }

  @Override
  public void setPriority( EPriority aPriority ) {
    TsNullArgumentRtException.checkNull( aPriority );
    if( priority != aPriority ) {
      priority = aPriority;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public String text() {
    return text;
  }

  @Override
  public String note() {
    return note;
  }

  @Override
  public void setTexts( String aText, String aNotes ) {
    TsNullArgumentRtException.checkNulls( aText, aNotes );
    if( !text.equals( aText ) || !note.equals( aNotes ) ) {
      text = aText;
      note = aNotes;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public void setDone( boolean aIsDone ) {
    if( done != aIsDone ) {
      done = aIsDone;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public IList<Long> relatedTodoIds() {
    IListEdit<Long> ll = new ElemLinkedBundleList<>();
    for( int i = 0; i < relatedTodoIds.size(); i++ ) {
      ll.add( relatedTodoIds.get( i ) );
    }
    return ll;
  }

  @Override
  public void addRelatedTodo( long aTodoId ) {
    if( !relatedTodoIds.hasValue( aTodoId ) ) {
      relatedTodoIds.add( aTodoId );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void removeRelatedTodo( long aTodoId ) {
    if( relatedTodoIds.removeValue( aTodoId ) >= 0 ) {
      eventer.fireChangeEvent();
    }
  }

  @Override
  public IReminder reminder() {
    return reminder;
  }

  @Override
  public void setReminder( IReminder aReminder ) {
    TsNullArgumentRtException.checkNull( aReminder );
    if( !reminder.equals( aReminder ) ) {
      reminder = aReminder;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public INotifierListBasicEdit<IFulfilStage> fulfilStages() {
    return fulfilStages;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @Override
  public String toString() {
    return Todo.class.getSimpleName() + ": " + text; //$NON-NLS-1$
  }

}
