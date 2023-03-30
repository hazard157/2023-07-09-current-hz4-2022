package com.hazard157.psx.proj3.todos;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Задача - что надо сделать.
 * <p>
 * Реализует интерфейс {@link Comparable}, сравнивая по возрастанию {@link #creationTime()}.
 *
 * @author hazard157
 */
public interface ITodo
    extends IGenericChangeEventCapable, Comparable<ITodo> {

  /**
   * Возвращает уникальный идентификатор задачи.
   *
   * @return long - идентификатор задачи
   */
  long id();

  /**
   * Возвращает момент времени создания задачи.
   * <p>
   * Момент создания задается с точностью до миллисекунд, неизменяем, и является уникальным идентификатором задачи, то
   * есть, метод возвращает {@link #id()}.
   *
   * @return long - момент времени создания задачи
   */
  long creationTime();

  /**
   * Возвращает важность (приоритет) задачи.
   *
   * @return {@link EPriority} - важность задачи
   */
  EPriority priority();

  /**
   * Задает приоритет (важность) задачи.
   *
   * @param aPriority {@link EPriority} - важность задачи
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setPriority( EPriority aPriority );

  /**
   * Возвращает текст задачи.
   *
   * @return String - текст задачи
   */
  String text();

  /**
   * Возвращает детальные пояснения к задаче.
   *
   * @return String - заметки, пояснения к задаче
   */
  String note();

  /**
   * Задает тексты задачи.
   *
   * @param aText String - текст задачи
   * @param aNote String - заметки, пояснения к задаче
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setTexts( String aText, String aNote );

  /**
   * Возвращает признак выполнения задачи.
   *
   * @return boolean - признак выполнения задачи
   */
  boolean isDone();

  /**
   * Задает признак выполнения задачи.
   *
   * @param isDone boolean - признак выполнения задачи
   */
  void setDone( boolean isDone );

  /**
   * Возвращает список связанных задач.
   *
   * @return {@link ILongList} - список идентификаторов связанных задач
   */
  IList<Long> relatedTodoIds();

  /**
   * Добавляет связанную задачу.
   * <p>
   * Если задача с таким идентификатором уже есть в списке, метод ничего не делает.
   *
   * @param aTodoId long - идентификатор удаляемой задачи
   */
  void addRelatedTodo( long aTodoId );

  /**
   * Удаляет связанную задачу.
   * <p>
   * Если задачи с таким идентификатором нет в списке, метод ничего не делает.
   *
   * @param aTodoId long - идентификатор удаляемой задачи
   */
  void removeRelatedTodo( long aTodoId );

  /**
   * Возвращает напоминалку к делу.
   *
   * @return {@link IReminder} - напоминалка
   */
  IReminder reminder();

  /**
   * Задает напоминалку.
   *
   * @param aReminder {@link IReminder} - напоминалка
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setReminder( IReminder aReminder );

  /**
   * Возвращает список выполненых этапов работ.
   *
   * @return {@link INotifierListBasicEdit}&lt;{@link IFulfilStage}&gt; - список выполненых этапов работ
   */
  INotifierListBasicEdit<IFulfilStage> fulfilStages();

}
