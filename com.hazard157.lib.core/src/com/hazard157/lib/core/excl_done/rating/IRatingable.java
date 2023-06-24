package com.hazard157.lib.core.excl_done.rating;

/**
 * Смешиваемый интерфес сущности, имеющей оценку.
 *
 * @author hazard157
 */
public interface IRatingable {

  /**
   * Возвращает оценку сущности.
   *
   * @return {@link ERating} - оценка сущности
   */
  ERating rating();

}
