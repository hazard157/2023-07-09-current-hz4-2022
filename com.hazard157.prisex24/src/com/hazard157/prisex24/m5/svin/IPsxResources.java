package com.hazard157.prisex24.m5.svin;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link SvinM5Model}
   */
  String STR_M5M_SVIN   = "Интервал";
  String STR_M5M_SVIN_D = "Часть эпизода, возможно с уточнением исходного видео (камеры)";
  String STR_DURATION   = "Время";
  String STR_DURATION_D = "Длительность интервала";

  /**
   * {@link SvinM5Mpc}
   */
  String STR_N_TMM_BY_EPISODE                = "По эпизодам";
  String STR_D_TMM_BY_EPISODE                = "Группировка интервалов по эпизодам";
  String STR_N_TMM_BY_EP_SECINT              = "По интервалам";
  String STR_D_TMM_BY_EP_SECINT              = "Группировка интервалов по интервалам эпизодов";
  String STR_N_TMM_BY_CAMERA                 = "По камерам";
  String STR_D_TMM_BY_CAMERA                 = "Группировка интервалов по камрам характерных кадров";
  String STR_N_SHOW_ALL_SVINS_IN_FRAMES_LIST = "Все в списке";
  String STR_D_SHOW_ALL_SVINS_IN_FRAMES_LIST = "Показать по кадру каждого интервала разм в списке кадров";
  String FMT_FILTERED_SUMMARY                = "N= %d (%d), Σ= %s (%s) Neps= %d (%d)";
  String FMT_NON_FILTER_SUMMARY              = "N= %d, Σ= %s Neps= %d";

}
