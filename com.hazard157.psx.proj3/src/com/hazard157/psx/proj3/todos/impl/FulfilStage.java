package com.hazard157.psx.proj3.todos.impl;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * Неизменяемая реализация {@link IFulfilStage}.
 *
 * @author hazard157
 */
public class FulfilStage
    implements IFulfilStage {

  private final long   when;
  private final String name;
  private final String description;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aWhen long - метка времени
   * @param aName String - название
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FulfilStage( long aWhen, String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    when = aWhen;
    name = aName;
    description = aDescription;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( IFulfilStage aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return Long.compare( when, aThat.when() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFulfilStage
  //

  @Override
  public long when() {
    return when;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return String.format( "%tF - %s", Long.valueOf( when ), name ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IFulfilStage ) {
      IFulfilStage that = (IFulfilStage)aThat;
      return when == that.when() && name.equals( that.name() ) && description.equals( that.description() );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (int)(when ^ (when >>> 32));
    result = TsLibUtils.PRIME * result + name.hashCode();
    result = TsLibUtils.PRIME * result + description.hashCode();
    return result;
  }

}
