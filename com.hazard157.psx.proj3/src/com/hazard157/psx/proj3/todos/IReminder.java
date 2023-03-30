package com.hazard157.psx.proj3.todos;

import org.toxsoft.core.tslib.utils.*;

/**
 * Напоминалка к делу.
 * <p>
 * Реализуем интерфейс {@link Comparable}, сравнивая напоминалки по возрастанию времени {@link #remindTimestamp()}.
 *
 * @author hazard157
 */
public interface IReminder
    extends Comparable<IReminder> {

  /**
   * Отсутствующее напоминане.
   */
  IReminder NONE = new InternalNoneReminder();

  /**
   * Возвращает призак активации напоминалки.
   *
   * @return boolean - призак активации напоминалки
   */
  boolean isActive();

  /**
   * Момент времени, когда надо напомнить.
   *
   * @return long - метка времени (миллисекунды с начала эпохи)
   */
  long remindTimestamp();

  /**
   * Возвращает сообщение напоминалки.
   *
   * @return String - сообщение напоминалки
   */
  String message();

}

class InternalNoneReminder
    implements IReminder {

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  public long remindTimestamp() {
    return 0;
  }

  @Override
  public String message() {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public int compareTo( IReminder aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return -1;
  }

}
