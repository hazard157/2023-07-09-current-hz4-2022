package com.hazard157.psx.proj3.episodes.story;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link AbstractScene}
   */
  String FMT_ERR_CANT_ADD_SCENE_INTERSECTION        = "Добавляемая сцена %s пересекается с существующим %s";
  String FMT_ERR_CANT_ADD_OUT_OF_RANGE              = "Добавляемая сцена %s выходит за пределы родителя %s";
  String FMT_ERR_NO_SUCH_SCENE                      = "Нет сцены с интервалом %s";
  String FMT_ERR_CANT_RELOCATE_SCENE_INTERSECTION   = "При перемещении %s сцена пересекается с существующим %s";
  String FMT_ERR_CANT_RELOCATE_OUT_OF_RANGE         = "При перемещении %s сцена выходит за пределы родителя %s";
  String FMT_WARN_FRAME_OUTSIDE_SCENE               = "Иллюстрирующий кадр %s находится вне интервала %s";
  String FMT_WARN_UNSET_SCENE_NAME                  = "Рекомендуется задать название сцены";
  String FMT_ERR_NEW_INTERVAL_TOO_NARROW            = "Новый инетрвал %s уже чем %s - уже знаятый дочерными сценами";
  String FMT_WARN_DELETING_SCENE_WTH_CHILDS         = "У сцены %s есть дочерные сцены";
  String FMT_ERR_CANT_REDUCE_SCENE_LESS_THAN_CHILDS =
      "Длительность %s нового инетрвала %s уже чем %s - уже знаятый дочерными сценами";

  /**
   * {@link Story}
   */
  String STR_N_STORY = "СЮЖЕТ - весь эпизод";

}
