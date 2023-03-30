package com.hazard157.psx24.catnote;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Константы GUI приложения.
 *
 * @author goga
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IPsx24CatnoteConstants {

  // ------------------------------------------------------------------------------------
  // e4

  String PERSPID_PRISEX_NOTEBOOK           = "com.hazard157.psx24.persp.notebook";
  String PARTID_PRISEX_NOTEBOOK_NOTES      = "com.hazard157.psx24.part.notebook_notes";
  String PARTID_PRISEX_NOTEBOOK_CATEGORIES = "com.hazard157.psx24.part.notebook_categories";

  // ------------------------------------------------------------------------------------
  // Значки

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_NB_";
  String ICON_NB_NOTE              = "note";
  String ICON_NB_NOTEBOOK          = "notebook";
  String ICON_NB_NOTE_KIND         = "note-kind";
  String ICON_NB_NOTE_CATEGORY     = "note-category";

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24CatnoteConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
