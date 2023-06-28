package com.hazard157.psx24.films;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.films.IPsxResources.*;
import static org.toxsoft.core.tsgui.rcp.valed.IValedFileConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.rcp.valed.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Константы GUI плагина.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IPsx24FilmsConstants {

  // ------------------------------------------------------------------------------------
  // Элементы GUI
  //

  String PERSPID_FILMS = "com.hazard157.psx24.persp.films";

  // ------------------------------------------------------------------------------------
  // Настройки приложения
  //

  String PREF_BUNDLE_ID = PERSPID_FILMS;

  // IDataDef OP_SHOW_FILM_FULLSCREEN = DataDef.create( "ShowFilmFullscreen", BOOLEAN, //$NON-NLS-1$
  // TSID_NAME, STR_N_SHOW_FILM_FULLSCREEN, //
  // TSID_DESCRIPTION, STR_D_SHOW_FILM_FULLSCREEN, //
  // TSID_DEFAULT_VALUE, AV_TRUE //
  // );

  IDataDef OP_FILMS_DIR_ROOT = DataDef.create( "FilmsDirRoot", STRING, //$NON-NLS-1$
      OPDEF_IS_WIDTH_FIXED, AV_FALSE, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvStringFile.FACTORY_NAME, //
      OPDEF_IS_OPEN_DIALOG, AV_FALSE, //
      OPDEF_MUST_EXIST, AV_FALSE, //
      OPDEF_IS_DIRECTORY, AV_TRUE, //
      TSID_NAME, STR_N_FILMS_DIR_ROOT, //
      TSID_DESCRIPTION, STR_D_FILMS_DIR_ROOT, //
      TSID_DEFAULT_VALUE, avStr( "/home/hmade/media/films/" ) //
  );

  /**
   * Размер миниатюр эпизодов на экране приветствия.
   */
  IDataDef OP_THUMB_SIZE = DataDef.create( "FilmThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ512 ), //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_NAME, STR_N_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_THUMB_SIZE //
  );

  /**
   * Показывать ли старые фильмы из подкаталога legacy.
   */
  IDataDef OP_SHOW_LEGACY_FILMS = DataDef.create( "IsLegacyFilmsShown", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_FALSE, //
      TSID_NAME, STR_N_SHOW_LEGACY_FILMS, //
      TSID_DESCRIPTION, STR_D_SHOW_LEGACY_FILMS //
  );

  /**
   * Все опции.
   */
  IStridablesList<IDataDef> ALL_OPS = new StridablesList<>( //
      OP_FILMS_DIR_ROOT, //
      // OP_SHOW_FILM_FULLSCREEN, //
      OP_THUMB_SIZE, //
      OP_SHOW_LEGACY_FILMS //
  );

  // ------------------------------------------------------------------------------------
  // Значки
  //

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";
  String ICONID_FILM               = "film";
  String ICONID_OUT_OF_DATE        = "out-of-date";

  // ------------------------------------------------------------------------------------
  // Действия ITsActionInfo
  //

  String AID_SHOW_LEGACY_FILMS = PSX_ACT_ID + ".ShowLegacyFilms";

  ITsActionDef AI_SHOW_LEGACY_FILMS = TsActionDef.ofCheck2( AID_SHOW_LEGACY_FILMS, //
      ACT_T_SHOW_LEGACY_FILMS, ACT_P_SHOW_LEGACY_FILMS, ICONID_OUT_OF_DATE );

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PREF_BUNDLE_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_PB_FILMS, //
        TSID_DESCRIPTION, STR_D_PB_FILMS, //
        TSID_ICON_ID, ICONID_FILM //
    ) );
    for( IDataDef opdef : ALL_OPS ) {
      pb.defineOption( opdef );
    }
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24FilmsConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
