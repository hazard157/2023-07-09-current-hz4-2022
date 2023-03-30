package com.hazard157.lib.core.quants.valeds.radioprop;

import com.hazard157.lib.core.utils.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * Common.
   */
  String FMT_P_SELECION_LINE = "Левый щелчок - %s\nПравый щелчок - %s";

  /**
   * {@link ValedRadioPropEnumIcons}
   */
  String STR_N_ICON_SIZE      = "Размер";
  String STR_D_ICON_SIZE      = "Размер звездочек в линейке";
  String STR_N_ICON_GAP       = "Интервал";
  String STR_D_ICON_GAP       = "Интервал в пикселях между значками";
  String STR_N_SEL_RECT_COLOR = "Цвет выделения";
  String STR_D_SEL_RECT_COLOR = "RGB цвет прямоугольника выделения значка выбранной константы";

  /**
   * /** {@link ValedRadioPropEnumStars}
   */
  String FMT_ERR_NO_FIRST_UNKNOWN_VALUE_IN_ENUM =
      "%s: у enum %s первой константой должно быть " + IRadioPropEnum.UNKNOWN_ID;

  /**
   * {@link IconsLineWidget}
   */
  String MSG_ERR_EMPTY_ITEMS   = "Список отображаемых элементов не должен быть пустым";
  String FMT_ERR_INV_DEF_INDEX = "Индекс по умолчанию %d выходит за пределы 0..%d";

}
