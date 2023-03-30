package com.hazard157.psx.proj3.todos;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Менеджер задач.
 *
 * @author hazard157
 */
public interface IUnitTodos
    extends IProjDataUnit {

  /**
   * Возвращае список задач.
   *
   * @return ILongMap&lt;{@link ITodo}&gt; - карта "ИД задачи" - "задача"
   */
  ILongMap<ITodo> todos();

  /**
   * Добавляет задачу.
   *
   * @param aText String - текст задачи
   * @return {@link ITodo} - созданная задача
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент пустая строка
   */
  ITodo addTodo( String aText );

  /**
   * Удаляет задачу.
   * <p>
   * Если такой задачи нет, метод ничего не делает.
   *
   * @param aTodoId long - идентификатор удаляемой задачи
   */
  void removeTodo( long aTodoId );

}
