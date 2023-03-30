package com.hazard157.psx24.gazes;

import static com.hazard157.psx24.gazes.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Константы GUI плагина.
 *
 * @author goga
 */
@SuppressWarnings( { "javadoc" } )
public interface IPsx24GazesConstants {

  /**
   * TODO Prespective - Gaze:<br>
   * - for media files add rating and quality properties<br>
   * - list all media files of all gazes<br>
   * - select to show image/anim/video/sound/misc<br>
   * - view ONE gaze media (100% sized image, embedded video player, embedded audio player, text viewer, etc.)<br>
   * - change rating of IGaze and media files directly while vieweing, without invoking dialog<br>
   */

  // ------------------------------------------------------------------------------------
  // Элементы GUI
  //

  String PERSPID_PSX14_GAZES = "com.hazard157.psx24.persp.gazes"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Значки
  //

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  // String ICONID_XXX= "xxx"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Plugin preferences
  //

  String PREFS_BUNDLE_ID_GAZES = PERSPID_PSX14_GAZES;

  IDataDef APREF_MEDIA_THUMB_SIZE = DataDef.create( "MediaFileThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_MEDIA_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_MEDIA_THUMB_SIZE, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ256 ) //
  );

  IDataDef APREF_XXX = DataDef.create( "Xxx", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_XXX, //
      TSID_DESCRIPTION, STR_D_XXX, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 ) //
  );

  IStridablesList<IDataDef> ALL_APPRMS = new StridablesList<>( //
      APREF_MEDIA_THUMB_SIZE //
  );

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24GazesConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
