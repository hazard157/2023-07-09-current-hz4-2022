package com.hazard157.lib.core.excl_done.secint;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link Secint}
   */
  String FMT_ERR_CANT_CREATE_START_LT_0   = "Время начала интервала %s меньше 0";
  String FMT_ERR_CANT_CREATE_START_GT_MAX = "Время начала интервала %s больше максимально допустимого %s";
  String FMT_ERR_CANT_CREATE_END_LT_0     = "Время окончания интервала %s меньше 0";
  String FMT_ERR_CANT_CREATE_END_GT_MAX   = "Время окончания интервала %s больше максимально допустимого %s";
  String FMT_ERR_CANT_CREATE_START_GT_END = "Время начала интервала %s позже времени окончания %s";

}
