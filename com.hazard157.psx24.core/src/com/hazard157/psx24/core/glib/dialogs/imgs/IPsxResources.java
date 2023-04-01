package com.hazard157.psx24.core.glib.dialogs.imgs;

/**
 * Локализуемые ресурсы общей библиотеки.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link DialogPsxShowFullSizedFrameImage}
   */
  String STR_C_DPSFSFI_IMAGE = "Просмотр изображения кадра";
  String FMT_T_DPSFSFI_IMAGE = "Кадр %s";

  /**
   * {@link DialogShowFullSizedImageFromFile}
   */
  String STR_C_DSFSI_IMAGE = "Просмотр изображения";
  String FMT_T_DSFSI_IMAGE = "Файл %s";
  String ACT_T_COPY_IMAGE  = "Копировать";
  String ACT_P_COPY_IMAGE  = "Скопировать файл изображения в указанную папку";

  /**
   * {@link DialogWorkWithFrames}
   */
  String STR_C_DWWF                    = "Кадры";
  String STR_T_DWWF                    = "Просмотр и работа с фалоами и изображениями кадров эпизодов";
  String STR_AGCP_EPISODE              = "Эпизод:";
  String STR_AGCP_INTERVAL             = "Интервал:";
  String STR_AGCP_CAMID                = "Камера:";
  String STR_T_BTN_INTERVAL_FROM_SCENE = "...";
  String STR_P_BTN_INTERVAL_FROM_SCENE = "Выбор интеравала по сцене эпизода";
  String STR_T_BTN_ALL_CAMS            = "Все";
  String STR_P_BTN_ALL_CAMS            = "Просмотреть все кадры камер в одном списке";
  String MSG_NO_EPSIDEOS_DEFINED       = "Нечего создавать - в проекте нет ни одного эпизода";

}
