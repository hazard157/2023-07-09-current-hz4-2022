package com.hazard157.psx24.core.glib.tagint;

/**
 * Локализуемые ресусры.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link DialogTagInterval}
   */
  String FMT_ERR_END_LESS_START          = "Время окончания интервала";
  String MSG_ERR_TAG_ID_MUST_BE_SELECTED = "Ярлык должен быть выбран";
  String FMT_ERR_NON_LEAF_TAG_SELECTED   = "Допускается вбырать только ярлыки - листья дерева";
  String DLG_C_TAG_INTERVAL              = "Интервал ярлыка";
  String DLG_T_TAG_INTERVAL              = "Выберите ярлык и задайте интервал";
  String STR_L_INTERVAL                  = "Интервал";
  // String MSG_ENTER_FILTER_TEXT = "Введите текст отбора ярлыков";

  /**
   * {@link TagMarksTreePanel}
   */
  String STR_COL_NAME_TAG_ID          = "Идентификатор";
  String STR_COL_NAME_TAG_DESCRIPTION = "Описание ярлыка";

}
