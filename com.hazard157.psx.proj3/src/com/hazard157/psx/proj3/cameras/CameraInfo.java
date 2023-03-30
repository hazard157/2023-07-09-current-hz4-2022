package com.hazard157.psx.proj3.cameras;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Информация о камере, используемой для съемки.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public final class CameraInfo {

  private final String      name;
  private final String      description;
  private final boolean     isAvailable;
  private final ECameraKind kind;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aName String - название
   * @param aDescription String - описание
   * @param aIsAvailable boolean - признак, что камера еще доступна для съемок
   * @param aKind {@link ECameraKind} - вид камеры
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public CameraInfo( String aName, String aDescription, boolean aIsAvailable, ECameraKind aKind ) {
    TsNullArgumentRtException.checkNull( aName, aDescription, aKind );
    name = aName;
    description = aDescription;
    isAvailable = aIsAvailable;
    kind = aKind;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает название камеры.
   *
   * @return String - название камеры
   */
  public String name() {
    return name;
  }

  /**
   * Возвращает описание камеры.
   *
   * @return String - описание камеры
   */
  public String description() {
    return description;
  }

  /**
   * Возвращает признак, что камера еще доступна для съемок.
   *
   * @return boolean - признак, что камера еще доступна для съемок
   */
  public boolean isStillAvailable() {
    return isAvailable;
  }

  /**
   * Возвращает вид камеры.
   *
   * @return String - вид камеры
   */
  public ECameraKind kind() {
    return kind;
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
    if( aObj instanceof CameraInfo ) {
      CameraInfo that = (CameraInfo)aObj;
      return isAvailable == that.isAvailable && name.equals( that.name ) && description.equals( that.description )
          && this.kind.equals( that.kind );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = TsLibUtils.PRIME * result + name.hashCode();
    result = TsLibUtils.PRIME * result + description.hashCode();
    result = TsLibUtils.PRIME * result + (isAvailable ? 1 : 0);
    result = TsLibUtils.PRIME * result + kind.hashCode();
    return result;
  }

}
