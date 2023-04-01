package com.hazard157.psx24.core.e4.uiparts.eps;

/**
 * Локализуемые ресусры.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link UipartEpisodeStory}
   */
  String STR_STORY_TOOLBAR_NAME         = "Сцены:";
  String ACT_T_ADD_TAG_INTERVAL         = "Ярлык";
  String ACT_P_ADD_TAG_INTERVAL         = "Пометить интервал ярлыком";
  String MSG_WARN_FIRST_SELECT_SCENE    = "Выберите сцену, после, перед или дочерным которой хотите создать новую";
  String MSG_ERR_NO_PLACES_TO_ADD_SCENE = "Некуда добавлять: нет места перед, после или среди дочек выбранной сцены";
  String DLG_C_ADD_SCENE_PLACE          = "Новая сцена";
  String DLG_T_ADD_SCENE_PLACE          = "Выберите место относительно текущей сцены,\n куда будет добалена новая";
  String DLG_C_SCENE_INTERVAL           = "Интервал сцены";
  String DLG_T_SCENE_INTERVAL           = "Задайте интервал и свойства сцены";

  /**
   * {@link UipartEpisodeTags}
   */
  String MSG_ENTER_FILTER_TEXT   = "Введите текст отбора ярлыков";
  String ASK_DELETE_TAG_INTERVAL = "Действительно удалить ярлык %s в интервале %s ?";
  String STR_L_TAG_DESCRIPTION   = "Ярлык: ";
  String STR_L_TAG_SECLINE       = "Занято: ";
  String STR_L_TAG_GAPS          = "Свободно: ";
  String STR_H_NAME              = "Ярлык";
  String STR_H_DESCRIPTION       = "Описание";

}
