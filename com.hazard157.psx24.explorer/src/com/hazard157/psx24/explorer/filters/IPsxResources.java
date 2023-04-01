package com.hazard157.psx24.explorer.filters;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link EPqSingleFilterKind}
   */
  String STR_N_ANY_TEXT    = "Поиск текста";
  String STR_D_ANY_TEXT    = "Поиск любого текста в во всей информации об эпизодах";
  String STR_N_TAG_IDS     = "Пометки ярлыками";
  String STR_D_TAG_IDS     = "Поиск в эпизодах по пометкам ярлыками";
  String STR_N_EPISODE_IDS = "Выбор эпизодов";
  String STR_D_EPISODE_IDS = "Отбор эпизодов для выборки";

  /**
   * {@link PqFilterAnyTextPanel}
   */
  String STR_N_TEXT         = "Текст";
  String STR_D_TEXT         = "Искомый текст";
  String STR_N_SEARCH_IN    = "Поиск в: ";
  String STR_D_SEARCH_IN    = "Искать вхождение текста в следующих местах";
  String STR_N_MATCH_MODE   = "Режим";
  String STR_D_MATCH_MODE   = "Режим посика текста";
  String STR_N_CHECK_TAGS   = "ярлыках?";
  String STR_D_CHECK_TAGS   = "Поиск текста в пометках ярлыками";
  String STR_N_CHECK_SCENES = "сценах?";
  String STR_D_CHECK_SCENES = "Поиск текста в названиях сцен";
  String STR_N_CHECK_NOTES  = "заметках?";
  String STR_D_CHECK_NOTES  = "Поиск текста в заметках к эпизоду";
  String STR_N_CHECK_PLANES = "планах?";
  String STR_D_CHECK_PLANES = "Поиск текста в планах съемки";

  /**
   * {@link PqFilterEpisodeIdsPanel}
   */
  String STR_N_EP_IDS         = "Эпизоды:";
  String STR_D_EP_IDS         = "Идентификаторы выборанных эпизодов (пусто = все эпизоды)";
  String STR_T_SEL_EP_IDS_BTN = "...";
  String STR_P_SEL_EP_IDS_BTN = "Пометка нужных эпизодов в списке всех эпизодов";

  /**
   * {@link PqFilterTagIdsPanel}
   */
  String STR_N_TAGS_IS_ANY = "Ярлыки по ИЛИ";
  String STR_D_TAGS_IS_ANY = "Признак выбщрки если хотя бы один ярлык есть в интервале, иначе должны быть все ярлыки";

}
