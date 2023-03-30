package com.hazard157.psx24.core.m5.plane;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link MarkPlaneGuideM5Model}
   */
  String STR_N_FIELD_START             = "Начало";
  String STR_D_FIELD_START             = "Момент времени начала главы";
  String STR_N_FIELD_DURATION          = "Длительность";
  String STR_D_FIELD_DURATION          = "Продолжительность главы главы";
  String STR_N_FIELD_NAME              = "Название";
  String STR_D_FIELD_NAME              = "Название главы ";
  String STR_N_FIELD_IS_NATURALLY_LONG = "Длительный?";
  String STR_D_FIELD_IS_NATURALLY_LONG = "Признак естественно длительного плана";
  String STR_N_FIELD_CAMERA_ID         = "Камера";
  String STR_D_FIELD_CAMERA_ID         = "Снимающая камера";
  String STR_N_FIELD_INTERVAL          = "Интервал";
  String STR_D_FIELD_INTERVAL          = "Интервал главы";
  String STR_N_FIELD_GUIDE             = "Глава";
  String STR_D_FIELD_GUIDE             = "Свойства главы ";
  String FMT_ERR_ALREADY_START         = "В момент времени %s уже начинается глава";
  String FMT_ERR_NO_START              = "Нет главы с началом %s";

}
