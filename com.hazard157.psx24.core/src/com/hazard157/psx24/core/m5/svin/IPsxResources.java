package com.hazard157.psx24.core.m5.svin;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link SvinM5Model}
   */
  String STR_N_M5M_SVIN = "Интервал";
  String STR_D_M5M_SVIN = "Часть эпизода, возможно с уточнением исходного видео (камеры)";
  String STR_N_DURATION = "Время";
  String STR_D_DURATION = "Длительность интервала";

  /**
   * {@link SvinM5Mpc}
   */
  String STR_N_TMM_BY_EPISODE                = "По эпизодам";
  String STR_D_TMM_BY_EPISODE                = "Группировка интервалов по эпизодам";
  String STR_N_TMM_BY_CAMERA                 = "По камерам";
  String STR_D_TMM_BY_CAMERA                 = "Группировка интервалов по камрам характерных кадров";
  String STR_N_SHOW_ALL_SVINS_IN_FRAMES_LIST = "Все в списке";
  String STR_D_SHOW_ALL_SVINS_IN_FRAMES_LIST = "Показать по кадру каждого интервала разм в списке кадров";
  String FMT_FILTERED_SUMMARY                = "N= %d (%d), Σ= %s (%s) Neps= %d (%d)";
  String FMT_NON_FILTER_SUMMARY              = "N= %d, Σ= %s Neps= %d";

}
