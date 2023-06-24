package com.hazard157.lib.core.excl_done.rating;

import static com.hazard157.lib.core.excl_done.rating.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_done.*;
import com.hazard157.lib.core.utils.*;

/**
 * Возможные варианты качественной оценки (рейтинг).
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public enum ERating
    implements IRadioPropEnum {

  // первым (по определению ISelectorPropEnum) должен быть UNKNOWN

  UNKNOWN( UNKNOWN_ID, STR_D_RATING_UNKNOWN, STR_N_RATING_UNKNOWN, -1 ),

  // константы должны быть перечислены по мере возрастания оценки

  BAD( "Bad", STR_D_RATING_BAD, STR_N_RATING_BAD, 0 ),

  NORMAL( "Normal", STR_D_RATING_NORMAL, STR_N_RATING_NORMAL, 1 ),

  GOOD( "Good", STR_D_RATING_GOOD, STR_N_RATING_GOOD, 2 ),

  TOP( "Top", STR_D_RATING_TOP, STR_N_RATING_TOP, 3 ),

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ERating";

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ERating> KEEPER = new StridableEnumKeeper<>( ERating.class );

  private static IList<ERating> list = null;

  private final String id;
  private final String description;
  private final String name;
  private final int    weight;

  static final String[] GAUGE_STRINGS = { //
      "-", //
      "*", //
      "**", //
      "***", //
      "****" //
  };

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название константы
   * @param aWeight int - целочисленная оценка качества
   */
  ERating( String aId, String aDescr, String aName, int aWeight ) {
    id = aId;
    description = aDescr;
    name = aName;
    weight = aWeight;
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

  // ------------------------------------------------------------------------------------
  // дополнительное API
  //

  /**
   * Возвращает "вес", целочисленную оценку рейтинга.
   *
   * @return int - "вес", целочисленная оценка рейтинга
   */
  public int weight() {
    return weight;
  }

  /**
   * Возвращает строковое представление в виде звёздочек.
   *
   * @return String - строковое представление в виде звёздочек
   */
  public String gaugeString() {
    return GAUGE_STRINGS[ordinal()];
  }

  /**
   * Возвращает следующую (более высокую) оценку, вплоть до {@link ERating#TOP}.
   *
   * @return {@link ERating} - следующая оценка
   */
  public ERating next() {
    int index = 1 + asList().indexOf( this );
    if( index >= asList().size() ) {
      return asList().last();
    }
    return asList().get( index );
  }

  /**
   * Возвращает предыдущую (более низкую) оценку, вплоть до {@link ERating#UNKNOWN}.
   *
   * @return {@link ERating} - предыдущая оценка
   */
  public ERating prev() {
    int index = asList().indexOf( this ) - 1;
    if( index <= 0 ) {
      return asList().first();
    }
    return asList().get( index );
  }

  /**
   * Определяет, существует ли константа перечисления с заданным "весом".
   *
   * @param aWeight int - "вес" искомого рейтинга
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным "весом" существует;<br>
   *         <b>false</b> - нет константы с таким "весом".
   */
  public static boolean isItemByWeight( int aWeight ) {
    return findByWeight( aWeight ) != null;
  }

  /**
   * Возвращает константу по "весу" или null.
   *
   * @param aWeight int - "вес" искомого рейтинга
   * @return ERating - найденная константа, или null если нет константы с таимк идентификатором
   */
  public static ERating findByWeight( int aWeight ) {
    for( ERating item : values() ) {
      if( item.weight == aWeight ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по "весу" или выбрасывает исключение.
   *
   * @param aWeight int - "вес" искомого рейтинга
   * @return ERating - найденная константа
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ERating getByWeight( int aWeight ) {
    return TsItemNotFoundRtException.checkNull( findByWeight( aWeight ) );
  }

  /**
   * Возвращает список всех константв порядке их обявления.
   *
   * @return {@link IList}&lt;{@link ERating}&gt; - список всех константв порядке их обявления
   */
  public static IList<ERating> asList() {
    if( list == null ) {
      list = new ElemArrayList<>( values() );
    }
    return list;
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
    return findById( aId ) != null;
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
    return findByDescription( aDescription ) != null;
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
    return findByName( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return ERating - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ERating findById( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ERating item : values() ) {
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
   * @return ERating - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ERating getById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findById( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return ERating - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ERating findByDescription( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( ERating item : values() ) {
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
   * @return ERating - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ERating getByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescription( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return ERating - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ERating findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ERating item : values() ) {
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
   * @return ERating - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ERating getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
