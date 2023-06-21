package com.hazard157.prisex24.m5.camera;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link CameraM5Model}
   */
  String STR_N_M5M_CAMERA                 = "Камера";
  String STR_D_M5M_CAMERA                 = "Камера, используемая для съемок";
  String STR_N_CAM_ID                     = "Идентификатор";
  String STR_D_CAM_ID                     = "Идентификатор камеры";
  String STR_N_CAM_NAME                   = "Название";
  String STR_D_CAM_NAME                   = "Название камеры";
  String STR_N_CAM_DESCRIPTION            = "Описание";
  String STR_D_CAM_DESCRIPTION            = "Описание камеры";
  String STR_N_CAM_IS_STILL_AVAILABLE     = "Доступен?";
  String STR_D_CAM_IS_STILL_AVAILABLE     = "Признак, что камера еще доступна для съемок";
  String STR_N_CAM_USE_COUNT              = "Снято";
  String STR_D_CAM_USE_COUNT              = "Сколько исходных видео было снято камерой";
  String STR_N_CAMERA_KIND                = "Тип камеры";
  String STR_D_CAMERA_KIND                = "Разновидность снимающей камеры";
  String FMT_ERR_CAMERA_ID_ALREADY_EXISTS = "Камера с идентификатором '%s' уже существует";

  /**
   * {@link PanelUnitCameras}
   */
  String STR_N_GROUP_BY_AVAIL        = "По наличию";
  String STR_D_GROUP_BY_AVAIL        = "Группировка по наличию камеры в настоящее время";
  String STR_N_GROUP_BY_USAGE        = "По использованию";
  String STR_D_GROUP_BY_USAGE        = "Группировка по использованию";
  String STR_N_GROUP_BY_TYPE         = "По типу";
  String STR_D_GROUP_BY_TYPE         = "Группировка по типам камер";
  String STR_N_NODE_AVAILABLE_CAMS   = "Доступные";
  String STR_N_NODE_UNAVAILABLE_CAMS = "Более недоступные";
  String STR_N_NODE_USED_CAMS        = "Было использованы";
  String STR_N_NODE_UNUSED_CAMS      = "Ничего не снимали";

}
