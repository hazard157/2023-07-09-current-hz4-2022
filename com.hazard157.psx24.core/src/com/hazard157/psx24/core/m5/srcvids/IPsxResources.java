package com.hazard157.psx24.core.m5.srcvids;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link EpisodeSourceVideoLifecycleManager}
   */
  String MSG_ERR_CANT_CHANGE_EPISODE_ID = "Нельзя менять эпизод!";
  String MSG_ERR_CANT_CHANGE_CAMERA_ID  = "Нельзя менять камеру!";

  /**
   * {@link PanelUnitSourceVideos}
   */
  String STR_N_TREE_BY_CAMERAS  = "Камеры";
  String STR_D_TREE_BY_CAMERAS  = "Группировка исходных видео по снимавшим камерам";
  String STR_N_TREE_BY_EPISODES = "Эпизоды";
  String STR_D_TREE_BY_EPISODES = "Группировка исходных видео по эпизодам";
  String STR_N_SVL_THUMB_SIZE   = "Миниатюры исходников";
  String STR_D_SVL_THUMB_SIZE   = "Размер миниатюр исходных видео в списках";

  /**
   * {@link SourceVideoM5Model}
   */
  String STR_N_M5M_SOURCE_VIDEO = "Исходное видео";
  String STR_D_M5M_SOURCE_VIDEO = "Информация об исходном видео эпизода";
  String STR_N_SV_ID            = "Идентификатор";
  String STR_D_SV_ID            = "Идентификатор исходного видео";
  String STR_N_SV_EPISODE_ID    = "Эпизод";
  String STR_D_SV_EPISODE_ID    = "Эпизод исходного видео";
  String STR_N_SV_CAM_ID        = "Камера";
  String STR_D_SV_CAM_ID        = "Камера, которым снато исходное видео";
  String STR_N_SV_LOCATION      = "Место";
  String STR_D_SV_LOCATION      = "Расположение снимающей камеры";
  String STR_N_SV_DESCRIPTION   = "Описание";
  String STR_D_SV_DESCRIPTION   = "Пояснения к видео, к съемке - направление, увеличение и др.";
  String STR_N_SV_DURATION      = "Длительность";
  String STR_D_SV_DURATION      = "Длительность исходного видео (сек)";

}
