package com.hazard157.psx.proj3.bricks.beq2.filter;

import static com.hazard157.psx.proj3.bricks.beq2.filter.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Вид единицного фильтра выборки интервалов из эпизодов.
 *
 * @author hazard157
 */
public enum EBeqSingleFilterKind
    implements IStridable {

  /**
   * Поиск любого текста в во всей информации об эпизодах.
   */
  ANY_TEXT( BeqSingleFilterAnyText.TYPE_ID, STR_N_ANY_TEXT, STR_D_ANY_TEXT, BeqSingleFilterAnyText.FACTORY ),

  /**
   * Поиск в эпизодах по пометкам ярлыками.
   */
  TAG_IDS( BeqSingleFilterTagIds.TYPE_ID, STR_N_TAG_IDS, STR_D_TAG_IDS, BeqSingleFilterTagIds.FACTORY ),

  /**
   * ыбор эпизодов.
   */
  EPISODE_IDS( BeqSingleFilterEpisodeIds.TYPE_ID, STR_N_EPISODE_IDS, STR_D_EPISODE_IDS,
      BeqSingleFilterEpisodeIds.FACTORY ),

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EBeqSingleFilterKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EBeqSingleFilterKind> KEEPER =
      new StridableEnumKeeper<>( EBeqSingleFilterKind.class );

  private static IStridablesListEdit<EBeqSingleFilterKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  private final ITsSingleFilterFactory<SecondSlice> factory;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   * @param aFactory {@link ITsSingleFilterFactory}&lt;{@link SecondSlice}&gt; - фабрика фильтра
   */
  EBeqSingleFilterKind( String aId, String aName, String aDescription, ITsSingleFilterFactory<SecondSlice> aFactory ) {
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
    EBeqSingleFilterKind kind = asList().getByKey( aParams.typeId() );
    switch( kind ) {
      case EPISODE_IDS:
        return BeqSingleFilterEpisodeIds.makeHumanReadableString( aParams );
      case ANY_TEXT:
        return BeqSingleFilterAnyText.makeHumanReadableString( aParams );
      case TAG_IDS:
        return BeqSingleFilterTagIds.makeHumanReadableString( aParams );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EBeqSingleFilterKind} &gt; - список всех констант
   */
  public static IStridablesList<EBeqSingleFilterKind> asList() {
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
   * @return EBeqSingleFilterKind - найденная константа, или <code>null</code> если нет константы с таимк
   *         идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EBeqSingleFilterKind findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EBeqSingleFilterKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EBeqSingleFilterKind getById( String aId ) {
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
   * @return EBeqSingleFilterKind - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EBeqSingleFilterKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EBeqSingleFilterKind item : values() ) {
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
   * @return EBeqSingleFilterKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EBeqSingleFilterKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
