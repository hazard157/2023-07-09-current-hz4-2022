package com.hazard157.psx.proj3.tags;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Менеджер управления и хранения ярлыков.
 *
 * @author hazard157
 */
public interface IUnitTags
    extends IProjDataUnit {

  // FIXME заменить master/initial tags tagOffer-ами для корневого ярлыка
  // FIXME может сделать НЕСКОЛЬКО наборов tagOffer для разных нужд ? напрмер, эизоды и плановые_эпизоды

  /**
   * Возвращает корневой (сам неиспользуемый) ярлык.
   *
   * @return {@link IRootTag} - корневой ярлык
   */
  IRootTag root();

  /**
   * Возвращает идентификаторы корневых групп для пометки эпизодов.
   *
   * @return {@link IStringList} - идентификаторы корневых групп для пометки эпизодов
   */
  IStringList getInitialRootsIds();

  /**
   * Задает идентификаторы корневых групп для пометки эпизодов.
   *
   * @param aRootIds {@link IStringList} - идентификаторы корневых групп для пометки эпизодов
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент содержит узел, не являющейся одной из корневых групп
   */
  void setInitialRootsIds( IStringList aRootIds );

  /**
   * Возвращает список ярлыков, для которых заданы предлагаемые группы.
   *
   * @return {@link IStringList} - список ярлыков, для которых заданы предлагаемые группы
   */
  IStringList getMasterTagIds();

  /**
   * Возвращает идентификаторы групп, предлагаемых указанным ярлыком.
   * <p>
   * Если ярлык ничего не предлагает, то метод возвращает пустой лист.
   *
   * @param aMasterId String - идентификатор ялыка
   * @return {@link IStringList} - идентификаторы групп, разрешаемых к включению этим ярлыком
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException нет ярлыка (не группы!) с таким идентификатором
   */
  IStringList getTagOfferIds( String aMasterId );

  /**
   * Задает идентификаторы групп, предлагаемых указанным ярлыком.
   * <p>
   * Для удаления предложения следует задать aOfferIds = null или пустой список.
   *
   * @param aMasterId String - идентификатор ялыка
   * @param aOfferIds {@link IStringList} - идентификаторы групп, разрешаемых к включению этим ярлыком или null
   * @throws TsNullArgumentRtException aMasterId = null
   * @throws TsItemNotFoundRtException нет ярлыка (не группы!) с таким идентификатором
   * @throws TsIllegalArgumentRtException в списке есть идентификаторы, которым не соответствует группа
   */
  void setTagOfferIds( String aMasterId, IStringList aOfferIds );

  /**
   * Возвращает все корневые группы и группы, разрешенные к использованию при указанном наборе ярлыков.
   * <p>
   * Аргумент может содержать идентификатор как групп, так и листьев дерева ярлыков. В возвращаемом списке только группы
   * ярлыков (то есть, узлы, имеющие дочерные узлы).
   *
   * @param aUsedTagIds {@link IStringList} - перечень ярлыков пометки
   * @return IStridablesList&lt;{@link ITag}&gt; - группы, разрешенные к использованию
   */
  IStridablesList<ITag> getEnabledTagGroups( IStringList aUsedTagIds );

  /**
   * Возвращает ярлыки по идентификаторам.
   *
   * @param aTagIds {@link IStringList} - идентификаторы запрашиваемых ярлыков
   * @return IStridablesList&lt;{@link ITag}&gt; - запрошенные ярлыки
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException а аргументе есть идентификаторы несуществующих ярлыков
   */
  IStridablesList<ITag> getTagsByIds( IStringList aTagIds );

}
