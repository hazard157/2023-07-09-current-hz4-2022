package com.hazard157.psx.proj3.cameras;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Camera properties.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class CameraInfo {

  private final String      name;
  private final String      description;
  private final boolean     isAvailable;
  private final ECameraKind kind;

  /**
   * Constructor.
   *
   * @param aName String - ID
   * @param aDescription String - name
   * @param aIsAvailable boolean - availability flag
   * @param aKind {@link ECameraKind} - the camera kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
   * Returns the title of the camera.
   *
   * @return String - camera name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the description of the camera.
   *
   * @return String - camera description
   */
  public String description() {
    return description;
  }

  /**
   * Returns the indication that the camera is still available for filming.
   *
   * @return boolean - a sign that the camera is still available for filming
   */
  public boolean isStillAvailable() {
    return isAvailable;
  }

  /**
   * Returns the camera kind.
   *
   * @return String - camera kind
   */
  public ECameraKind kind() {
    return kind;
  }

  // ------------------------------------------------------------------------------------
  // Object
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
    if( aObj instanceof CameraInfo that ) {
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
