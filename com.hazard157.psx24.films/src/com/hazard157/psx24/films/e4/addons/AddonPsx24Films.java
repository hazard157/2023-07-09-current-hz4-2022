package com.hazard157.psx24.films.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.impl.*;

import com.hazard157.psx24.films.*;
import com.hazard157.psx24.films.e4.services.*;
import com.hazard157.psx24.films.glib.valeds.*;
import com.hazard157.psx24.films.m5.*;

/**
 * Адон приложения.
 *
 * @author goga
 */
public class AddonPsx24Films
    extends MwsAbstractAddon {

  /**
   * Конструктор.
   */
  public AddonPsx24Films() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ICurrentFilmService cfs = new CurrentFilmService();
    aAppContext.set( ICurrentFilmService.class, cfs );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsx24FilmsConstants.init( aWinContext );
    //
    IPsxFilmsService filmsService = new PsxFilmsService( aWinContext );
    aWinContext.set( IPsxFilmsService.class, filmsService );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new FilmM5Model() );
    //
    ValedControlFactoriesRegistry vreg = aWinContext.get( ValedControlFactoriesRegistry.class );
    vreg.registerFactory( ValedAvPsxKeywords.FACTORY );
  }

}
