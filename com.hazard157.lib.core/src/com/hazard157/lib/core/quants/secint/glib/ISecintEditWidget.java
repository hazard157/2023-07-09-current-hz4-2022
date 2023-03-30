package com.hazard157.lib.core.quants.secint.glib;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Виджет редактирования значений типа {@link Secint}.
 *
 * @author goga
 */
public interface ISecintEditWidget
    extends IGenericChangeEventCapable, ILazyControl<Control> {

  /**
   * Определяет, используется ли формат представления "ММ:СС" или "ЧЧ:ММ:СС".
   * <p>
   * В зависимости от формата представления ("ММ:СС" или "ЧЧ:ММ:СС"), максимальные значения начала и окончания интервала
   * ограничены (соотвественно, величиной {@link HmsUtils#MAX_MMSS_VALUE} или {@link HmsUtils#MAX_HHMMSS_VALUE}).
   *
   * @return boolean - признак использования "ММ:СС" (а не "ЧЧ:ММ:СС")
   */
  boolean isOnlyMmSs();

  /**
   * Задает формат представления "ММ:СС" или "ЧЧ:ММ:СС".
   * <p>
   * При изменении формата уже созданного виджета, он пересоздает редакторы начала, окончания и длительности. Поэтому,
   * может потребовать обновление раскладки (layout) родительского контроля.
   *
   * @param aIsOnlyMmSs boolean - признак использования "ММ:СС" (а не "ЧЧ:ММ:СС")
   */
  void setOnlyMmSs( boolean aIsOnlyMmSs );

  /**
   * Определяет, может ли пользователь редактировать значение.
   *
   * @return boolean - допустимость редактирования значения
   */
  boolean isEditable();

  /**
   * Меняет допустимость редактирования значения в виджете.
   *
   * @param aEditable boolean - допустимость редактирования значения
   */
  void setEditable( boolean aEditable );

  /**
   * Возвращает допустимые пределы изменения редактируемого значения.
   * <p>
   * Контроль гарантирует, что ни программно, и не с помощю GUI нельзя задать интервал, выходящий за эти пределы.
   *
   * @return {@link Secint} - интервал внутри которого всегда находится значение {@link #getValue()}
   */
  Secint getLimits();

  /**
   * Задает новые пределы редактируемого значения.
   *
   * @param aMinStart int - минимальное допустимое значение редактируемого {@link Secint#start()}
   * @param aMaxEnd int - максимаьлное допустимое значение редактируемого {@link Secint#end()}
   * @throws TsValidationFailedRtException не прошла проверка {@link Secint#checkCanCreate(int, int)}
   */
  void setLimits( int aMinStart, int aMaxEnd );

  /**
   * Возвращает значение в редакторе.
   *
   * @return {@link Secint} - значение рамках пределов {@link #getLimits()}
   */
  Secint getValue();

  /**
   * Задает редактируемое значение.
   *
   * @param aValue {@link Secint} - новое значение
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsValidationFailedRtException не прошла проверка {@link Secint#checkCanCreate(int, int)}
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #getLimits()}
   */
  void setValue( Secint aValue );

  /**
   * Задает редактируемое значение.
   *
   * @param aStart int - начальная (включительно) секунда интервала (должно быть >= 0)
   * @param aEnd int - конечная (включительно) секунда интервала (должно быть >= aStart)
   * @throws TsValidationFailedRtException не прошла проверка {@link Secint#checkCanCreate(int, int)}
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #getLimits()}
   */
  void setValue( int aStart, int aEnd );

}
