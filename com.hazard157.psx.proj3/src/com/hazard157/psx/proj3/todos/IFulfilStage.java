package com.hazard157.psx.proj3.todos;

/**
 * Этап выполнения дела.
 * <p>
 * Реализует интерфейс {@link Comparable}, сравнивая по возрастанию {@link #when()}.
 *
 * @author hazard157
 */
public interface IFulfilStage
    extends Comparable<IFulfilStage> {

  /**
   * Время выполнения этапа работ по делу.
   *
   * @return long - метка времени (миллисекунды с начала эпохи)
   */
  long when();

  /**
   * Возвращает название выполненного этапа.
   *
   * @return String - название выполненного этапа
   */
  String name();

  /**
   * Возвращат описание выполненного этапа.
   *
   * @return String - описание выполненного этапа
   */
  String description();

}
