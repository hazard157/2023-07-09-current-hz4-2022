package com.hazard157.psx.proj3.pleps;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.common.quants.secint.*;

/**
 * The planned episode..
 *
 * @author hazard157
 */
public interface IPlep
    extends ISinentity<PlepInfo> {

  /**
   * Возвращает список элементов (движений) из которых состоит планируемый эпизод.
   *
   * @return {@link IList}&lt;{@link IStir}&gt; - упорядоченный список движений планируемого эпизода
   */
  IList<IStir> stirs();

  /**
   * Создает новый элемент в {@link #stirs()}.
   * <p>
   * Индекс может быть от -1 до размера списка {@link #stirs()}. Значение -1 и размера списка {@link #stirs()}.size()
   * приводят к добавлению элемента в конец списка.
   *
   * @param aIndex int - индекс добавляемого элемента (или -1 для добавления в конец)
   * @param aParams {@link IOptionSet} - начальные значения {@link IStir#params()}
   * @return {@link IStir} - созданный элемент
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  IStir newStir( int aIndex, IOptionSet aParams );

  /**
   * Удаляет запрошенный элемент из {@link #stirs()}.
   *
   * @param aIndex int - инжекс удаляемого элемента
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  void removeStir( int aIndex );

  /**
   * Возвращает средство управления порядком движений в списке {@link #stirs()}.
   *
   * @return {@link IListReorderer}&lt;{@link IStir}&gt; - средство управления порядком элементов в {@link #stirs()}
   */
  IListReorderer<IStir> stirsReorderer();

  /**
   * Возвращает звуковую дорожку.
   *
   * @return {@link INotifierListEdit}&lt;{@link ITrack}&gt; - треки, составляющие звуковую дорожку
   */
  INotifierListEdit<ITrack> tracks();

  /**
   * Создает новый элемент в {@link #tracks()}.
   * <p>
   * Индекс может быть от -1 до размера списка {@link #tracks()}. Значение -1 и размера списка {@link #tracks()}.size()
   * приводят к добавлению элемента в конец списка.
   *
   * @param aIndex int - индекс добавляемого элемента (или -1 для добавления в конец)
   * @param aSongId String - идентификатор песни
   * @param aInterval {@link Secint} - интервал в песне
   * @return {@link ITrack} - созданный трек
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентифткатор не ИД-путь
   */
  ITrack newTrack( int aIndex, String aSongId, Secint aInterval );

  /**
   * Удаляет запрошенный элемент из {@link #tracks()}.
   *
   * @param aIndex int - инжекс удаляемого элемента
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  void removeTrack( int aIndex );

  /**
   * Возвращает средство управления порядком треков в списке {@link #tracks()}.
   *
   * @return {@link IListReorderer}&lt;{@link ITrack}&gt; - средство управления порядком треков в {@link #tracks()}
   */
  IListReorderer<ITrack> tracksReorderer();

  /**
   * Returns list of step for PLEP preparation.
   *
   * @return {@link IStringList} - an editable list of preparation steps
   */
  INotifierStringListEdit preparationSteps();

  /**
   * Returns the reorderer for {@link #preparationSteps()}.
   *
   * @return {@link IList}&lt;{@link String}&gt; - the reorderer
   */
  IListReorderer<String> preparationStepsReorderer();

  /**
   * Вычисляет плановую длительность.
   * <p>
   * Плановая длитеность определяется как максимальная из двух суммарных длитеностий: движений {@link #stirs()} и
   * саундтрека {@link #tracks()}.
   *
   * @return int - плановая длительность в секундах
   */
  int computeDuration();

  /**
   * Возвращает родительский менеджер планов.
   *
   * @return {@link IUnitPleps} - менеджер планов
   */
  IUnitPleps unit();

}
