package com.hazard157.psx24.timeline.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx24.timeline.*;

/**
 * Module addon.
 *
 * @author goga
 */
public class AddonPsx24Timeline
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonPsx24Timeline() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsx24TimelineConstants.init( aWinContext );
  }

}
