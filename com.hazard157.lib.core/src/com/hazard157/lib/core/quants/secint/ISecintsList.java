package com.hazard157.lib.core.quants.secint;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A sorted list of non-overlapping intervals.
 *
 * @author hazard157
 */
public interface ISecintsList
    extends IList<Secint> {

  /**
   * Returns the sorted list of items.
   *
   * @return {@link IList}&lt;{@link Secint}&gt; - items list
   */
  IList<Secint> items();

  /**
   * Returns a list of gaps for the specified interval <code>aIn</code>.
   *
   * @param aIn {@link Secint} - the specified interval
   * @return IList&lt;{@link Secint}&gt; - the list of gaps
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<Secint> listGaps( Secint aIn );

  /**
   * Retuns change eventer.
   *
   * @return {@link IGenericChangeEventer} - change eventer
   */
  IGenericChangeEventer eventer();

}
