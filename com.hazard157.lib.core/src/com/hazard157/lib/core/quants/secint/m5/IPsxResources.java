package com.hazard157.lib.core.quants.secint.m5;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link ISecintM5Constants}
   */
  String STR_N_DT_VIDEO_POSITION = "Позиция";
  String STR_D_DT_VIDEO_POSITION = "Позициия (в секундах) в видеоматериале";
  String STR_N_DT_VIDEO_DURATION = "Длительность";
  String STR_D_DT_VIDEO_DURATION = "Длительность (в секунах) в аудио-видеоматериалах";
  String STR_N_DT_VIDEO_INTERVAL = "Интервал";
  String STR_D_DT_VIDEO_INTERVAL = "Интервал начало-окончание (в секунах) в аудио-видеоматериалах";

  /**
   * {@link SecintM5Model}
   */
  String STR_N_M5M_SECINT            = "Интервал";
  String STR_D_M5M_SECINT            = "Интервал времени (в секундах)";
  String STR_N_SN_START              = "Начало";
  String STR_D_SN_START              = "Начало интервала в секундах";
  String STR_N_SN_END                = "Окончание";
  String STR_D_SN_END                = "Окончание интервала в секундах";
  String STR_N_SN_DURATION           = "Длительность";
  String STR_D_SN_DURATION           = "Длительность интервала в секундах";
  String FMT_ERR_SECINT_START_LT_0   = "Начало %s интервалоа не может быть отрицательным";
  String FMT_ERR_SECINT_END_LT_0     = "Окончание %s интервалоа не может быть отрицательным";
  String FMT_ERR_SECINT_START_GT_END = "Начало %s интервалоа не может быть позже окончани %s";
  String FMT_ERR_SECINT_INV_DUR      = "Длительность %s интервалоа не равна разнице между началом %s и окончанием %s";

}
