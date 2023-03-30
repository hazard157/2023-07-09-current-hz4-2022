package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый интерфейс всех линии свойств видеоряда любыми сущностями.
 *
 * @author hazard157
 */
public interface IAnyPropLineBase
    extends IGenericChangeEventCapable, IKeepableEntity, ITsClearable {

  /**
   * Возвращает длительность линии.
   *
   * @return int - длительность линии в секундах
   */
  int duration();

  /**
   * Задает длительность линии.
   *
   * @param aDuration int - длительность линии в секундах
   * @throws TsIllegalArgumentRtException длительность < 1
   */
  void setDuration( int aDuration );

}
