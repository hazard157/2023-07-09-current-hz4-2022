package com.hazard157.psx.proj3.todos.impl;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.impl.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Реализация {@link IUnitTodos}.
 *
 * @author hazard157
 */
public class UnitTodos
    extends AbstractProjDataUnit
    implements IUnitTodos {

  private final ILongMapEdit<ITodo> todos = new SortedLongMap<>();

  /**
   * Конструктор.
   */
  public UnitTodos() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private final long getUniqueIdForNewTodo() {
    long idBase = System.currentTimeMillis();
    for( long i = 0; i < 1000; i++ ) {
      long id = idBase + i;
      if( !todos.hasKey( id ) ) {
        return id;
      }
    }
    throw new TsIllegalStateRtException();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepableEntity
  //

  @Override
  protected void doWrite( IStrioWriter aDw ) {
    TodoKeeper.KEEPER.writeColl( aDw, todos, true );
  }

  @Override
  protected void doRead( IStrioReader aDr ) {
    IList<ITodo> list = TodoKeeper.KEEPER.readColl( aDr );
    todos.clear();
    for( ITodo t : list ) {
      todos.put( t.id(), t );
      t.genericChangeEventer().addListener( genericChangeEventer() );
    }
    genericChangeEventer().fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsClearableCollection
  //

  @Override
  protected void doClear() {
    if( !todos.isEmpty() ) {
      todos.clear();
      genericChangeEventer().fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITodo2Manager
  //

  @Override
  public ILongMap<ITodo> todos() {
    return todos;
  }

  @Override
  public ITodo addTodo( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    long id = getUniqueIdForNewTodo();
    Todo t = new Todo( id );
    t.genericChangeEventer().addListener( genericChangeEventer() );
    todos.put( id, t );
    genericChangeEventer().fireChangeEvent();
    return t;
  }

  @Override
  public void removeTodo( long aTodoId ) {
    ITodo t = todos.removeByKey( aTodoId );
    if( t == null ) {
      return;
    }
    t.genericChangeEventer().removeListener( genericChangeEventer() );
    genericChangeEventer().fireChangeEvent();
  }

}
