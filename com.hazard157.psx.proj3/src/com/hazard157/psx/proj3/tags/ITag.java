package com.hazard157.psx.proj3.tags;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.categs.*;

/**
 * Tag to mark episode intervals.
 * <p>
 * Th ID {@link #id()} is an IDpath making natural tree of tags. The name {@link #nmName()} is last IDname component of
 * the ID {@link #id()}.
 *
 * @author hazard157
 */
public interface ITag
    extends ICategory<ITag> {

  @Override
  IOptionSet params();

  @Override
  ITag parent();

  /**
   * Sets parameters values.
   *
   * @param aInfo {@link IOptionSet} - the parameters.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setParams( IOptionSet aInfo );

  /**
   * Изменяет имя (последнюю компоненту идентификатора {@link #id()}) ярлыка.
   * <p>
   * Внимание: успешное изменение имени приводит к пересозданию всего дерева дочерных ярлыков - ведь у них меняются
   * идентификаторы.
   *
   * @param aName String - новое имя (ИД-имя) ярлыка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент не ИД-путь
   * @throws TsItemAlreadyExistsRtException ярлык с таким именем уже есть у родителя
   */
  // void changeName( String aName );

  /**
   * Возвращает имя узла ярлыка.
   * <p>
   * Имя язла ярлыка - это последняя компонента идентификатора {@link #id()}.
   *
   * @return String - имя узла ярлыка
   */
  String nodeName();

  /**
   * Возвращает список дочерных узлов.
   *
   * @return IStridablesList&lt;{@link ITag}&gt; - список дочерных узлов
   */
  IStridablesList<ITag> childNodes();

  /**
   * Возвращает все узлы, находящейся в дереве под этим корнем.
   * <p>
   * Не включает этот узел, только поддерево под ним.
   *
   * @return IStridablesList&lt;{@link ITag}&gt; - список всех узлов этого корня
   */
  IStridablesList<ITag> allNodesBelow();

  /**
   * Возвращает все листья, находящейся в дереве под этим корнем.
   *
   * @return IStridablesList&lt;{@link ITag}&gt; - список всех листьев этого корня
   */
  IStridablesList<ITag> allLeafsBelow();

  /**
   * Возвращает все группы, находящейся в дереве под этим корнем.
   *
   * @return IStridablesList&lt;{@link ITag}&gt; - список всех групп этого корня
   */
  IStridablesList<ITag> allGroupsBelow();

  /**
   * Находит дочерный узел по имени {@link ITag#nodeName()};
   *
   * @param aNodeName String - имя (ИД-имя) узла
   * @return {@link ITag} - найденный узел или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  ITag findByName( String aNodeName );

  /**
   * Добавляет корневой узел.
   *
   * @param aNodeName String - имя (ИД-имя) дочерного узла
   * @param aInfo {@link IOptionSet} - информация об узле
   * @return {@link ITag} - созданный узел
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-имя
   * @throws TsItemAlreadyExistsRtException уже есть дочерный узел с таким идентификатором
   */
  ITag addNode( String aNodeName, IOptionSet aInfo );

  /**
   * Добавляет корневой узел.
   *
   * @param aNodeName String - имя (ИД-имя) дочерного узла
   * @param aIdsAndValues Object[] - identifier / value pairs for {@link #params()}
   * @return {@link ITag} - созданный узел
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-имя
   * @throws TsItemAlreadyExistsRtException уже есть дочерный узел с таким идентификатором
   * @throws TsIllegalArgumentRtException количество аргументов aNamesAndValues нечетное
   * @throws ClassCastException нарушено соглашение по аргументам
   */
  ITag addNode( String aNodeName, Object... aIdsAndValues );

  /**
   * Удаляет корневой узел со всеми подузлами.
   *
   * @param aNodeId String - полный идентификатор удаляемого узла
   * @return {@link ITag} - удаленный узел или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  ITag removeNode( String aNodeId );

  /**
   * Синтаксически создает полный идентификатор узла из имени узла и идентификатора {@link #id()}.
   *
   * @param aNodeName String - имя (ИД-имя) узла
   * @return String - созданный полный идентификатор (ИД-путь) узла
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргуменет не ИД-имя
   */
  String formatNodeId( String aNodeName );

  /**
   * Определяет, является ли узел корневым.
   *
   * @return boolean - признак корневого узла
   */
  @Override
  default boolean isRoot() {
    return id().isEmpty();
  }

}
