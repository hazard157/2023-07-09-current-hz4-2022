package com.hazard157.psx.proj3.cameras;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Базовый интерфейс камеры.
 * <p>
 * Реализует общие для люой реализации эпизода методы, что для psx3, что для psx5 или еще будущих реализации.
 *
 * @author hazard157
 */
public interface ICamera
    extends IStridable {

  /**
   * Возвращает признак, что камера еще доступна для съемок.
   *
   * @return boolean - признак, что камера еще доступна для съемок
   */
  boolean isCamAvailable();

  /**
   * Возвращает вид камеры.
   *
   * @return String - вид камеры
   */
  ECameraKind kind();

}
