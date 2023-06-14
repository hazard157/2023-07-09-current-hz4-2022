package com.hazard157.prisex24;

import org.toxsoft.core.tsgui.bricks.ctx.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Extending {@link ITsGuiContextable} with PSX services.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxGuiContextable
    extends ITsGuiContextable {

  default IUnitEpisodes unitEpisodes() {
    return tsContext().get( IUnitEpisodes.class );
  }

  default IMediaPlayerService mediaPlayer() {
    return tsContext().get( IMediaPlayerService.class );
  }

}
