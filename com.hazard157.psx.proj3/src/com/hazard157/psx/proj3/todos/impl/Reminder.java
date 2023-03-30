package com.hazard157.psx.proj3.todos.impl;

import static com.hazard157.psx.proj3.todos.impl.IPsxResources.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Неизменяемая реализация {@link IReminder}.
 *
 * @author hazard157
 */
public // сделан пока public для ReminderM3Model
class Reminder
    implements IReminder {

  private final boolean active;
  private final long    timestamp;
  private final String  message;

  /**
   * Конструктор.
   *
   * @param aIsActive boolean - призак активации напоминалки
   * @param aTime long - метка времени (миллисекунды с начала эпохи)
   * @param aMessage String - сообщение напоминалки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public Reminder( boolean aIsActive, long aTime, String aMessage ) {
    TsNullArgumentRtException.checkNull( aMessage );
    active = aIsActive;
    timestamp = aTime;
    message = aMessage;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IReminder
  //

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public long remindTimestamp() {
    return timestamp;
  }

  @Override
  public String message() {
    return message;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    if( active ) {
      Long tm = Long.valueOf( timestamp );
      return String.format( "%tF %tT - %s", tm, tm, message ); //$NON-NLS-1$
    }
    return STR_N_INCATIVE_REMONDER;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IReminder ) {
      IReminder that = (IReminder)aThat;
      if( active == that.isActive() && timestamp == that.remindTimestamp() ) {
        return message.equals( that.message() );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (active ? 1 : 0);
    result = TsLibUtils.PRIME * result + (int)(timestamp ^ (timestamp >>> 32));
    result = TsLibUtils.PRIME * result + message.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( IReminder aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return Long.compare( timestamp, aThat.remindTimestamp() );
  }

}
