package com.hazard157.psx.proj3.tags;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link ITagsConstants}
   */
  String STR_N_TIP_DESCRIPTION       = DDEF_DESCRIPTION.nmName();
  String STR_D_TIP_DESCRIPTION       = DDEF_DESCRIPTION.description();
  String STR_N_TIP_IS_LEAF           = "Лист?";
  String STR_D_TIP_IS_LEAF           = "Признак, что ярлык не может иметь дочерные ярлыки";
  String STR_N_TIP_IS_RADIO          = "Радио?";
  String STR_D_TIP_IS_RADIO          = "Признак, что это радио-группа дочерных ярлыков";
  String STR_N_TIP_IS_MANDATORY      = "Обязан?";
  String STR_D_TIP_IS_MANDATORY      = "Признак, что один из дочерных ярлыков обязательно должен быть в пометках";
  String STR_N_TIP_ICON_NAME         = "Значок";
  String STR_D_TIP_ICON_NAME         = "Имя файла (путь) значка";
  String STR_N_TIP_IN_RADIO_PRIORITY = "Приоритет";
  String STR_D_TIP_IN_RADIO_PRIORITY =
      "Приоритет при выборе ярлыка внутри радиогруппы (сначала назначается более высокий приоритет) -100..+100";

}
