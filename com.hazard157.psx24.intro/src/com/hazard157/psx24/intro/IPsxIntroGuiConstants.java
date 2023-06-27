package com.hazard157.psx24.intro;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.intro.IPsxIntroSharedResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxIntroGuiConstants {

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_PRISEX_INTRO               = "com.hazard157.psx24.persp.intro";                //$NON-NLS-1$
  String PARTID_PRISEX_INTRO_EPISODE_THUMBS = "com.hazard157.psx24.persp.intro_episode_thumbs"; //$NON-NLS-1$
  String CMDID_TOGGLE_FORCE_STILL           = "com.hazard157.psx31.cmd.toggle_force_still";     //$NON-NLS-1$
  String TOOLBTNID_TOGGLE_FORCE_STILL       = "com.hazard157.psx31.toolbtn.toggle_force_still"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_PSX_"; //$NON-NLS-1$
  String ICON_PSX_INTRO            = "intro";     //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // App Preferences

  /**
   * Идентифиатор {@link IPrefBundle} с настройками модуля.
   */
  String PSX_INTRO_APREF_BUNDLE_ID = PERSPID_PRISEX_INTRO;

  /**
   * Размер миниатюр эпизодов на экране приветствия.
   */
  IDataDef APPRM_THUMB_SIZE = DataDef.create( PSX_ID + ".intro.ThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ180 ), //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_NAME, STR_N_INTRO_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_INTRO_THUMB_SIZE //
  );

  IDataDef APPRM_IS_LABEL_AS_YMD = DataDef.create( PSX_ID + "intro.IsLabelAsYMD", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_N_INTRO_LABEL_AS_YMD, //
      TSID_DESCRIPTION, STR_D_INTRO_LABEL_AS_YMD //
  );

  IDataDef APPRM_IS_STARTUP_GIF_SHOWN = DataDef.create( PSX_ID + "intro.IsStartupGifShown", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_N_IS_STARTUP_GIF_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_STARTUP_GIF_SHOWN //
  );

  IDataDef APPRM_IS_FORCE_STILL_FRAME = DataDef.create( PSX_ID + "intro.ForceStillFrame", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_IS_FORCE_STILL_FRAME, //
      TSID_DESCRIPTION, STR_IS_FORCE_STILL_FRAME_D //
  );

  IStridablesList<IDataDef> ALL_APREFS = new StridablesList<>( //
      APPRM_THUMB_SIZE, //
      APPRM_IS_LABEL_AS_YMD, //
      APPRM_IS_STARTUP_GIF_SHOWN, //
      APPRM_IS_FORCE_STILL_FRAME //
  );

  // ------------------------------------------------------------------------------------
  // Действия

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsxIntroGuiConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PSX_INTRO_APREF_BUNDLE_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_PB_INTRO, //
        TSID_DESCRIPTION, STR_D_PB_INTRO, //
        TSID_ICON_ID, ICON_PSX_INTRO //
    ) );
    for( IDataDef dd : ALL_APREFS ) {
      pb.defineOption( dd );
    }
  }

}
