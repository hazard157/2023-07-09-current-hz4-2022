package com.hazard157.psx.proj3.movies;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Дополнительная информация о трейлере.
 *
 * @author hazard157
 */
public class MovieInfo {

  private final String name;
  private final String description;
  private final int    plannedDuration;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aName String - название трейлера
   * @param aDescription String - описание трейлера
   * @param aPlannedDuration int - плановая длительность (секунды)
   */
  public MovieInfo( String aName, String aDescription, int aPlannedDuration ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    name = aName;
    description = aDescription;
    plannedDuration = aPlannedDuration;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает название.
   *
   * @return String - название
   */
  public String name() {
    return name;
  }

  /**
   * Возвращает описание.
   *
   * @return String - описание
   */
  public String description() {
    return description;
  }

  /**
   * Возвращает плановую длительность.
   *
   * @return int - плановая длительность трейлера в секундах
   */
  public int plannedDuration() {
    return plannedDuration;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof MovieInfo ) {
      MovieInfo that = (MovieInfo)aObj;
      if( this.plannedDuration == that.plannedDuration ) {
        return this.name.equals( that.name ) && this.description.equals( that.description );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + name.hashCode();
    result = PRIME * result + description.hashCode();
    result = PRIME * result + plannedDuration;
    return result;
  }

}
