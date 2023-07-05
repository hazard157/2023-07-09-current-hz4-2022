package com.hazard157.psx.proj3.excl_done.beq;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.excl_done.beq.filters.*;

/**
 * Фильтр, точнее, параметры для отбора интервалов эпизода.
 * <p>
 * Представляет собой набор из фильтров {@link EBeqSingleFilterKind}, по одному фильтру каждого типа, объединенных по
 * AND и опционально иневртирует, в зависимости от згачения {@link #isInverted()}.
 *
 * @author hazard157
 */
public interface IBeqFilter {

  /**
   * Возвращает набор применяемых фильтров.
   * <p>
   * Возвращаемый набор всегда содержит все виды фильтров из {@link EBeqSingleFilterKind}, для неиспользуемых фильтров в
   * качестве значения содержится {@link ITsSingleFilterParams#NONE}.
   *
   * @return {@link IMap}&lt;{@link EBeqSingleFilterKind},{@link ITsSingleFilterParams}&gt; - карта "вид"- "параметры"
   */
  IMap<EBeqSingleFilterKind, ITsSingleFilterParams> fpMap();

  /**
   * Determones if filter operation must be inverted (NOT applied).
   *
   * @return boolean - the flag of the NOT unary operation
   */
  boolean isInverted();

  /**
   * Создает параметры комбинированного фильта из параметров единичных фильтров {@link #fpMap()}.
   * <p>
   * При формировании параметров отсутствющие (то есть {@link ITsSingleFilterParams#NONE} не включаются. Если все
   * фильтры отсутствую, то может вернуть {@link ITsCombiFilterParams#NONE}.
   * <p>
   * Если {@link #isInverted()} = <code>true</code>, то у возвращаемого набора тоже
   * {@link ITsCombiFilterParams#isInverted()} = <code>true</code>.
   *
   * @return {@link ITsCombiFilterParams} - параметры комби-фильтра
   */
  ITsCombiFilterParams makeFilterParams();

}
