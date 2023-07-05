package com.hazard157.psx.proj3.excl_done.beq;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.excl_done.beq.filters.*;

/**
 * Выборка - последовательность применения фильтров {@link IBeqFilter}.
 * <p>
 * {@link IBeqFilter} состоит из одиночных фильтров (по одному каждого из типов {@link EBeqSingleFilterKind}), которые
 * собраны по AND. Таким образом, получается, что вместо одного {@link ITsCombiFilterParams}, которым фактически
 * является {@link IBeqInquiry}, используется разбиение: список {@link IBeqFilter}, который сам является
 * {@link ITsCombiFilterParams}.
 * <p>
 * Смысл и мотивация такого объединения (вместо того, чтобы иметь один {@link ITsCombiFilterParams}) в следующем:
 * <ul>
 * <li>нет инструмента нормального GUI-редактирования фильтра {@link ITsCombiFilterParams}, и в данный момент
 * (09-11-2019), даже не понятно как и зачем его делать;</li>
 * <li>главный мотив в том, что такое построение соответствует интйитивному способу работы с выборками - сначала
 * применяетя один фильтр {@link IBeqFilter} (например, отбор по одному ярлкку и соответсвющему тексту), смотрится
 * реузальтат, на котором применятся другой фильтр, смотрится результат и т.п. То есть, естественно для человека
 * уточнять выборки, от общего к более частным.</li>
 * </ul>
 * <p>
 * Фактически, объединяет {@link IBeqFilter} по AND.
 *
 * @author hazard157
 */
public interface IBeqInquiry
    extends IGenericChangeEventCapable {

  /**
   * Возвращает список фильтров в прядке их применения.
   *
   * @return {@link IList}&lt;{@link IBeqFilter}&gt; - спрсок фильтров
   */
  IList<IBeqFilter> items();

  /**
   * Создает параметры комбинированного фильта из параметров {@link #items()}.
   *
   * @return {@link ITsCombiFilterParams} - параметры комби-фильтра
   */
  ITsCombiFilterParams makeFilterParams();

}
