package com.hazard157.lib.core;

import static com.hazard157.lib.core.IHzResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;

/**
 * Library constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IHzLibConstants {

  /**
   * com.hazard157.* package constants prefix.
   */
  String HZ_ID = "com.hazard157"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Значки
  String PREFIX_OF_ICON_FIELD_NAME = "ICON";                //$NON-NLS-1$
  String ICON_STAR_YELLOW          = "star-yellow";         //$NON-NLS-1$
  String ICON_STAR_DIMMED          = "star-dimmed";         //$NON-NLS-1$
  String ICON_VLG_UNKNOWN          = "vlg-unknown";         //$NON-NLS-1$
  String ICON_VLG_DECENT           = "vlg-decent";          //$NON-NLS-1$
  String ICON_VLG_EROTICA          = "vlg-erotica";         //$NON-NLS-1$
  String ICON_VLG_PORNO            = "vlg-porno";           //$NON-NLS-1$
  String ICON_SFZ_UNKNOWN          = "sfz-unknown";         //$NON-NLS-1$
  String ICON_SFZ_FULL             = "sfz-full";            //$NON-NLS-1$
  String ICON_SFZ_AMERICAN         = "sfz-american";        //$NON-NLS-1$
  String ICON_SFZ_MEDIUM           = "sfz-medium";          //$NON-NLS-1$
  String ICON_SFZ_CLOSEUP          = "sfz-closeup";         //$NON-NLS-1$
  String ICON_GWENVIEW             = "gwenview";            //$NON-NLS-1$
  String ICON_THUMB_ZOOM_ORIGINAL  = "thumb-zoom-original"; //$NON-NLS-1$
  String ICON_THUMB_ZOOM_IN        = "thumb-zoom-in";       //$NON-NLS-1$
  String ICON_THUMB_ZOOM_OUT       = "thumb-zoom-out";      //$NON-NLS-1$
  String ICONID_AK_ANIMATED        = "ak-animated";         //$NON-NLS-1$
  String ICONID_AK_BOTH            = "ak-both";             //$NON-NLS-1$
  String ICONID_AK_SINGLE          = "ak-single";           //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_RUN_GWENVIEW = HZ_ID + ".act.RunGwenview"; //$NON-NLS-1$

  ITsActionDef ACDEF_RUN_GWENVIEW = TsActionDef.ofPush1( ACTID_RUN_GWENVIEW, //
      TSID_NAME, STR_T_RUN_GWENVIEW, //
      TSID_DESCRIPTION, STR_P_RUN_GWENVIEW, //
      TSID_ICON_ID, ICON_GWENVIEW //
  );

  /**
   * Drop down menu thumb size managment via {@link IThumbSizeable}.
   * <p>
   * Button fires action {@link IStdActions#ACTID_ZOOM_ORIGINAL}, drop down menu items fire actions
   * {@link IStdActions#ACTID_ZOOM_OUT} and {@link IStdActions#ACTID_ZOOM_IN}.
   */
  ITsActionDef AI_IMAGE_SIZEABLE_ZOOM_MENU = TsActionDef.ofMenu2( ACTID_ZOOM_ORIGINAL, //
      ACDEF_ZOOM_ORIGINAL.nmName(), ACDEF_ZOOM_ORIGINAL.description(), ICONID_ZOOM_ORIGINAL );

  /**
   * Действие с выпадающим меню для управления размером значка {@link IIconSizeable}.
   * <p>
   * Кнопка вызывает действие {@link IStdActions#ACTID_ZOOM_ORIGINAL}, а два пункта выпадающего меню - действия
   * {@link IStdActions#ACTID_ZOOM_OUT} и {@link IStdActions#ACTID_ZOOM_IN}.
   */
  ITsActionDef AI_ICON_SIZEABLE_ZOOM_MENU = TsActionDef.ofMenu2( ACTID_ZOOM_ORIGINAL, //
      ACDEF_ZOOM_ORIGINAL.nmName(), ACDEF_ZOOM_ORIGINAL.description(), ICONID_ZOOM_ORIGINAL );

  // ------------------------------------------------------------------------------------
  // Misc

  String PROG_GWENVIEW = "gwenview"; //$NON-NLS-1$

  /**
   * Initialization.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    // регистрация значков
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IHzLibConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
