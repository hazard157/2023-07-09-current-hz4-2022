package com.hazard157.psx24.explorer.unit;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Описание {@link Inquiry}.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public final class InquiryInfo {

  private final String name;
  private final String description;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aName String - название
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public InquiryInfo( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    name = aName;
    description = aDescription;
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

}
