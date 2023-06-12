package com.hazard157.prisex24.e4.addons;

import static com.hazard157.prisex24.Activator.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.utils.progargs.*;

/**
 * Application addon.
 *
 * @author hazard157
 */
public class Addon05Prisex24WindowsSize
    extends MwsAbstractAddon {

  /**
   * If this command line argument is specified, the main window will have small size for debugging convenience.
   */
  public static final String CMDLINEARG_MAIN_WINDOW_DEBUG_SIZE = "MainWindowDebugSize"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public Addon05Prisex24WindowsSize() {
    super( PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    // initial size of window
    ProgramArgs programArgs = aAppContext.get( ProgramArgs.class );
    if( programArgs.hasArg( CMDLINEARG_MAIN_WINDOW_DEBUG_SIZE ) ) { // initial size of the window SMALL
      Display display = aAppContext.get( Display.class );
      Rectangle dBounds = display.getBounds();
      int dx = dBounds.width / 8;
      int dy = dBounds.height / 8;
      mainWindow.setX( 4 * dx );
      mainWindow.setY( 2 * dy );
      mainWindow.setWidth( 3 * dx );
      mainWindow.setHeight( 5 * dy );
    }
    else { // initial size of the window BIG
      Display display = aAppContext.get( Display.class );
      Rectangle dBounds = display.getBounds();
      mainWindow.setX( dBounds.x + 8 );
      mainWindow.setY( 0 );
      mainWindow.setWidth( dBounds.width - 4 * 8 );
      mainWindow.setHeight( dBounds.height );
    }
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
