package com.hazard157.psx.proj3.excl_done.beq.impl;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.excl_done.beq.*;

/**
 * Реализация {@link IBeqResult}.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public class BeqResult
    implements IBeqResult {

  /**
   * Всегда пустой набор.
   */
  public static final BeqResult EMPTY = new BeqResult();

  private final IStringMapEdit<IListBasicEdit<Svin>> epinsMap = new StringMap<>();

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aSvins {@link IList}&lt;{@link Svin}&gt; - список интервалов эпизодов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public BeqResult( IList<Svin> aSvins ) {
    TsNullArgumentRtException.checkNull( aSvins );
    for( Svin epin : aSvins ) {
      IListBasicEdit<Svin> list = epinsMap.findByKey( epin.episodeId() );
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
   * @return {@link BeqResult} - "корневой" набор, откуда пойдет первый запрос
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static BeqResult createFull( IUnitEpisodes aEpMan ) {
    IListEdit<Svin> list = new ElemLinkedBundleList<>();
    for( IEpisode e : aEpMan.items() ) {
      list.add( new Svin( e.id(), new Secint( 0, e.info().duration() - 1 ) ) );
    }
    return new BeqResult( list );
  }

  private BeqResult() {
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
  @Override
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
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStringMap<IList<Svin>> epinsMap() {
    return (IStringMap)epinsMap;
  }

  /**
   * Возвращает все интервалы в виде одного сортированного списка.
   *
   * @return IList&lt;{@link Svin}&gt; - список всех интервалов в результате
   */
  @Override
  public IList<Svin> listAllSvins() {
    IListBasicEdit<Svin> result = new SortedElemLinkedBundleList<>();
    for( IList<Svin> ll : epinsMap.values() ) {
      result.addAll( ll );
    }
    return result;
  }

}
