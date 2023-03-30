package com.hazard157.lib.core.quants.secint;

import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Сортированный список непересекающихся интервалов.
 *
 * @author hazard157
 */
public interface ISecintsList
    extends IList<Secint> {

  /**
   * Возвращает содержимое списка.
   *
   * @return {@link IList}&lt;{@link Secint}&gt; - сортированный список интервалов
   */
  IList<Secint> items();

  /**
   * Возвращает список пропусков на заданном интервале aIn.
   *
   * @param aIn {@link Secint} - искомый интервал
   * @return IList&lt;{@link Secint}&gt; - список пропусков
   * @throws TsNullArgumentRtException аргумент = null
   */
  IList<Secint> listGaps( Secint aIn );

  /**
   * Retuns change eventer.
   *
   * @return {@link IGenericChangeEventer} - change eventer
   */
  IGenericChangeEventer eventer();

}
