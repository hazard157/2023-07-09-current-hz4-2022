package com.hazard157.prisex24;

import static com.hazard157.prisex24.IPsxResources.*;
import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
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
  // E4

  String PERSPID_EPISODES    = "com.hazard157.prisex24.persp.episodes";     //$NON-NLS-1$
  String PARTID_EISODES_LIST = "com.hazard157.prisex24.part.episodes_list"; //$NON-NLS-1$
  String PARTID_EP_PROPS     = "com.hazard157.prisex24.part.ep_props";      //$NON-NLS-1$
  String PARTID_EP_STORY     = "com.hazard157.prisex24.part.ep_story";      //$NON-NLS-1$
  String PARTID_EP_PLANES    = "com.hazard157.prisex24.part.ep_planes";     //$NON-NLS-1$
  String PARTID_EP_NOTES     = "com.hazard157.prisex24.part.ep_notes";      //$NON-NLS-1$
  String PARTID_EP_          = "com.hazard157.prisex24.part.ep_";           //$NON-NLS-1$

  String PERSPID_REFBOOKS     = "com.hazard157.prisex24.persp.refbooks";     //$NON-NLS-1$
  String PARTSTACKID_REFBOOKS = "com.hazard157.prisex24.partstack.refbooks"; //$NON-NLS-1$

  String CMDID_GOTO_EPISODE_PREV   = "com.hazard157.prisex24.cmd.goto_episode_prev";   //$NON-NLS-1$
  String CMDID_GOTO_EPISODE_NEXT   = "com.hazard157.prisex24.cmd.goto_episode_next";   //$NON-NLS-1$
  String CMDID_GOTO_EPISODE_SELECT = "com.hazard157.prisex24.cmd.goto_episode_select"; //$NON-NLS-1$
  String CMDID_EP_KDENLIVES        = "com.hazard157.prisex24.cmd.episode_kdenlive";    //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME          = "ICONID_";                     //$NON-NLS-1$
  String ICONID_APP_ICON                    = "app-icon";                    //$NON-NLS-1$
  String ICONID_PORNICON                    = "pornicon";                    //$NON-NLS-1$
  String ICONID_TODO_ITEM                   = "todo-item";                   //$NON-NLS-1$
  String ICONID_TODO_ITEM_DIMMED            = "todo-item-dimmed";            //$NON-NLS-1$
  String ICONID_TODOS_LIST                  = "todos-list";                  //$NON-NLS-1$
  String ICONID_TAG                         = "tag";                         //$NON-NLS-1$
  String ICONID_TAGS_LIST                   = "tags-list";                   //$NON-NLS-1$
  String ICONID_FRAMES_PER_SVIN_FORCE_ONE   = "frames-per-svin-force-one";   //$NON-NLS-1$
  String ICONID_FRAMES_PER_SVIN_ONE_NO_MORE = "frames-per-svin-one-no-more"; //$NON-NLS-1$
  String ICONID_FRAMES_PER_SVIN_SELECTED    = "frames-per-svin-selected";    //$NON-NLS-1$
  String ICONID_FRAMES_PER_SVIN_QUESTION    = "frames-per-svin-question";    //$NON-NLS-1$
  String ICONID_GIF_CREATE                  = "gif-create";                  //$NON-NLS-1$
  String ICONID_GIF_INFO                    = "gif-info";                    //$NON-NLS-1$
  String ICONID_GIF_RECREATE_ALL            = "gif-recreate-all";            //$NON-NLS-1$
  String ICONID_GIF_REMOVE                  = "gif-remove";                  //$NON-NLS-1$
  String ICONID_GIF_TEST                    = "gif-test";                    //$NON-NLS-1$
  String ICONID_WORK_WITH_FRAMES            = "work-with-frames";            //$NON-NLS-1$
  String ICONID_PSX_PLANE                   = "media-optical-dvd-video";     //$NON-NLS-1$
  String ICONID_PSX_PLANE_LINE              = "tools-rip-video-dvd";         //$NON-NLS-1$
  String ICONID_KNOTES                      = "knotes";                      //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_GIF_CREATE       = PSX_ACT_ID + ".gif_create";       //$NON-NLS-1$
  String ACTID_GIF_INFO         = PSX_ACT_ID + ".gif_info";         //$NON-NLS-1$
  String ACTID_GIF_RECREATE_ALL = PSX_ACT_ID + ".gif_recreate_all"; //$NON-NLS-1$
  String ACTID_GIF_REMOVE       = PSX_ACT_ID + ".gif_remove";       //$NON-NLS-1$
  String ACTID_GIF_TEST         = PSX_ACT_ID + ".gif_test";         //$NON-NLS-1$
  String ACTID_WORK_WITH_FRAMES = PSX_ACT_ID + ".work_with_frames"; //$NON-NLS-1$
  String ACTID_CAM_ID_MENU      = PSX_ACT_ID + ".CamIdMenu";        //$NON-NLS-1$
  String ACTID_COPY_FRAME       = PSX_ACT_ID + ".CopyFrame";        //$NON-NLS-1$

  ITsActionDef ACDEF_GIF_CREATE = TsActionDef.ofPush2( ACTID_GIF_CREATE, //
      STR_GIF_CREATE, STR_GIF_CREATE_D, ICONID_GIF_CREATE );

  ITsActionDef ACDEF_GIF_TEST_MENU = TsActionDef.ofMenu2( ACTID_GIF_TEST, //
      STR_GIF_TEST, STR_GIF_TEST_D, ICONID_GIF_TEST );

  ITsActionDef ACDEF_GIF_INFO = TsActionDef.ofPush2( ACTID_GIF_INFO, //
      STR_GIF_INFO, STR_GIF_INFO_D, ICONID_GIF_INFO );

  ITsActionDef ACDEF_GIF_RECREATE_ALL = TsActionDef.ofPush2( ACTID_GIF_RECREATE_ALL, //
      STR_GIF_RECREATE_ALL, STR_GIF_RECREATE_ALL_D, ICONID_GIF_RECREATE_ALL );

  ITsActionDef ACDEF_GIF_REMOVE = TsActionDef.ofPush2( ACTID_GIF_REMOVE, //
      STR_GIF_REMOVE, STR_GIF_REMOVE_D, ICONID_GIF_REMOVE );

  ITsActionDef ACDEF_GIF_TEST = TsActionDef.ofPush2( ACTID_GIF_TEST, //
      STR_GIF_TEST, STR_GIF_TEST_D, ICONID_GIF_TEST );

  ITsActionDef ACDEF_WORK_WITH_FRAMES = TsActionDef.ofPush2( ACTID_WORK_WITH_FRAMES, //
      STR_WORK_WITH_FRAMES, STR_WORK_WITH_FRAMES_D, ICONID_WORK_WITH_FRAMES );

  ITsActionDef ACDEF_CAM_ID_MENU = TsActionDef.ofMenu2( ACTID_CAM_ID_MENU, //
      ACT_CAM_ID_MENU, ACT_CAM_ID_MENU_D, ICONID_CAMERA_GENERIC );

  ITsActionDef ACDEF_COPY_FRAME = TsActionDef.ofPush2( ACTID_COPY_FRAME, //
      ACT_COPY_FRAME, ACT_COPY_FRAME_D, ICONID_ARROW_RIGHT );

  // ------------------------------------------------------------------------------------
  // Application preferences

  /**
   * Preferences bundle specific for HZ library.
   */
  String PBID_PSX24_COMMON = PSX_FULL_ID + ".Common"; //$NON-NLS-1$

  String APREFID_THUMB_SIZE_IN_MENUS = "ThumbSizeInMenus"; //$NON-NLS-1$

  IDataDef APPREF_THUMB_SIZE_IN_MENUS = DataDef.create( APREFID_THUMB_SIZE_IN_MENUS, VALOBJ, //
      TSID_NAME, STR_THUMB_SIZE_IN_MENUS, //
      TSID_DESCRIPTION, STR_THUMB_SIZE_IN_MENUS_D, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ180 ) //
  );

  IStridablesList<IDataDef> ALL_APPREFS = new StridablesList<>( //
      APPREF_THUMB_SIZE_IN_MENUS //
  );

  // ------------------------------------------------------------------------------------
  // Misc settings

  /**
   * Minimal frame thumb size, restricted due to prepared image files size.
   */
  EThumbSize PSX_MIN_FRAME_THUMB_SIZE = EThumbSize.SZ64;

  /**
   * Maximal frame thumb size, restricted due to prepared image files size.
   */
  EThumbSize PSX_MAX_FRAME_THUMB_SIZE = EThumbSize.SZ512;

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPrisex24CoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PBID_PSX24_COMMON, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_PB_PSX24_COMMON, //
        TSID_DESCRIPTION, STR_PB_PSX24_COMMON_D, //
        TSID_ICON_ID, ICONID_APP_ICON //
    ) );
    for( IDataDef dd : ALL_APPREFS ) {
      pb.defineOption( dd );
    }
  }

}
