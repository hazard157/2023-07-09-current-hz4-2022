package com.hazard157.lib.core.excl_done.sfz;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IHzResources {

  /**
   * {@link EShotFieldSize}
   */
  String STR_N_SFZ_FULL     = "Полный";
  String STR_D_SFZ_FULL     = "Люди видны полностью в кадре, и еще остается место";
  String STR_N_SFZ_AMERICAN = "3/4";
  String STR_D_SFZ_AMERICAN = "Съемка в 3/4, когда небольшая часть тела не помещаетс в кад";
  String STR_N_SFZ_MEDUM    = "Средний";
  String STR_D_SFZ_MEDUM    = "Средний план, показывается полностью часть тела и сопредельные части "
      + "(например, голова, плечи и предплечья, часть груди)";
  String STR_N_SFZ_CLOSEUP  = "Крупный";
  String STR_D_SFZ_CLOSEUP  = "Крупный план, часть тела не помещается в кадр (например, показано лицо,"
      + " но уши, волосы и подбородок не помещаются в кадр)";
  String STR_N_SFZ_UNKNOWN  = "Неизвестный";
  String STR_D_SFZ_UNKNOWN  = "Пока неизвестная крупность плана";

  /**
   * {@link IShotFieldSizeConstants}
   */
  String STR_N_M5M_SHOT_FIELD_SIZE = "Оценка";
  String STR_D_M5M_SHOT_FIELD_SIZE = "Качественная оценка";

}
