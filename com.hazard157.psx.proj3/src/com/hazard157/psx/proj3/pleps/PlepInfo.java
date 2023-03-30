package com.hazard157.psx.proj3.pleps;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редко изменяемые свойсива IPlep.
 *
 * @author hazard157
 */
public final class PlepInfo {

  private final String name;
  private final String description;
  private final String place;

  /**
   * Конструктор.
   *
   * @param aName String - название
   * @param aDescription String - описание
   * @param aPlace String - планируемое место
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PlepInfo( String aName, String aDescription, String aPlace ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription, aPlace );
    name = aName;
    description = aDescription;
    place = aPlace;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает название эпизода.
   *
   * @return String - название эпизода
   */
  public String name() {
    return name;
  }

  /**
   * Возвращает описание эпизода.
   *
   * @return String - описание эпизода
   */
  public String description() {
    return description;
  }

  /**
   * Возвращает название планируемого места.
   *
   * @return String - название планируемого места
   */
  public String place() {
    return place;
  }

}
