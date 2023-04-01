package com.hazard157.psx24.planning.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx24.planning.*;
import com.hazard157.psx24.planning.e4.services.*;
import com.hazard157.psx24.planning.m5.*;
import com.hazard157.psx24.planning.m5.plep.*;

/**
 * Адон приложения.
 *
 * @author hazard157
 */
public class AddonPsx24Planning
    extends MwsAbstractAddon {

  /**
   * Конструктор.
   */
  public AddonPsx24Planning() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ICurrentPlepService cps = new CurrentPlepService();
    aAppContext.set( ICurrentPlepService.class, cps );
    ICurrentStirService css = new CurrentStirService();
    aAppContext.set( ICurrentStirService.class, css );
    ICurrentTrackService cts = new CurrentTrackService();
    aAppContext.set( ICurrentTrackService.class, cts );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsx42PlaningConstants.init( aWinContext );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new PlepM5Model() );
    m5.addModel( new StirM5Model() );
    m5.addModel( new TrackM5Model() );
  }

}
