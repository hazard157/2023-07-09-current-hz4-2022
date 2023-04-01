package com.hazard157.psx24.timeline;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Константы GUI приложения.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IPsx24TimelineConstants {

  // ------------------------------------------------------------------------------------
  // e4

  String PERSPID_PRISEX_TIMELINE = "com.hazard157.psx24.persp.timeline";
  String PARTID_PRISEX_TIMELINE  = "com.hazard157.psx24.part.timeline";

  // ------------------------------------------------------------------------------------
  // Значки
  String PREFIX_OF_ICON_FIELD_NAME = "ICON_PSX_";
  String ICON_TIMELINE             = "timeline";

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24TimelineConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
