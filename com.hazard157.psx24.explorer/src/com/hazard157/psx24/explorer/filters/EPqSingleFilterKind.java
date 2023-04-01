package com.hazard157.psx24.explorer.filters;

import static com.hazard157.psx24.explorer.filters.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Вид единицного фильтра выборки из PSX.
 *
 * @author hazard157
 */
public enum EPqSingleFilterKind
    implements IStridable {

  /**
   * Поиск текста.
   */
  ANY_TEXT( PqFilterAnyText.TYPE_ID, STR_N_ANY_TEXT, STR_D_ANY_TEXT, PqFilterAnyText.FACTORY ),

  /**
   * Пометки ярлыками.
   */
  TAG_IDS( PqFilterTagIds.TYPE_ID, STR_N_TAG_IDS, STR_D_TAG_IDS, PqFilterTagIds.FACTORY ),

  /**
   * Отбор эпизодов.
   */
  EPISODE_IDS( PqFilterEpisodeIds.TYPE_ID, STR_N_EPISODE_IDS, STR_D_EPISODE_IDS, PqFilterEpisodeIds.FACTORY ),

  ;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "EPqSingleFilterKind"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EPqSingleFilterKind> KEEPER =
      new StridableEnumKeeper<>( EPqSingleFilterKind.class );

  private static IStridablesListEdit<EPqSingleFilterKind> list = null;

  private final String                              id;
  private final String                              name;
  private final String                              description;
  private final ITsSingleFilterFactory<SecondSlice> factory;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   * @param aFactory {@link ITsSingleFilterFactory}&lt;{@link SecondSlice}&gt; - фабрика фильтра
   */
  EPqSingleFilterKind( String aId, String aName, String aDescription, ITsSingleFilterFactory<SecondSlice> aFactory ) {
    id = aId;
    name = aName;
    description = aDescription;
    factory = aFactory;
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
   * Возаращает фабрику фильтра.
   *
   * @return {@link ITsSingleFilterFactory}&lt;{@link SecondSlice}&gt; - фабрика фильтра
   */
  public ITsSingleFilterFactory<SecondSlice> factory() {
    return factory;
  }

  /**
   * Возвращает удобочитаему строку параметров фильтра.
   *
   * @param aParams {@link ITsSingleFilterParams} - параметры одного из фильтроы
   * @return String - однострочный текст
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException параметры не от известного фильтра
   */
  public static final String makeHumanReadableString( ITsSingleFilterParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    EPqSingleFilterKind kind = asList().getByKey( aParams.typeId() );
    switch( kind ) {
      case ANY_TEXT:
        return PqFilterAnyText.makeHumanReadableString( aParams );
      case TAG_IDS:
        return PqFilterTagIds.makeHumanReadableString( aParams );
      case EPISODE_IDS:
        return PqFilterEpisodeIds.makeHumanReadableString( aParams );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EPqSingleFilterKind} &gt; - список всех констант
   */
  public static IStridablesList<EPqSingleFilterKind> asList() {
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
   * @return EPqSingleFilterKind - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EPqSingleFilterKind findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EPqSingleFilterKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EPqSingleFilterKind getById( String aId ) {
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
   * @return EPqSingleFilterKind - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EPqSingleFilterKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EPqSingleFilterKind item : values() ) {
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
   * @return EPqSingleFilterKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EPqSingleFilterKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
