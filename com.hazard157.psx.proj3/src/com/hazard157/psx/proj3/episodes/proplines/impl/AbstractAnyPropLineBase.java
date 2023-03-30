package com.hazard157.psx.proj3.episodes.proplines.impl;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Базовая реализация {@link IAnyPropLineBase}.
 *
 * @author hazard157
 */
public abstract class AbstractAnyPropLineBase
    implements IAnyPropLineBase {

  protected final GenericChangeEventer eventer;
  private int                          duration = 1;

  protected AbstractAnyPropLineBase() {
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  protected void fireChangeEvent() {
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IAnyPropLineBase
  //

  @Override
  final public int duration() {
    return duration;
  }

  @Override
  final public void setDuration( int aDuration ) {
    TsIllegalArgumentRtException.checkTrue( aDuration < 1 );
    if( duration != aDuration ) {
      duration = aDuration;
      afterDurationChanged();
      fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может отработать изменение длительности линии.
   * <p>
   * Вызывается после изменения длительности в методе {@link #setDuration(int)}.
   */
  protected void afterDurationChanged() {
    // nop
  }

}
