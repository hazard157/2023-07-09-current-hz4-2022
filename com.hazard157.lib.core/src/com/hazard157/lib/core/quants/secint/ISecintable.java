package com.hazard157.lib.core.quants.secint;

/**
 * Примешиваемый интерфейс сущностей, которые определены на каком-то интервале {@link Secint}.
 *
 * @author hazard157
 */
public interface ISecintable {

  /**
   * Возвращает интервал, на которой определена сущность.
   * 
   * @return {@link Secint} - интервал в секундах
   */
  Secint interval();

}
