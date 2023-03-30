package com.hazard157.psx24.planning;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Константы GUI плагина.
 *
 * @author goga
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IPsx42PlaningConstants {

  // ------------------------------------------------------------------------------------
  // Элементы GUI
  //

  String PERSPID_PLANNING = "com.hazard157.psx24.persp.planning";

  // ------------------------------------------------------------------------------------
  // Значки
  //

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";
  String ICONID_PLANNING           = "planning";
  String ICONID_PLAN               = "plan";
  String ICONID_STIR               = "stir";

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx42PlaningConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
