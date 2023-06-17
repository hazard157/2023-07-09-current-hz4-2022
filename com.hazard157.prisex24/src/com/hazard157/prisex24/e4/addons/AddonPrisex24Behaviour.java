package com.hazard157.prisex24.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.glib.frasel.*;
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
          EFramesPerSvin.SELECTED );
      selSvinsService.setSvin( svin );
    } );
  }

}
