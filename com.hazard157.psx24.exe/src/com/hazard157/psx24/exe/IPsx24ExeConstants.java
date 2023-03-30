package com.hazard157.psx24.exe;

import static com.hazard157.psx24.exe.IPsxResources.*;

import java.time.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Application constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsx24ExeConstants {

  // ------------------------------------------------------------------------------------
  // App info

  String APP_ID    = "com.hazard157.psx24"; //$NON-NLS-1$
  String APP_ALIAS = "psx24";               //$NON-NLS-1$

  TsVersion APP_VERSION = new TsVersion( 24, 0, 2022, Month.JUNE, 12 );

  ITsApplicationInfo APP_INFO = new TsApplicationInfo( APP_ID, STR_N_APP_INFO, STR_D_APP_INFO, APP_ALIAS, APP_VERSION );

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";                              //$NON-NLS-1$
  String ICONID_APP_ICON           = ITsStdIconIds.ICONID_TSAPP_WINDOWS_ICON;

  // ------------------------------------------------------------------------------------
  // Initialization code
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24ExeConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
