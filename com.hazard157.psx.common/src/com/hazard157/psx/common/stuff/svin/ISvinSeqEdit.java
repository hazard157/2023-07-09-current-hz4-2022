package com.hazard157.psx.common.stuff.svin;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * An ediable extension of {@link ISvinSeq}.
 *
 * @author hazard157
 */
public interface ISvinSeqEdit
    extends ISvinSeq, IGenericChangeEventCapable {

  /**
   * Returns editable list of SVINs making this set.
   *
   * @return {@link IListEdit}&lt;{@link Svin}&gt; - editable list of SVINs
   */
  @Override
  IListEdit<Svin> svins();

  /**
   * Returns reorderer for {@link #svins()}.
   *
   * @return {@link IListReorderer}&lt;{@link Svin}&gt; - SVINs list reorderer
   */
  IListReorderer<Svin> reorderer();

}
