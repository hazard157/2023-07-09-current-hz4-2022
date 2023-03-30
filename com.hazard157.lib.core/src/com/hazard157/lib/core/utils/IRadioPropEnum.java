package com.hazard157.lib.core.utils;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Интерфейс констант перечисления, которые служат как значение (одно из enum-а) какого либо свойства.
 * <p>
 * Любое значение должно иметь константу "неизвестный" с идентификатором {@link #UNKNOWN_ID}. Кроме этого, следует
 * придерживаться следующих правил: Java-константа неизвестного значения должна иметь имя <code>UNKNOWN</code> и быть
 * первым в перечне констант enum-перечисления.
 *
 * @author hazard157
 */
public interface IRadioPropEnum
    extends IStridable {

  /**
   * Идентификатор неизвестного значения, то есть, значение еще не определено.
   */
  String UNKNOWN_ID = "Unknown"; //$NON-NLS-1$

  /**
   * Определят, является ли эта константа константой неизвестного значения.
   *
   * @return boolean - признак неизвестного значения
   */
  default boolean isUnknown() {
    return id().equals( UNKNOWN_ID );
  }

  /**
   * Определят, является ли эта константа константой известного значения.
   *
   * @return boolean - признак известного значения
   */
  default boolean isKnown() {
    return !isUnknown();
  }

  /**
   * Returns the icon ID corresponding to this enum constant.
   * <p>
   * The icon ID is used to load icon image by method {@link ITsIconManager#loadStdIcon(String, EIconSize)}. The
   * <code>enum</code> implemetation must include icon omages to be included in plugin and registered in
   * {@link ITsIconManager}.
   *
   * @return String - the icon ID or <code>null</code> for no icon
   */
  default String iconId() {
    return null;
  }

}
