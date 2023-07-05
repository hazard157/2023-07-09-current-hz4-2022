package com.hazard157.prisex24.explorer;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface IPsxResources {

  String STR_T_AS_LIST = "Список";
  String STR_P_AS_LIST = "Результаты в виде простого списка";
  String STR_T_AS_GRID = "Миниатюры";
  String STR_P_AS_GRID = "Результаты в виде сетки миниатюр";
  String STR_T_AS_TREE = "Дерево";
  String STR_P_AS_TREE = "Результаты в виде дерева по эпизодам";

  /**
   * {@link PanelAllFiltersSet}, {@link PanelAllFiltersSetLadder}
   */
  String STR_N_FILTER_EP_IDS          = "Выборка по эпизодам";
  String STR_N_FILTER_ANY_TEXT        = "Выборка любого текста";
  String STR_N_FILTER_TAG_IDS         = "Выборка по ярлыкам";
  String STR_N_CLEAR_FILTER           = "Сброс";
  String STR_D_CLEAR_FILTER           = "Сбросить фильтра на начения по умолчанию";
  String STR_N_INVERT_FILTER          = "НЕТ";
  String STR_D_INVERT_FILTER          = "Применить оператор НЕТ к фильтру";
  String STR_ALL_FP                   = "Всё";
  String STR_N_TAB_FILTER_EPISODE_IDS = "Эпизоды";
  String STR_N_TAB_FILTER_ANY_TEXT    = "Текст";
  String STR_N_TAB_FILTER_TAG_IDS     = "Ярлыки";

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
