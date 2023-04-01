package com.hazard157.psx24.explorer;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Константы GUI плагина.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IPsx24ExplorerConstants {

  // ------------------------------------------------------------------------------------
  // E4
  //

  String PERSPID_EXPLORER = "com.hazard157.psx24.persp.explorer";

  // ------------------------------------------------------------------------------------
  // Значки

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";
  String ICONID_EXPLORER           = "briefcase2_view";
  String ICONID_INQUIRY            = "data_view";

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24ExplorerConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
