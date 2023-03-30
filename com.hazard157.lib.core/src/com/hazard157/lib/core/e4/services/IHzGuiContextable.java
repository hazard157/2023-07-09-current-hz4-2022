package com.hazard157.lib.core.e4.services;

import org.toxsoft.core.tsgui.bricks.ctx.*;

import com.hazard157.lib.core.e4.services.mps.*;

/**
 * Add access to HZ core resources via context.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IHzGuiContextable
    extends ITsGuiContextable {

  default IMediaPlayerService mps() {
    return tsContext().get( IMediaPlayerService.class );
  }

}
