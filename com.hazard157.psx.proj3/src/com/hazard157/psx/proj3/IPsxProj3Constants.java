package com.hazard157.psx.proj3;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Константы GUI приложения.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxProj3Constants {

  // ------------------------------------------------------------------------------------
  // Значки
  //

  String PREFIX_OF_ICON_FIELD_NAME    = "ICONID_";               //$NON-NLS-1$
  String ICONID_CAMERA_GENERIC        = "camera-generic";        //$NON-NLS-1$
  String ICONID_CAMERA_VHS            = "camera-vhs";            //$NON-NLS-1$
  String ICONID_CAMERA_DV             = "camera-dv";             //$NON-NLS-1$
  String ICONID_CAMERA_WEB            = "camera-web";            //$NON-NLS-1$
  String ICONID_CAMERA_FOTO           = "camera-foto";           //$NON-NLS-1$
  String ICONID_CAMERA_PHONE          = "camera-phone";          //$NON-NLS-1$
  String ICONID_CAMERA_GENERIC_DIMMED = "camera-generic-dimmed"; //$NON-NLS-1$
  String ICONID_CAMERA_VHS_DIMMED     = "camera-vhs-dimmed";     //$NON-NLS-1$
  String ICONID_CAMERA_DV_DIMMED      = "camera-dv-dimmed";      //$NON-NLS-1$
  String ICONID_CAMERA_WEB_DIMMED     = "camera-web-dimmed";     //$NON-NLS-1$
  String ICONID_CAMERA_FOTO_DIMMED    = "camera-foto-dimmed";    //$NON-NLS-1$
  String ICONID_CAMERA_PHONE_DIMMED   = "camera-phone-dimmed";   //$NON-NLS-1$
  String ICONID_EPISODE               = "incident-episode";      //$NON-NLS-1$
  String ICONID_GAZE                  = "incident-gaze";         //$NON-NLS-1$
  // String ICONID_TAG = "tag"; //$NON-NLS-1$
  // String ICONID_TAGS_LIST = "tags-list"; //$NON-NLS-1$
  // String ICONID_PLAYBACK_START = "media-playback-start"; //$NON-NLS-1$
  // String ICONID_WORK_WITH_FRAMES = "work-with-frames"; //$NON-NLS-1$
  // String ICONID_KDENLIVE = "kdenlive"; //$NON-NLS-1$
  // String ICONID_FILM = "film"; //$NON-NLS-1$
  // String ICONID_FILMS_LIST = "films-list"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPsxProj3Constants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
