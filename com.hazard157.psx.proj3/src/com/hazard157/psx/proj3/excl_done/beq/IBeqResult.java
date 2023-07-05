package com.hazard157.psx.proj3.excl_done.beq;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.excl_done.beq.impl.*;

/**
 * Набор результатов запроса.
 * <p>
 * Набор используется и как вход для процессора запросов {@link IBeqProcessor}, так и возвращается результат запроса.
 * Таким образом, можно делать запросы к результатам запроса.
 * <p>
 * Для создания начального набора (сождержащего все интерваы всех эпизодов) используйте
 * {@link BeqUtils#createFull(IUnitEpisodes)}.
 *
 * @author hazard157
 */
public interface IBeqResult {

  /**
   * Всегда пустой набор.
   */
  IBeqResult EMPTY = new InternalEmptyBeqResult();

  /**
   * Определяет, содержится ли что-нибудь в результате.
   * <p>
   * На данный момент равнозначно проверке на пустоту списка {@link #epinsMap()}. Сделан с учтотом возможности
   * добавления полей к класс.
   *
   * @return boolean - признак непустого сожержимого
   */
  boolean isEmpty();

  /**
   * Возвращает всю выборку в виде карты "ИД эпизода" - "список интервалов эпизода".
   * <p>
   * Списки в карте сортированы по правилам {@link Svin#compareTo(Svin)}.
   *
   * @return IStringMap&lt;IList&lt;{@link Svin}&gt;&gt; - карта "ИД эпизода" - "список интервалов эпизода"
   */
  IStringMap<IList<Svin>> epinsMap();

  /**
   * Возвращает все интервалы в виде одного сортированного списка.
   *
   * @return IList&lt;{@link Svin}&gt; - список всех интервалов в результате
   */
  IList<Svin> listAllSvins();

}

class InternalEmptyBeqResult
    implements IBeqResult {

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public IStringMap<IList<Svin>> epinsMap() {
    return IStringMap.EMPTY;
  }

  @Override
  public IList<Svin> listAllSvins() {
    return IList.EMPTY;
  }

}
