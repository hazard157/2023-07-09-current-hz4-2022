package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;

/**
 * Линия пометки видеорядя однотипными маркерами.
 * <p>
 * Линия имеет виртуальную длительность до максимально допустимой {@link Secint#MAXIMUM}. На протяжении линии каждая
 * секунда отмечена одной, и только одной сущностью-меткой. Если клиентом явно не была задана пометка, то видеоряд
 * считается помеченным меткой-заполнителем {@link #fillerMarker()}. Другими словами, на не заданных пользователем
 * интервалах автоматически создаются пометки маркером-заполнителем.
 *
 * @author hazard157
 * @param <E> - конкретный тип маркера
 * @param <M> - конкретный наследник {@link Mark}
 */
public interface IMarkLine<E, M extends Mark<E>>
    extends IAnyPropLineBase {

  /**
   * Возвращает маркер-заполнитель.
   *
   * @return &lt;E&gt; - маркер-заполнитель, не бывает null
   */
  E fillerMarker();

  /**
   * Возвращает сортированную по возрастанию времени карту маркеров.
   *
   * @return IIntMap&lt;E&gt; - карта "время маркера" - "маркер"
   */
  IIntMap<E> markersMap();

  /**
   * Возвращает все пометки на линии.
   * <p>
   * Список не бывает пустым. Последняя пометка в списке всегда длится до длительности лиинии
   * {@link HmsUtils#MAX_MMSS_VALUE}.
   * <p>
   * Внимание: экземпляр списка содается каждый раз, при вызове метода.
   *
   * @return IList&lt;M;&gt; - упорядоченный по времени список пометок
   */
  IList<M> marksList();

  /**
   * Добавляет маркер с указанной секунды и до следующего маркера.
   * <p>
   * Существующий маркер заменяется.
   *
   * @param aStartSec int - секунда начала пометки
   * @param aMark &lt;E&gt; - маркер
   * @return &lt;M&gt; - созданная пометка
   * @throws TsNullArgumentRtException aMark = null
   * @throws TsIllegalArgumentRtException aStartSec < 0
   */
  M addMarkAt( int aStartSec, E aMark );

  /**
   * Добавляет маркер на интервале.
   * <p>
   * После окончания заданного интервала будет действовать "обрезанная" пометка, существующая до новой. Все пометки,
   * попадающие внутри интервала, удаляются.
   *
   * @param aIn {@link Secint} - интервал пометки
   * @param aMark &lt;E&gt; - маркер
   * @return &lt;M&gt; - созданная пометка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  M addMarkIn( Secint aIn, E aMark );

  /**
   * Удаляет пометку по секунде маркера.
   *
   * @param aMarkerSec int - секунда маркера
   * @return &lt;M&gt; - удаленная пометка
   * @throws TsItemNotFoundRtException за указанную секунду нет маркера
   */
  M removeByMarkerSec( int aMarkerSec );

  /**
   * Удаляет пометку по индексу в упорядоченной карте {@link #markersMap()}.
   * <p>
   * Пометка просто удаляется из списка - то есть, весь интервал пометки заполяется "удлинением" предыдущей пометки.
   * Если удаляется первая пометка (начиная с нулевой секунды), то на его месте автоматически создается пометка
   * маркером-заполнителем. В любом случае, если предыдущая и следующая пометка содержат один и тот же маркер, то эти
   * две пометки объединяются в одну.
   *
   * @param aIndex int - индекс в списке {@link #markersMap()}.{@link IIntMap#keys() keys()}
   * @return &lt;M&gt; - удаленная пометка
   * @throws TsIllegalArgumentRtException индекс выходит зха допустимые пределы
   */
  M removeByListIndex( int aIndex );

  /**
   * Возвращает пометку, которая приходится на запрошенную секунду.
   *
   * @param aSec int - запрошенная секунда
   * @return &lt;M&gt; - помекта в эту секунду
   * @throws TsNullArgumentRtException aSec < 0
   */
  M getMarkAt( int aSec );

  /**
   * Возвращает пометки в заданном интервале.
   * <p>
   * Возвращаются все пометки, которые пересекаются сзапрошенным интервалом.
   *
   * @param aIn {@link Secint} - заданный интервал
   * @return IList&lt;M&gt; - упорядоченный по времени список пометок
   */
  IList<M> getMarksIn( Secint aIn );

  /**
   * Возвращает интервалы пометки запрошенным маркером в запрошенном интервале.
   *
   * @param aMark &lt;E&gt; - запрошенный маркер
   * @param aIn {@link Secint} - запрошенный интервал
   * @return {@link ISecintsList} - список интервалов (внутри интервала-аргумента)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ISecintsList getMarkSecints( E aMark, Secint aIn );

}
