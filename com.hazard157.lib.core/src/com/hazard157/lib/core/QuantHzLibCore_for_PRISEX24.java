package com.hazard157.lib.core;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;

import com.hazard157.lib.core.bricks.kwmark.*;
import com.hazard157.lib.core.e4.services.mps.*;
import com.hazard157.lib.core.legacy.valeds.fileimg.*;
import com.hazard157.lib.core.legacy.valeds.hms.*;
import com.hazard157.lib.core.quants.cupsz.*;
import com.hazard157.lib.core.quants.eyecol.*;
import com.hazard157.lib.core.quants.haircol.*;
import com.hazard157.lib.core.quants.naughty.*;
import com.hazard157.lib.core.quants.rating.*;
import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.quants.secint.m5.*;
import com.hazard157.lib.core.quants.secint.valed.*;
import com.hazard157.lib.core.quants.stars5.*;
import com.hazard157.lib.core.quants.zodsign.*;

/**
 * Plugin quant.
 *
 * @author hazard157
 */
public class QuantHzLibCore_for_PRISEX24
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantHzLibCore_for_PRISEX24() {
    super( QuantHzLibCore_for_PRISEX24.class.getSimpleName() );
    //
    registerQuant( new QuantRating() );
    registerQuant( new QuantStarsFive() );
    // registerQuant( new QuantVulgarness() );
    registerQuant( new QuantNaughtiness() );
    registerQuant( new QuantSecint() );
    // registerQuant( new QuantVisumple() );
    // registerQuant( new QuantVisumple3() );
    // registerQuant( new QuantShotFieldSize() );
    registerQuant( new QuantBraCupUkSize() );
    registerQuant( new QuantZodiacSign() );
    registerQuant( new QuantEyeColor() );
    registerQuant( new QuantHairColor() );
    registerQuant( new QuantKeywordManager() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    IMediaPlayerService mps = new MediaPlayerService();
    aAppContext.set( IMediaPlayerService.class, mps );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IHzLibConstants.init( aWinContext );
    //
    IValedControlFactoriesRegistry vr = aWinContext.get( IValedControlFactoriesRegistry.class );
    vr.registerFactory( ValedAvIntHhmmss.FACTORY );
    vr.registerFactory( ValedSecintFactory.FACTORY );
    vr.registerFactory( ValedAvSecintFactory.FACTORY );
    vr.registerFactory( ValedAvValobjFileImage.FACTORY );
    // vr.registerFactory( ValedAvValobjRadioPropEnumStars.FACTORY );
    // vr.registerFactory( ValedRadioPropEnumStars.FACTORY );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SecintM5Model() );
  }

}
