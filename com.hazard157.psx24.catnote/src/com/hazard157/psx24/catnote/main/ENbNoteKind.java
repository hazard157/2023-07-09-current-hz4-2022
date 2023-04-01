package com.hazard157.psx24.catnote.main;

import static com.hazard157.psx24.catnote.main.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Note kind.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ENbNoteKind
    implements IStridable {

  MISC( "misc", STR_N_NNK_MISC, STR_D_NNK_MISC ), //$NON-NLS-1$

  IDEA( "idea", STR_N_NNK_IDEA, STR_D_NNK_IDEA ), //$NON-NLS-1$

  TODO( "todo", STR_N_NNK_TODO, STR_D_NNK_TODO ), //$NON-NLS-1$

  ;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link ValobjUtils}.
   */
  public static final String KEEPER_ID = "ENbNoteKind"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ENbNoteKind> KEEPER = new StridableEnumKeeper<>( ENbNoteKind.class );

  private static IStridablesListEdit<ENbNoteKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   */
  ENbNoteKind( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link ENbNoteKind} &gt; - список всех констант
   */
  public static IStridablesList<ENbNoteKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - нет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Находит константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ENbNoteKind - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ENbNoteKind findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ENbNoteKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ENbNoteKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - нет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  /**
   * Находит константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ENbNoteKind - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ENbNoteKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ENbNoteKind item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ENbNoteKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ENbNoteKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
