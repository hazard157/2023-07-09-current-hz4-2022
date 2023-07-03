package com.hazard157.prisex24.e4.addons;

import java.util.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.common.e4.services.mwsloc.*;
import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secstep.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.glib.locations.*;
import com.hazard157.prisex24.utils.frasel.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Sets up application common behaviour.
 *
 * @author hazard157
 */
public class AddonPrisex24Behaviour
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonPrisex24Behaviour() {
    super( AddonPrisex24Behaviour.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IEpisode getRandomEpisode( IEclipseContext aWinContext ) {
    IUnitEpisodes unitEpisodes = aWinContext.get( IUnitEpisodes.class );
    if( unitEpisodes.items().isEmpty() ) {
      return null;
    }
    Random random = new Random();
    int rind = random.nextInt();
    if( rind < 0 ) {
      rind = -rind;
    }
    rind %= unitEpisodes.items().size();
    return unitEpisodes.items().get( rind );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // on current episode change displays it's animated frames in the shared view
    ICurrentEpisodeService currEpService = aWinContext.get( ICurrentEpisodeService.class );
    IPsxSelectedSvinsService selSvinsService = aWinContext.get( IPsxSelectedSvinsService.class );
    currEpService.addCurrentEntityChangeListener( aCurrent -> {
      IEpisode currEpisode = currEpService.current();
      Svin svin = null;
      if( currEpisode != null ) {
        svin = new Svin( currEpisode.id() );
      }
      selSvinsService.framesSelectionParams().setParams( EAnimationKind.ANIMATED, Boolean.FALSE, IStringList.EMPTY,
          ESecondsStep.SEC_20, EFramesPerSvin.SELECTED );
      selSvinsService.setSvin( svin );
    } );
    //
    IMwsLocationService locationService = aWinContext.get( IMwsLocationService.class );
    EpisodePropertyLocator episodePropertyLocator = new EpisodePropertyLocator( new TsGuiContext( aWinContext ) );
    locationService.registerLocator( episodePropertyLocator );
    // initially select the random episode
    ICurrentEpisodeService currentEpisodeService = aWinContext.get( ICurrentEpisodeService.class );
    Display display = aWinContext.get( Display.class );
    display.asyncExec( () -> currentEpisodeService.setCurrent( getRandomEpisode( aWinContext ) ) );
  }

}
