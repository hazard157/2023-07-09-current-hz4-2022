package com.hazard157.lib.core.quants.secint;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link ISecintsList}.
 *
 * @author hazard157
 */
public interface ISecintsListEdit
    extends ISecintsList {

  /**
   * Adds an interval to the list, when it intersects or touches existing elements, merges them.
   *
   * @param aIn {@link Secint} - the interval to be added
   * @return boolean - <code>true</code> if list was changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean add( Secint aIn );

  /**
   * Removes an interval from the list, truncate existing elements if needed.
   *
   * @param aIn {@link Secint} - the interval to remove
   * @return boolean - <code>true</code> if list was changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean remove( Secint aIn );

  /**
   * Adds a list of intervals.
   *
   * @param aList {@link IList}&lt;{@link Secint}&gt; - the list to be added
   * @return boolean - <code>true</code> if list was changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean add( IList<Secint> aList );

  /**
   * Clears the list.
   */
  void clear();

}
