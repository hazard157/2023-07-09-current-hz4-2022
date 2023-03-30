package com.hazard157.psx24.gazes.e4.addons;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx24.gazes.IPsx24GazesConstants.*;
import static com.hazard157.psx24.gazes.e4.addons.IPsxResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.psx24.gazes.*;
import com.hazard157.psx24.gazes.e4.services.*;
import com.hazard157.psx24.gazes.m5.*;
import com.hazard157.psx24.gazes.utils.*;

/**
 * Module addon.
 *
 * @author goga
 */
public class AddonPsx24Gazes
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonPsx24Gazes() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    IAppPreferences aprefs = aAppContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PREFS_BUNDLE_ID_GAZES, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_PREFS_BUNDLE, //
        TSID_DESCRIPTION, STR_D_PREFS_BUNDLE, //
        TSID_ICON_ID, ICONID_GAZE //
    ) );
    for( IDataDef d : ALL_APPRMS ) {
      pb.defineOption( d );
    }
    aAppContext.set( PREFS_BUNDLE_ID_GAZES, pb );
    //
    ICurrentGazeService cgs = new CurrentGazeService();
    aAppContext.set( ICurrentGazeService.class, cgs );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsx24GazesConstants.init( aWinContext );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new GazeM5Model() );
  }

  @Override
  protected void doBeforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
    GazeMediaFileVisualsProvider.disposeImagesOnProgramExit();
  }

}
