package com.hazard157.psx.proj3.trailers;

import com.hazard157.psx.proj3.movies.*;

/**
 * Информация о трейлере, расширяет {@link MovieInfo}.
 *
 * @author hazard157
 */
public class TrailerInfo
    extends MovieInfo {

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aName String - название трейлера
   * @param aDescription String - описание трейлера
   * @param aPlannedDuration int - плановая длительность (секунды)
   */
  public TrailerInfo( String aName, String aDescription, int aPlannedDuration ) {
    super( aName, aDescription, aPlannedDuration );
  }

}
