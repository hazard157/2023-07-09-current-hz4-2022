package com.hazard157.prisex24.e4.services.selsvins;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.utils.frasel.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Service introduces concept of "selected SVINSs to be viewed as frames".
 *
 * @author hazard157
 */
public interface IPsxSelectedSvinsService {

  /**
   * Returns list of SVINs.
   *
   * @return {@link IList}&lt;{@link Svin}&gt; - list of SVINs
   */
  IList<Svin> svins();

  /**
   * Sets list of SVINs.
   *
   * @param aSvins {@link IList}&lt;{@link Svin}&gt; - list of SVINs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setSvins( IList<Svin> aSvins );

  /**
   * Returns the means to tune how the specified {@link #svins()} will be displayed as frames.
   * <p>
   * Note: user may override this settings at any time in by the frames viewer GUI.
   *
   * @return {@link ISvinFramesParams} - the SVINs to frames strategy
   */
  ISvinFramesParams framesSelectionParams();

  /**
   * Returns event manager informing of changes in {@link #svins()} list.
   * <p>
   * Note: {@link #framesSelectionParams()} has own eventer, so does not affects this one.
   *
   * @return {@link IGenericChangeEventer} - SVINs list change eventer
   */
  IGenericChangeEventer eventer();

  // ------------------------------------------------------------------------------------
  // Convenience inline methods

  /**
   * Set single SVIN.
   *
   * @param aSvin {@link Svin} - the SVIN, may be null
   */
  default void setSvin( Svin aSvin ) {
    if( aSvin != null ) {
      setSvins( new SingleItemList<>( aSvin ) );
      return;
    }
    setSvins( IList.EMPTY );
  }
}
