package com.hazard157.psx24.core;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsxResources.*;
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
 * Константы GUI приложения.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsx24CoreConstants {

  // ------------------------------------------------------------------------------------
  // E4
  //

  String PERSPID_EPISODES = "com.hazard157.psx24.persp.episodes"; //$NON-NLS-1$
  String PERSPID_REFBOOKS = "com.hazard157.psx24.persp.refbooks"; //$NON-NLS-1$

  String SHAREDPARTID_CURR_FRAMES_LIST = "com.hazard157.psx24.sharedpart.curr_frames_list"; //$NON-NLS-1$
  String SHAREDPARTID_EPISODES_LIST    = "com.hazard157.psx24.sharedpart.episodes_list";    //$NON-NLS-1$
  String SHAREDPARTID_SVINS_FRAMES     = "com.hazard157.psx24.sharedpart.svins_frames";     //$NON-NLS-1$

  String PARTSTACKID_EP_ELEMS = "com.hazard157.psx24.core.partstack.ep_elems"; //$NON-NLS-1$

  String PARTID_EP_PROPS    = "com.hazard157.psx24.parts.ep_props";    //$NON-NLS-1$
  String PARTID_EP_STORY    = "com.hazard157.psx24.parts.ep_story";    //$NON-NLS-1$
  String PARTID_EP_TAGS     = "com.hazard157.psx24.parts.ep_tags";     //$NON-NLS-1$
  String PARTID_EP_PLANES   = "com.hazard157.psx24.parts.ep_planes";   //$NON-NLS-1$
  String PARTID_EP_NOTES    = "com.hazard157.psx24.parts.ep_notes";    //$NON-NLS-1$
  String PARTID_EP_TRAILERS = "com.hazard157.psx24.parts.ep_trailers"; //$NON-NLS-1$
  String PARTID_EP_SRC_VIDS = "com.hazard157.psx24.parts.ep_src_vids"; //$NON-NLS-1$

  String PARTID_RB_TODOS    = "com.hazard157.psx24.parts.rb_todos";    //$NON-NLS-1$
  String PARTID_RB_CAMERAS  = "com.hazard157.psx24.parts.rb_cameras";  //$NON-NLS-1$
  String PARTID_RB_TAGS     = "com.hazard157.psx24.parts.rb_tags";     //$NON-NLS-1$
  String PARTID_RB_SONGS    = "com.hazard157.psx24.parts.rb_songs";    //$NON-NLS-1$
  String PARTID_RB_TRAILERS = "com.hazard157.psx24.parts.rb_trailers"; //$NON-NLS-1$
  String PARTID_RB_SRC_VIDS = "com.hazard157.psx24.parts.rb_src_vids"; //$NON-NLS-1$

  String CMDID_GOTO_EPISODE_PREV   = "com.hazard157.psx24.cmd.goto_episode_prev";   //$NON-NLS-1$
  String CMDID_GOTO_EPISODE_NEXT   = "com.hazard157.psx24.cmd.goto_episode_next";   //$NON-NLS-1$
  String CMDID_GOTO_EPISODE_SELECT = "com.hazard157.psx24.cmd.goto_episode_select"; //$NON-NLS-1$
  String CMDID_EP_KDENLIVES        = "com.hazard157.psx24.cmd.episode_kdenlives";   //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // icons

  String PREFIX_OF_ICON_FIELD_NAME    = "ICON_";                                //$NON-NLS-1$
  String ICON_APP_ICON                = "app-icon";                             //$NON-NLS-1$
  String ICON_PORNICON                = "pornicon";                             //$NON-NLS-1$
  String ICON_PSX_EPISODES            = "ui-episodes";                          //$NON-NLS-1$
  String ICON_PSX_REFBOOKS            = "ui-refbooks";                          //$NON-NLS-1$
  String ICON_PSX_CREATE_GIF          = "create-gif";                           //$NON-NLS-1$
  String ICON_PSX_TEST_GIF            = "test-gif";                             //$NON-NLS-1$
  String ICON_PSX_RECREATE_ALL_GIFS   = "recreate-all-gifs";                    //$NON-NLS-1$
  String ICON_PSX_WORK_WITH_FRAMES    = "work-with-frames";                     //$NON-NLS-1$
  String ICON_PSX_IMAGE_X_GENERIC     = "image-x-generic";                      //$NON-NLS-1$
  String ICON_PSX_VIDEO_X_GENERIC     = "video-x-generic";                      //$NON-NLS-1$
  String ICON_AUDIO_X_GENERIC         = "audio-x-generic";                      //$NON-NLS-1$
  String ICON_TEXT_X_GENERIC          = "text-x-generic";                       //$NON-NLS-1$
  String ICON_UNKNOWN_X_GENERIC       = "unknown-x-generic";                    //$NON-NLS-1$
  String ICON_PSX_PLAYBACK_START      = "media-playback-start";                 //$NON-NLS-1$
  String ICON_PSX_ONE_ICON_PER_EPIN   = "applications-education-miscellaneous"; //$NON-NLS-1$
  String ICON_PSX_NONE_EPISODE_IMAGE  = "none-episode-image";                   //$NON-NLS-1$
  String ICON_TAG                     = "tag";                                  //$NON-NLS-1$
  String ICON_TAGS_LIST               = "tags-list";                            //$NON-NLS-1$
  String ICON_PSX_INCL_ONLY_USED_TAGS = "bookmark-new-list";                    //$NON-NLS-1$
  String ICON_PSX_TODO_ITEM           = "todo-item";                            //$NON-NLS-1$
  String ICON_PSX_TODO_ITEM_DIMMED    = "todo-item-dimmed";                     //$NON-NLS-1$
  String ICON_PSX_TODOS_LIST          = "todos-list";                           //$NON-NLS-1$
  String ICON_PSX_STORY_EDITOR        = "story-editor";                         //$NON-NLS-1$
  String ICON_TIMELINE_ZOOM_FIT       = "timeline-zoom-fit";                    //$NON-NLS-1$
  String ICON_TIMELINE_ZOOM_IN        = "timeline-zoom-in";                     //$NON-NLS-1$
  String ICON_TIMELINE_ZOOM_OUT       = "timeline-zoom-out";                    //$NON-NLS-1$
  String ICON_TIMELINE_ZOOM_ORIGINAL  = "timeline-zoom-original";               //$NON-NLS-1$
  String ICON_PSX_KDENLIVE            = "kdenlive";                             //$NON-NLS-1$
  String ICON_PSX_NOTE                = "knotes";                               //$NON-NLS-1$
  String ICON_PSX_PLANE               = "media-optical-dvd-video";              //$NON-NLS-1$
  String ICON_PSX_PLANE_LINE          = "tools-rip-video-dvd";                  //$NON-NLS-1$
  String ICON_PSX_FOLDER_IMAGE        = "folder-image";                         //$NON-NLS-1$
  String ICON_PSX_FOLDER_VIDEO        = "folder-video";                         //$NON-NLS-1$
  String ICON_ONE_BY_ONE              = "one-by-one";                           //$NON-NLS-1$

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

  // ------------------------------------------------------------------------------------
  // App Preferences

  /**
   * Идентифиатор {@link IPrefBundle} с общими настройками программы.
   */
  String PSX_COMMON_APREF_BUNDLE_ID = Activator.PLUGIN_ID;

  String APPRMID_THUMBSZ_FRAMES_IN_PANELS = PSX_ID + ".ThumbSize.FramesInPanels";

  /**
   * Параметр: Размер миниатюр, выводимых при просмотре панелях различных списков кадров.
   */
  IDataDef APPRM_THUMBSZ_FRAMES_IN_PANELS = DataDef.create( APPRMID_THUMBSZ_FRAMES_IN_PANELS, VALOBJ, //
      TSID_NAME, STR_N_APPRM_THUMBSZ_FRAMES_IN_PANELS, //
      TSID_DESCRIPTION, STR_D_APPRM_THUMBSZ_FRAMES_IN_PANELS, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ96 ) //
  );

  /**
   * Параметр {@link IPrefBundle}: размер выводимых в меню вопроизведения значков.
   */
  IDataDef APPRM_THUMBSZ_FRAMES_IN_MENUS = DataDef.create( PSX_ID + ".ThumbSize.FramesInMenus", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_APPRM_THUMBSZ_FRAMES_IN_MENUS, //
      TSID_DESCRIPTION, STR_D_APPRM_THUMBSZ_FRAMES_IN_MENUS, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ180 ) //
  );

  /**
   * Параметр {@link IPrefBundle}: размер выводимых в меню вопроизведения значков.
   */
  IDataDef APPRM_THUMBSZ_FRAMES_IN_TREES = DataDef.create( PSX_ID + ".ThumbSize.FramesInTrees", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_APPRM_THUMBSZ_FRAMES_IN_TREES, //
      TSID_DESCRIPTION, STR_D_APPRM_THUMBSZ_FRAMES_IN_TREES, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ48 ) //
  );

  IStridablesList<IDataDef> ALL_APPREFS = new StridablesList<>( //
      APPRM_THUMBSZ_FRAMES_IN_MENUS, //
      APPRM_THUMBSZ_FRAMES_IN_PANELS, //
      APPRM_THUMBSZ_FRAMES_IN_TREES //
  );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsx24CoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
    IAppPreferences apref = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = apref.defineBundle( PSX_COMMON_APREF_BUNDLE_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_PSX_COMMON_APREF_BUNDLE, //
        TSID_DEFAULT_VALUE, STR_D_PSX_COMMON_APREF_BUNDLE, //
        TSID_ICON_ID, ICON_APP_ICON //
    ) );
    for( IDataDef p : ALL_APPREFS ) {
      pb.defineOption( p );
    }
  }

}
