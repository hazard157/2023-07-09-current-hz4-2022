package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Immutable class - information about {@link IScene}.
 *
 * @author hazard157
 */
public final class SceneInfo
    implements IFrameable {

  /**
   * The singleton of the non-existing scene.
   */
  // public static SceneInfo NULL = new SceneInfo( TsLibUtils.EMPTY_STRING, IFrame.NONE );

  private final String name;
  private final IFrame frame;

  /**
   * Constructor.
   *
   * @param aName String - the scene name
   * @param aFrame {@link IFrame} - illustrative frame
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SceneInfo( String aName, IFrame aFrame ) {
    TsNullArgumentRtException.checkNulls( aName, aFrame );
    name = aName;
    frame = aFrame;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the scene name.
   *
   * @return String - the scene name
   */
  public String name() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // IFrameable
  //

  @Override
  public IFrame frame() {
    return frame;
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
    if( aObj instanceof SceneInfo obj ) {
      return name.equals( obj.name ) && frame.equals( obj.frame );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + name.hashCode();
    result = TsLibUtils.PRIME * result + frame.hashCode();
    return result;
  }

}
