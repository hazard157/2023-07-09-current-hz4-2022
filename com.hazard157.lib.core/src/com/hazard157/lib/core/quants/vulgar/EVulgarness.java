package com.hazard157.lib.core.quants.vulgar;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.quants.vulgar.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * Степень эротичности.
 * <p>
 * Константы объявлены по степени повышения степени эротичности.
 *
 * @author goga
 */
@SuppressWarnings( { "nls", "javadoc" } )
public enum EVulgarness
    implements IRadioPropEnum {

  // первым (по определению IRank) должен быть UNKNOWN

  UNKNOWN( UNKNOWN_ID, STR_D_UNKNOWN, STR_N_UNKNOWN, ICON_VLG_UNKNOWN ),

  // константы должны быть перечислены по мере возрастания оценки

  DECENT( "Decent", STR_D_DECENT, STR_N_DECENT, ICON_VLG_DECENT ),

  EROTICA( "Erotica", STR_D_EROTICA, STR_N_EROTICA, ICON_VLG_EROTICA ),

  PORNO( "Porno", STR_D_PORNO, STR_N_PORNO, ICON_VLG_PORNO ),

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EVulgarness";

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EVulgarness> KEEPER = new StridableEnumKeeper<>( EVulgarness.class );

  private final String id;
  private final String description;
  private final String name;
  private final String iconId;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название константы
   * @param aIconId String - идентификатор значка
   */
  EVulgarness( String aId, String aDescr, String aName, String aIconId ) {
    id = aId;
    description = aDescr;
    name = aName;
    iconId = aIconId;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IRadioPropEnum
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ------------------------------------------------------------------------------------
  // Добавочное API
  //

  /**
   * Определяет, является ли аргумент более эротичным.
   * <p>
   * Если аргумент равен этому объекту, возвращает <code>false</code>.
   *
   * @param aThat {@link EVulgarness} - сравниваемая степень эротичности
   * @return boolean - эротичность аргумента выше этой константы
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean isMoreErotic( EVulgarness aThat ) {
    TsNullArgumentRtException.checkNull( aThat );
    return aThat.ordinal() > this.ordinal();
  }

  /**
   * Определяет, является ли аргумент менее эротичным.
   * <p>
   * Если аргумент равен этому объекту, возвращает <code>false</code>.
   *
   * @param aThat {@link EVulgarness} - сравниваемая степень эротичности
   * @return boolean - эротичность аргумента ниже этой константы
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean isLessErotic( EVulgarness aThat ) {
    TsNullArgumentRtException.checkNull( aThat );
    return aThat.ordinal() < this.ordinal();
  }

  /**
   * Определяет, является ли аргумент более или таким же эротичным.
   * <p>
   * Если аргумент равен этому объекту, возвращает <code>true</code>.
   *
   * @param aThat {@link EVulgarness} - сравниваемая степень эротичности
   * @return boolean - эротичность аргумента выше или равно этой константе
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean isMoreOrEqErotic( EVulgarness aThat ) {
    TsNullArgumentRtException.checkNull( aThat );
    return aThat.ordinal() >= this.ordinal();
  }

  /**
   * Определяет, является ли аргумент менее или таким же эротичным.
   * <p>
   * Если аргумент равен этому объекту, возвращает <code>true</code>.
   *
   * @param aThat {@link EVulgarness} - сравниваемая степень эротичности
   * @return boolean - эротичность аргумента ниже или равно этой константе
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean isLessOrEqErotic( EVulgarness aThat ) {
    TsNullArgumentRtException.checkNull( aThat );
    return aThat.ordinal() <= this.ordinal();
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
   * @return EVulgarness - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVulgarness findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EVulgarness item : values() ) {
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
   * @return EVulgarness - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EVulgarness findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return EVulgarness - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVulgarness findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EVulgarness item : values() ) {
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
   * @return EVulgarness - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EVulgarness findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return EVulgarness - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVulgarness findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVulgarness item : values() ) {
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
   * @return EVulgarness - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EVulgarness findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}
