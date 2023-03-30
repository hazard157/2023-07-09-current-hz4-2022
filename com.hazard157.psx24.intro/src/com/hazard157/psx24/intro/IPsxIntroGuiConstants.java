package com.hazard157.psx24.intro;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.intro.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.psx24.intro.utils.*;

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

  /**
   * Параметр: отображать дату эпизода как "YYYY-MM-DD", а не как "DD mmm YYYY" на экране приветствия миниатюрами.
   * <p>
   * Хранится как {@link EAtomicType#BOOLEAN}.
   */
  IDataDef APPRM_IS_LABEL_AS_YMD = DataDef.create( PSX_ID + "intro.IsLabelAsYMD", //$NON-NLS-1$
      BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_N_INTRO_LABEL_AS_YMD, //
      TSID_DESCRIPTION, STR_D_INTRO_LABEL_AS_YMD //
  );

  /**
   * Настройка программы - показывать каждрый раз {@link StartupGifDisplay#show()}.
   */
  IDataDef APPRM_IS_STARTUP_GIF_SHOWN = DataDef.create( PSX_ID + "intro.IsStartupGifShown", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_N_IS_STARTUP_GIF_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_STARTUP_GIF_SHOWN //
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
    pb.defineOption( APPRM_THUMB_SIZE );
    pb.defineOption( APPRM_IS_LABEL_AS_YMD );
    pb.defineOption( APPRM_IS_STARTUP_GIF_SHOWN );
  }

}
