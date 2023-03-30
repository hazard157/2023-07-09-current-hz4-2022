package com.hazard157.lib.core.quants.stars5;

import static com.hazard157.lib.core.quants.stars5.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * Возможные варианты качественной оценки (рейтинг).
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public enum EStarsFive
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, STR_D_UNKNOWN, STR_N_UNKNOWN, 0 ),

  STARS1( "Stars_1", STR_D_STARS_1, STR_N_STARS_1, 1 ),

  STARS2( "Stars_2", STR_D_STARS_2, STR_N_STARS_2, 2 ),

  STARS3( "Stars_3", STR_D_STARS_3, STR_N_STARS_3, 3 ),

  STARS4( "Stars_4", STR_D_STARS_4, STR_N_STARS_4, 4 ),

  STARS5( "Stars_5", STR_D_STARS_5, STR_N_STARS_5, 5 ),

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EStarsFive";

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EStarsFive> KEEPER = new StridableEnumKeeper<>( EStarsFive.class );

  private static IList<EStarsFive> list = null;

  private final String id;
  private final String description;
  private final String name;
  private final int    weight;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название константы
   * @param aWeight int - целочисленная оценка качества
   */
  EStarsFive( String aId, String aDescr, String aName, int aWeight ) {
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
   * Возвращает следующую (более высокую) оценку, вплоть до {@link EStarsFive#TOP}.
   *
   * @return {@link EStarsFive} - следующая оценка
   */
  public EStarsFive next() {
    int index = 1 + list().indexOf( this );
    if( index >= list().size() ) {
      return list().last();
    }
    return list().get( index );
  }

  /**
   * Возвращает предыдущую (более низкую) оценку, вплоть до {@link EStarsFive#UNKNOWN}.
   *
   * @return {@link EStarsFive} - предыдущая оценка
   */
  public EStarsFive prev() {
    int index = list().indexOf( this ) - 1;
    if( index <= 0 ) {
      return list().first();
    }
    return list().get( index );
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
   * @return EStarsFive - найденная константа, или null если нет константы с таимк идентификатором
   */
  public static EStarsFive findByWeight( int aWeight ) {
    for( EStarsFive item : values() ) {
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
   * @return EStarsFive - найденная константа
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EStarsFive getByWeight( int aWeight ) {
    return TsItemNotFoundRtException.checkNull( findByWeight( aWeight ) );
  }

  /**
   * Возвращает список всех константв порядке их обявления.
   *
   * @return {@link IList}&lt;{@link EStarsFive}&gt; - список всех константв порядке их обявления
   */
  public static IList<EStarsFive> list() {
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
   * @return EStarsFive - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EStarsFive findById( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EStarsFive item : values() ) {
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
   * @return EStarsFive - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EStarsFive getById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findById( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return EStarsFive - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EStarsFive findByDescription( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EStarsFive item : values() ) {
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
   * @return EStarsFive - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EStarsFive getByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescription( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return EStarsFive - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EStarsFive findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EStarsFive item : values() ) {
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
   * @return EStarsFive - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EStarsFive getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
