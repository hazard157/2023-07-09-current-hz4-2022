package com.hazard157.lib.core.quants.secint;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Расширение интерфейса {@link ISecintsList} методами редатктирования.
 *
 * @author hazard157
 */
public interface ISecintsListEdit
    extends ISecintsList {

  /**
   * Добавляет интервал в список, при пересечении или соприкосновении с существующими элементами, объединяет их.
   *
   * @param aIn {@link Secint} - добавляемый интервал
   * @return boolean - признак, что список был изменен
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean add( Secint aIn );

  /**
   * Удаляет интервал из списка, при пересечении или соприкосновении с существующими элементами, обрезает их.
   *
   * @param aIn {@link Secint} - удаляемый интервал
   * @return boolean - признак, что список был изменен
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean remove( Secint aIn );

  /**
   * Добавляет список интервалов.
   *
   * @param aList {@link ITsCollection}&lt;{@link Secint}&gt; - добавляемый список
   * @return boolean - признак, что список был изменен
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean add( ITsCollection<Secint> aList );

  /**
   * Очищает список интервалов.
   */
  void clear();

}
