package com.hazard157.psx24.core.e4.services.currep;

import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * The concept of "current episode in the application".
 *
 * @author hazard157
 */
public interface ICurrentEpisodeService
    extends ICurrentEntityService<IEpisode> {

  /**
   * Selectes episode in the given direction.
   *
   * @param aDirection {@link ETsCollMove} - the direction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void select( ETsCollMove aDirection );

}
