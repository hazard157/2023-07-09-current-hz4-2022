package com.hazard157.psx.proj3.todos;

import static com.hazard157.psx.proj3.todos.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.wrappers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Приоритет (важность) дела.
 * <p>
 * Константы определены в порядке возрастания приоритета.
 *
 * @author hazard157
 */
public enum EPriority
    implements IStridable {

  /**
   * Низкий приоритет.
   */
  LOW( "Low", STR_D_PRIORITY_LOW, STR_N_PRIORITY_LOW ), //$NON-NLS-1$

  /**
   * Нормальный (по умолчанию) приоритет.
   */
  NORMAL( "Normal", STR_D_PRIORITY_NORMAL, STR_N_PRIORITY_NORMAL ), //$NON-NLS-1$

  /**
   * Высокий приоритет.
   */
  HIGH( "High", STR_D_PRIORITY_HIGH, STR_N_PRIORITY_HIGH ), //$NON-NLS-1$

  ;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "EPriority"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EPriority> KEEPER = new StridableEnumKeeper<>( EPriority.class );

  private IAtomicValue av = null;

  private static IList<EPriority> itemsList = null;
  private final String            id;
  private final String            description;
  private final String            name;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название константы
   */
  EPriority( String aId, String aDescr, String aName ) {
    id = aId;
    description = aDescr;
    name = aName;
  }

  // --------------------------------------------------------------------------
  // IStridable
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

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the {@link EAtomicType#VALOBJ} atomic value of this constant.
   *
   * @return {@link IAtomicValue} - atomic value of this constant
   */
  public IAtomicValue atomicValue() {
    if( av == null ) {
      av = avValobj( this );
    }
    return av;
  }

  /**
   * Возвращает все константы перечисления в виде списка {@link IList}.
   *
   * @return IList&lt;{@link EPriority}&gt; - константы перечисления в виде списка {@link IList}
   */
  public static IList<EPriority> listAll() {
    if( itemsList == null ) {
      itemsList = new ElemArrayWrapper<>( values() );
    }
    return itemsList;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - неет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным описанием.
   *
   * @param aDescription String - описание искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным описанием существует;<br>
   *         <b>false</b> - неет константы с таким описанием.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - неет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return EPriority - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EPriority findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EPriority item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по идентификатору или выбрасывает исключение.
   *
   * @param aId String - идентификатор искомой константы
   * @return EPriority - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EPriority findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return EPriority - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EPriority findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EPriority item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по описанию или выбрасывает исключение.
   *
   * @param aDescription String - описание искомой константы
   * @return EPriority - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EPriority findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return EPriority - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EPriority findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EPriority item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени или выбрасывает исключение.
   *
   * @param aName String - имя искомой константы
   * @return EPriority - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EPriority findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}
