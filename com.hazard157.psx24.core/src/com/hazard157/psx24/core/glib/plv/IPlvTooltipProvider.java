package com.hazard157.psx24.core.glib.plv;

import org.eclipse.jface.window.*;
import org.eclipse.swt.widgets.*;

/**
 * Поставщик данных для формирования всплывающей подсказки к элементам {@link PlvItem}.
 *
 * @author hazard157
 */
public interface IPlvTooltipProvider {

  /**
   * Реализация должна создать виджет, который появится как всплывающая подсказка к элементу.
   * <p>
   * Меееод работает аналогично {@link DefaultToolTip}<code>.createToolTipContentArea()</code>, только вместо события
   * мыши передается найденный элемент.
   *
   * @param aItem {@link PlvItem} - элемент для отображения подсказки или <code>null</code> вне элемента
   * @param aEvent {@link Event} - событие, привдящее к "всплытию" подсказки
   * @param aTooltipParent {@link Composite} - родительская компонента, котоая и "всплывает"
   * @return {@link Composite} - созданное содержимое подсказки
   */
  Composite createToolTipContentArea( PlvItem aItem, Event aEvent, Composite aTooltipParent );

}
