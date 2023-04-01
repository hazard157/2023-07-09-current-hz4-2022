package com.hazard157.psx24.core.e4.services.selsvins;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;

/**
 * Service introduces concept of "selectted svins to be viewed as frames".
 *
 * @author hazard157
 */
public interface IPsxSelectedSvinsService {

  /**
   * Returns list of svins.
   *
   * @return {@link IList}&lt;{@link Svin}&gt; - list of svins
   */
  IList<Svin> svins();

  /**
   * Sets list of svins.
   *
   * @param aSvins {@link IList}&lt;{@link Svin}&gt; - list of svins
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setSvins( IList<Svin> aSvins );

  /**
   * Set single svin.
   *
   * @param aSvin {@link Svin} - the svin, may ne null
   */
  default void setSvin( Svin aSvin ) {
    if( aSvin != null ) {
      setSvins( new SingleItemList<>( aSvin ) );
      return;
    }
    setSvins( IList.EMPTY );
  }

  /**
   * Returns event manager informing of changes in {@link #svins()} list.
   *
   * @return {@link IGenericChangeEventer} - svins list vhange eventer
   */
  IGenericChangeEventer eventer();

}
