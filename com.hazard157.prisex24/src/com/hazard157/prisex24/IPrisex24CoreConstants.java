package com.hazard157.prisex24;

import static com.hazard157.prisex24.IPsxResources.*;

import java.time.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Application common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPrisex24CoreConstants {

  // ------------------------------------------------------------------------------------
  // Application info

  String    APP_ID      = "com.hazard157.prisex24";                    //$NON-NLS-1$
  String    APP_ALIAS   = "prisex24";                                  //$NON-NLS-1$
  TsVersion APP_VERSION = new TsVersion( 24, 2, 2023, Month.JULY, 12 );

  ITsApplicationInfo APP_INFO = new TsApplicationInfo( APP_ID, STR_APP_INFO, STR_APP_INFO_D, APP_ALIAS, APP_VERSION );

  // ------------------------------------------------------------------------------------
  // PRISEX24

  String PSX_ID      = "psx24";                  //$NON-NLS-1$
  String PSX_FULL_ID = "com.hazard157.prisex24"; //$NON-NLS-1$
  String PSX_M5_ID   = PSX_ID + ".m5";           //$NON-NLS-1$
  String PSX_ACT_ID  = PSX_ID + ".act";          //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$
  String ICONID_PORNICON           = "pornicon"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPrisex24CoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
