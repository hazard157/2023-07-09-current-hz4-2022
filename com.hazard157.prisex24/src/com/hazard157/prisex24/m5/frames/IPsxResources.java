package com.hazard157.prisex24.m5.frames;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link FrameCollectionPanelTmm}
   */
  String STR_UNSCENED_NODE          = "Кадры вне сцен сюжета";
  String STR_UNNOTED_NODE           = "Кадры вне интевалов с заметками";
  String STR_TM_BY_MINUTES          = "По минутам";
  String STR_TM_BY_MINUTES_D        = "Группировка кадров по минутам";
  String STR_TM_BY_20SEC            = "По 20 сек";
  String STR_TM_BY_20SEC_D          = "Группировка кадров по 20 екундным интервалам";
  String STR_TM_BY_SCENES           = "По сценам";
  String STR_TM_BY_SCENES_D         = "Сцены сюжета с кадрами";
  String STR_TM_BY_NOTES            = "По заметкам";
  String STR_TM_BY_NOTES_D          = "Группировка кадров по интервалам пометок";
  String FMT_ERR_NOT_ONE_EP_FRAMES  = "В списке есть кадры более одного эпизода (%s, %s),\nОтображение невозможно";
  String MSG_WARN_NO_FRAMES_TO_SHOW = "Нет пригодных к отображению кадров";

  /**
   * {@link FrameM5Model}
   */
  String STR_N_M5M_FRAME      = "Кадр";
  String STR_D_M5M_FRAME      = "Кадр из эпизода";
  String STR_N_FR_FRAME_NO    = "№ кадра";
  String STR_D_FR_FRAME_NO    = "Номер кадра в исходнеом клипе";
  String STR_N_FR_IS_ANIMATED = "Клип?";
  String STR_D_FR_IS_ANIMATED = "Признак анимированного изображения (мини-клипа)";
  String STR_N_FR_FRAME       = "Изображение";
  String STR_D_FR_FRAME       = "Изображение кадра для визуального просмотра";

}
