package com.hazard157.psx24.core.m5.tags;

import com.hazard157.psx.proj3.tags.*;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IPsxResource {

  /**
   * {@link TagLifecycleManager}
   */
  String MSG_ERR_TAG_INV_MASTER_OBJ          = "Мастер-объект должен быть " + ITag.class.getSimpleName();
  String FMT_ERR_CANT_MAKE_CHILDED_NODE_LEAF = "У ярлык '%s' есть дочерные ярлыки, нельзя его делать листом";
  String FMT_ERR_TAG_NAME_ALREADY_EXISTS     = "У узла '%s' уже есть ярлык с именем '%s'";
  String FMT_WARN_REMOVE_TAG_WITH_CHILDS     = "У ярлыка %s есть дочерные ярлыки!";

  /**
   * {@link TagM5Model}
   */
  String STR_N_TAG       = "Ярлык";
  String STR_D_TAG       = "Ярлык для пометки сущностей";
  String STR_N_TAG_ID    = "Идентификатор";
  String STR_D_TAG_ID    = "Полный идентификатор ярлыка";
  String STR_N_TAG_NAME  = "Название";
  String STR_D_TAG_NAME  = "Названия ярлыка (последняя компонента идентификатора)";
  String STR_N_ICON_NAME = "Значок";
  String STR_D_ICON_NAME = "Имя фалй значка для отображения ключевого слова";

  /**
   * {@link TagMpc}
   */
  String STR_N_TMI_BY_GROUP        = "Группы";
  String STR_D_TMI_BY_GROUP        = "Дерево по граппам ярлыков";
  String FMT_ERR_NO_CHILDS_IN_LEAF = "Ярлык %s не поддерживает создание дочерных ярлыков";
  String DLG_T_FMT_NEW_CHILD_TAG   = "Создание дочернего ярлыка в группе %s";
  String DLG_T_NEW_ROOT_TAG        = "Создание корневого ярлыка";
  String DLG_C_NEW_TAG             = "Создание ярлыка";

}
