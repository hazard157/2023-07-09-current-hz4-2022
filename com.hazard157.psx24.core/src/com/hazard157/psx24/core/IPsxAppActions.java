package com.hazard157.psx24.core;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxResources.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.toxsoft.core.tsgui.bricks.actions.*;

/**
 * Общие действия приложения.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxAppActions {

  // ------------------------------------------------------------------------------------
  // actions

  String PSX_ACT_ID = PSX_ID + ".Action"; //$NON-NLS-1$

  String AID_COPY_FRAME             = PSX_ACT_ID + ".CopyFrame";           //$NON-NLS-1$
  String AID_TEST_GIF               = PSX_ACT_ID + ".TextGif";             //$NON-NLS-1$
  String AID_CREATE_GIF             = PSX_ACT_ID + ".CreateGif";           //$NON-NLS-1$
  String AID_RECREATE_ALL_GIFS      = PSX_ACT_ID + ".RecreateAllGifs";     //$NON-NLS-1$
  String AID_DELETE_GIF             = PSX_ACT_ID + ".DeleteGif";           //$NON-NLS-1$
  String AID_GIF_INFO               = PSX_ACT_ID + ".GifInfo";             //$NON-NLS-1$
  String AID_CAM_ID_MENU            = PSX_ACT_ID + ".CamIdMenu";           //$NON-NLS-1$
  String AID_SHOW_SINGLE            = PSX_ACT_ID + ".ShowSingle";          //$NON-NLS-1$
  String AID_PLAY                   = PSX_ACT_ID + ".Play";                //$NON-NLS-1$
  String AID_WORK_WITH_FRAMES       = PSX_ACT_ID + ".WorkWithFrames";      //$NON-NLS-1$
  String AID_SHOW_TAGLINE_AS_LIST   = PSX_ACT_ID + ".ShowTaglineAsList";   //$NON-NLS-1$
  String AID_INCLUDE_ONLY_USED_TAGS = PSX_ACT_ID + ".IncludeOnlyUsedTags"; //$NON-NLS-1$
  String AID_RUN_KDENLIVE           = PSX_ACT_ID + ".RunKdenlive";         //$NON-NLS-1$
  String AID_SHOW_AK_BOTH           = PSX_ACT_ID + ".ShowAkBoth";          //$NON-NLS-1$
  String AID_SHOW_AK_ANIMATED       = PSX_ACT_ID + ".ShowAkAnimated";      //$NON-NLS-1$
  String AID_SHOW_AK_SINGLE         = PSX_ACT_ID + ".ShowAkSingle";        //$NON-NLS-1$
  String ACTID_ONE_BY_ONE           = "psx.one_by_one";                    //$NON-NLS-1$

  // String AID_RUN_PROGRAM = PSX_ACT_ID + ".2";
  // String AID_TIMELINE_ZOOM_OUT = PSX_ACT_ID + ".18";
  // String AID_TIMELINE_ZOOM_ORIGINAL = PSX_ACT_ID + ".19";
  // String AID_TIMELINE_ZOOM_IN = PSX_ACT_ID + ".20";

  // ------------------------------------------------------------------------------------
  // Misc

  String PROGRAM_KDENLIVE = "run-kdenlive.sh"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Описания действий ITsAction

  // ITsActionDef AI_FILTER = new TsActCheckBoxInfo( SAID_FILTER, ACT_T_FILTER, ACT_P_FILTER, ICONID_VIEW_FILTER );

  ITsActionDef AI_PLAY = TsActionDef.ofPush2( AID_PLAY, //
      ACT_T_PLAY, ACT_P_PLAY, ICON_PSX_PLAYBACK_START );

  ITsActionDef AI_PLAY_MENU = TsActionDef.ofMenu2( AID_PLAY, //
      ACT_T_PLAY_MENU, ACT_P_PLAY_MENU, ICON_PSX_PLAYBACK_START );

  // ITsActionDef AI_RUN_PROGRAM =
  // TsActionDef.ofMenu2( AID_RUN_PROGRAM, ACT_T_RUN_PROGRAM, ACT_P_RUN_PROGRAM, ICON_PSX_RUN_PROGRAM, null );

  ITsActionDef AI_WORK_WITH_FRAMES = TsActionDef.ofPush2( AID_WORK_WITH_FRAMES, ACT_T_WORK_WITH_FRAMES,
      ACT_P_WORK_WITH_FRAMES, ICON_PSX_WORK_WITH_FRAMES );

  ITsActionDef AI_CAM_ID_MENU = TsActionDef.ofMenu2( AID_CAM_ID_MENU, //
      ACT_T_CAM_ID_MENU, ACT_P_CAM_ID_MENU, ICONID_CAMERA_GENERIC );

  ITsActionDef AI_CREATE_GIF = TsActionDef.ofMenu2( AID_CREATE_GIF, //
      ACT_T_CREATE_GIF, ACT_P_CREATE_GIF, ICON_PSX_CREATE_GIF );

  ITsActionDef AI_TEST_GIF = TsActionDef.ofPush2( AID_TEST_GIF, //
      ACT_T_TEST_GIF, ACT_P_TEST_GIF, ICON_PSX_TEST_GIF );

  ITsActionDef AI_RECREATE_ALL_GIFS = TsActionDef.ofPush2( AID_RECREATE_ALL_GIFS, //
      ACT_T_RECREATE_ALL_GIFS, ACT_P_RECREATE_ALL_GIFS, ICON_PSX_RECREATE_ALL_GIFS );

  ITsActionDef AI_DELETE_GIF = TsActionDef.ofPush2( AID_DELETE_GIF, //
      ACT_T_DELETE_GIF, ACT_P_DELETE_GIF, ICONID_LIST_REMOVE );

  ITsActionDef AI_GIF_INFO = TsActionDef.ofPush2( AID_GIF_INFO, //
      ACT_T_GIF_INFO, ACT_P_GIF_INFO, ICONID_DIALOG_INFORMATION );

  ITsActionDef AI_COPY_FRAME = TsActionDef.ofPush2( AID_COPY_FRAME, //
      ACT_T_COPY_FRAME, ACT_P_COPY_FRAME, ICONID_ARROW_RIGHT );

  ITsActionDef AI_SHOW_SINGLE = TsActionDef.ofCheck2( AID_SHOW_SINGLE, //
      STR_T_SHOW_SINGLE, STR_P_SHOW_SINGLE, ICON_PSX_IMAGE_X_GENERIC );

  ITsActionDef AI_SHOW_TAGLINE_AS_LIST = TsActionDef.ofCheck2( AID_SHOW_TAGLINE_AS_LIST, //
      ACT_T_SHOW_TAGLINE_AS_LIST, ACT_P_SHOW_TAGLINE_AS_LIST, ICONID_FORMAT_JUSTIFY_FILL );

  ITsActionDef AI_INCLUDE_ONLY_USED_TAGS = TsActionDef.ofCheck2( AID_INCLUDE_ONLY_USED_TAGS, //
      ACT_T_INCLUDE_ONLY_USED_TAGS, ACT_P_INCLUDE_ONLY_USED_TAGS, ICON_PSX_INCL_ONLY_USED_TAGS );

  ITsActionDef AI_RUN_KDENLIVE = TsActionDef.ofPush2( AID_RUN_KDENLIVE, //
      ACT_T_RUN_KDENLIVE, ACT_P_RUN_KDENLIVE, ICON_PSX_KDENLIVE );

  ITsActionDef AI_SHOW_AK_BOTH = TsActionDef.ofRadio2( AID_SHOW_AK_BOTH, //
      ACT_T_SHOW_AK_BOTH, ACT_P_SHOW_AK_BOTH, ICONID_AK_BOTH );

  ITsActionDef AI_SHOW_AK_ANIMATED = TsActionDef.ofRadio2( AID_SHOW_AK_ANIMATED, //
      ACT_T_SHOW_AK_ANIMATED, ACT_P_SHOW_AK_ANIMATED, ICONID_AK_ANIMATED );

  ITsActionDef AI_SHOW_AK_SINGLE =
      TsActionDef.ofRadio2( AID_SHOW_AK_SINGLE, ACT_T_SHOW_AK_SINGLE, ACT_P_SHOW_AK_SINGLE, ICONID_AK_SINGLE );

  // ITsActionDef AI_TIMELINE_ZOOM_OUT = TsActionDef.ofPush2( AID_TIMELINE_ZOOM_OUT, //
  // ACT_T_TIMELINE_ZOOM_OUT, ACT_P_TIMELINE_ZOOM_OUT, ICONID_TIMELINE_ZOOM_OUT );
  //
  // ITsActionDef AI_TIMELINE_ZOOM_ORIGINAL = TsActionDef.ofPush2( AID_TIMELINE_ZOOM_ORIGINAL, //
  // ACT_T_TIMELINE_ZOOM_ORIGINAL, ACT_P_TIMELINE_ZOOM_ORIGINAL, ICONID_TIMELINE_ZOOM_ORIGINAL );
  //
  // ITsActionDef AI_TIMELINE_ZOOM_IN = TsActionDef.ofPush2( AID_TIMELINE_ZOOM_IN, //
  // ACT_T_TIMELINE_ZOOM_IN, ACT_P_TIMELINE_ZOOM_IN, ICONID_TIMELINE_ZOOM_IN );

  ITsActionDef ACDEF_ONE_BY_ONE = TsActionDef.ofCheck2( ACTID_ONE_BY_ONE, //
      ACT_T_ONE_BY_ONE, ACT_P_ONE_BY_ONE, ICON_ONE_BY_ONE );

}
