package com.hazard157.psx24.explorer.pq;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Набор результатов запроса.
 * <p>
 * Набор используется и как вход для процессора запросов {@link PqQueryProcessor}, так и возвращается результат запроса.
 * Таким образом, можно делать запросы к результатам запроса.
 * <p>
 * Для создания начального набора (сождержащего все интерваы всех эпизодов) используйте
 * {@link #createFull(IUnitEpisodes)}.
 * <p>
 * Это неизменяемый класс.
 *
 * @author goga
 */
public class PqResultSet {

  /**
   * Всегда пустой набор.
   */
  public static final PqResultSet EMPTY = new PqResultSet();

  private final IStringMapEdit<IList<Svin>> epinsMap = new StringMap<>();

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aEpins {@link IList}&lt;{@link Svin}&gt; - список интервалов эпизодов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PqResultSet( IList<Svin> aEpins ) {
    TsNullArgumentRtException.checkNull( aEpins );
    for( Svin epin : aEpins ) {
      IListBasicEdit<Svin> list = (IListBasicEdit<Svin>)epinsMap.findByKey( epin.episodeId() );
      if( list == null ) {
        list = new SortedElemLinkedBundleList<>();
        epinsMap.put( epin.episodeId(), list );
      }
      list.add( epin );
    }
  }

  /**
   * Создает набор, содержащий все интервалы всех эпизодов.
   *
   * @param aEpMan {@link IUnitEpisodes} - менеджер эпизодов
   * @return {@link PqResultSet} - "корневой" набор, откуда пойдет первый запрос
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static PqResultSet createFull( IUnitEpisodes aEpMan ) {
    IListEdit<Svin> list = new ElemLinkedBundleList<>();
    for( IEpisode e : aEpMan.items() ) {
      Svin svin = new Svin( e.id(), IStridable.NONE_ID, new Secint( 0, e.info().duration() - 1 ) );
      list.add( svin );
    }
    return new PqResultSet( list );
  }

  private PqResultSet() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, содержится ли что-нибудь в результате.
   * <p>
   * На данный момент равнозначно проверке на пустоту списка {@link #epinsMap()}. Сделан с учтотом возможности
   * добавления полей к класс.
   *
   * @return boolean - признак непустого сожержимого
   */
  public boolean isEmpty() {
    return epinsMap.isEmpty();
  }

  /**
   * Возвращает всю выборку в виде карты "ИД эпизода" - "список интервалов эпизода".
   * <p>
   * Списки в карте сортированы по правилам {@link Svin#compareTo(Svin)}.
   *
   * @return IStringMap&lt;IList&lt;{@link Svin}&gt;&gt; - карта "ИД эпизода" - "список интервалов эпизода"
   */
  public IStringMap<IList<Svin>> epinsMap() {
    return epinsMap;
  }

  /**
   * Возвращает все интервалы в виде одного сортированного списка.
   *
   * @return IList&lt;{@link Svin}&gt; - список всех интервалов в результате
   */
  public IList<Svin> listAllSvins() {
    IListBasicEdit<Svin> result = new SortedElemLinkedBundleList<>();
    for( IList<Svin> ll : epinsMap.values() ) {
      result.addAll( ll );
    }
    return result;
  }

}
