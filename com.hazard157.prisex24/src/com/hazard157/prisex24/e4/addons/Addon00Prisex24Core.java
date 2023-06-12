package com.hazard157.prisex24.e4.addons;

import static com.hazard157.prisex24.Activator.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.rcp.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.prisex24.*;
import com.hazard157.prisex24.Activator;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class Addon00Prisex24Core
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public Addon00Prisex24Core() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantTsGuiRcp() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // window and application icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( PLUGIN_ID, ICONID_APP_ICON, IS_48X48 ) );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPrisex24CoreConstants.init( aWinContext );
    // DEBUG --- resource tracking
    Resource.setNonDisposeHandler( aT -> {
      TsTestUtils.pl( "ResourseErr: %s", aT.toString() ); //$NON-NLS-1$
      TsTestUtils.p( "" ); //$NON-NLS-1$
    } );
    // ---

  }

}
