package com.hazard157.psx.proj3.episodes;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link IEpVerifyCfg}
   */
  String STR_N_IS_MIN_SCENE_DURATION_WARNING_DISABLED     = "Короткие сцены?";
  String STR_D_IS_MIN_SCENE_DURATION_WARNING_DISABLED     =
      "Определяет, отключено ли предупреждение о слишком коротких длителностьях сцен";
  String STR_N_IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED = "Много сцен?";
  String STR_D_IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED =
      "Определяет, отключено ли предупреждение о слишком большом количестве дочерных сцен";

}
