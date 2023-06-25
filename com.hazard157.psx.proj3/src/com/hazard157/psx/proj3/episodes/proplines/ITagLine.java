package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tsgui.utils.checkstate.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Теглайн - редактируемый набор пометок эпизода ярлыками.
 *
 * @author hazard157
 */
public interface ITagLine
    extends IAnyPropLineBase {

  /**
   * Возвращает список идентификаторов ярлыков, которыее присутствуют в пометках.
   *
   * @return {@link IStringList} - список идентификаторов используемых ярлыков
   */
  IStringList usedTagIds();

  /**
   * Возвращает пометку эпизода запрошенным ярлыком.
   * <p>
   * Пометка реализована в виде набора интервалов, внутри которых эпизод помечен запрошенным ярлыком. Для удобства
   * работы, вместо списка интервалов возвращается карта, ключами в котором служит секунды начала интервала
   * отсортированные по возрастанию. Значениями являются сами интервалы пометки. Предполагается, что интервалы на
   * пересекаются и не соприкасаются (в таком случае, их нужно было объединить).
   * <p>
   * Для неизвестных идентификаторов возвращает пустую карту.
   *
   * @param aTagId String - идентификатор запрошенного ярлыка
   * @return {@link IIntMap}&lt;{@link Secint}&gt; - карта "начало интервала" - "сам интервал пометки"
   * @throws TsNullArgumentRtException аргумент = null
   */
  IIntMap<Secint> marks( String aTagId );

  /**
   * Возвращает список пометок ярлыком.
   *
   * @param aTagId String - идентификатор ярлыка
   * @return {@link ISecintsList} - список интервалов, помеченных ярдыком
   * @throws TsNullArgumentRtException аргумент = null
   */
  ISecintsList calcMarks( String aTagId );

  /**
   * Возвращает список пометок любым из перечисленных идентификаторов.
   *
   * @param aTags {@link IStringList} - список запрашиваемых идентификаторов
   * @return {@link ISecintsList} - список интервалов, помеченных хотя бы одним запрошенным идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  ISecintsList calcMarks( IStringList aTags );

  /**
   * Возвращает список идентификаторов ярлыков, которыми помечена запрошеная секунда эпизода.
   *
   * @param aSec int - запрошенная секунда
   * @return {@link IStringList} - список идентификаторов ярлыков, которыми помечена запрошеная секунда эпизода
   * @throws TsIllegalArgumentRtException aSec < 0
   */
  IStringList tagIdsAt( int aSec );

  /**
   * Возвращает список идентификаторов ярлыков, которыми помечена хотя бы одна секнуда запрошеного интервала.
   *
   * @param aIn {@link Secint} - запрошенный интервал
   * @return {@link IStringList} - список идентификаторов ярлыков
   * @throws TsNullArgumentRtException аргумент = null
   */
  IStringList tagIdsIn( Secint aIn );

  /**
   * Определяет помекти ярлыками интервала.
   * <p>
   * Возвращает карту "ИД-ярлыка" - "степень пометки". Степень пометки может быть либо {@link ECheckState#CHECKED}
   * (когда ярлыком помечень весь интервал) или {@link ECheckState#GRAYED}, если интервалом помечена только часть
   * интервала. Степень пометки {@link ECheckState#UNCHECKED} не используется - ярлыки, которыми не помечен интервал не
   * попадают в карту.
   *
   * @param aIn {@link Secint} - запрошенный интервал
   * @return IStringMap&lt;{@link ECheckState}&gt; - карту "ИД-ярлыка" - "степень пометки"
   * @throws TsNullArgumentRtException аргумент = null
   */
  IStringMap<ECheckState> tagMarksIn( Secint aIn );

  /**
   * Находит интервал, который приходится на указанную секунду.
   *
   * @param aTagId String - идентификатор ярлыка
   * @param aSec int - запрошенная секунда
   * @return {@link Secint} - найденный интервал или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aSec < 0
   */
  Secint findMark( String aTagId, int aSec );

  /**
   * Добавляет пометку одним ярлыком.
   * <p>
   * Если добавляемый ярлык пересекается илисоприкасается с другими ярлыками, то это приводит к объединению пометок так,
   * чтобы инетрвалы не пересекались (не соприкасались).
   *
   * @param aTagId String - идентификатор ярлыка
   * @param aSecint {@link Secint} - интервал пометки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aTagId не ИД-путь
   */
  void addMark( String aTagId, Secint aSecint );

  /**
   * Удяляет один интервал пометки, начинающейся с указанной секунды.
   * <p>
   * Если нет запрошенного ярлыка, или нет интервала, начинающегося с указанной секунды, метод ничего не делает.
   *
   * @param aTagId String - идентификатор ярлыка удаляемой пометки
   * @param aStart int - секунда начала удаляемой пометки
   * @return boolean - признак, что пометка была удалена
   * @throws TsNullArgumentRtException aTagId = null
   */
  boolean removeMark( String aTagId, int aStart );

  /**
   * Удяляет пометку в указанном интервале.
   *
   * @param aTagId String - идентификатор ярлыка удаляемой пометки
   * @param aSecint {@link Secint} - ядаляемый интервал
   * @return boolean - признак, что пометка была удалена
   * @throws TsNullArgumentRtException aTagId = null
   */
  boolean removeMark( String aTagId, Secint aSecint );

  /**
   * Удаляет все пометки указанным ярлыком.
   *
   * @param aTagId String - идентификатор ярлыка удаляемых пометок
   * @return boolean - признак, что пометки были удалены
   * @throws TsNullArgumentRtException aTagId = null
   */
  boolean removeAllMarks( String aTagId );

  /**
   * Удаляет все интервалы пометки ярлыком.
   *
   * @param aTagId String - идентификатор ярлыка удаляемой пометки
   * @throws TsNullArgumentRtException аргумент = null
   */
  void clearTag( String aTagId );

}
