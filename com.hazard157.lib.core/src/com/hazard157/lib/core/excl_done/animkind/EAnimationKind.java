package com.hazard157.lib.core.excl_done.animkind;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.excl_done.animkind.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Критерий выборки по типу анимированности изображения.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public enum EAnimationKind
    implements IStridable {

  BOTH( "Both", STR_D_IAC_BOTH, STR_N_IAC_BOTH, true, true, ICONID_AK_BOTH ),

  SINGLE( "Single", STR_D_IAC_SINGLE, STR_N_IAC_SINGLE, true, false, ICONID_AK_SINGLE ),

  ANIMATED( "Animated", STR_D_IAC_ANIMATED, STR_N_IAC_ANIMATED, false, true, ICONID_AK_ANIMATED );

  private final String  id;
  private final String  description;
  private final String  name;
  private final boolean isSingle;
  private final boolean isAnimated;
  private final String  iconId;

  EAnimationKind( String aId, String aDescr, String aName, boolean aIsSingle, boolean aIsAnimated, String aIconId ) {
    id = aId;
    description = aDescr;
    name = aName;
    isSingle = aIsSingle;
    isAnimated = aIsAnimated;
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
  // Дополнительное API
  //

  /**
   * Определяет, включены ли одиночные изображения.
   *
   * @return boolean - признак, включены ли одиночные изображения
   */
  public boolean isSingle() {
    return isSingle;
  }

  /**
   * Определяет, включены ли анимированные изображения.
   *
   * @return boolean - признак, включены ли анимированные изображения
   */
  public boolean isAnimated() {
    return isAnimated;
  }

  /**
   * Определяет, включен ли хотя бы один тип изображения.
   *
   * @return boolean - признак, включен ли хотя бы один тип изображения
   */
  public boolean isAny() {
    return isSingle || isAnimated;
  }

  /**
   * Определяет, включены ли оба типа изображения.
   * <p>
   * Равнозначно проверки равнества константе {@link #BOTH}.
   *
   * @return boolean - признак, включены ли оба типа изображения.
   */
  public boolean isBoth() {
    return isSingle && isAnimated;
  }

  /**
   * Возвращает идентификатор значка, соответствующий константе.
   *
   * @return String - идентификатор значка константы
   */
  public String iconId() {
    return iconId;
  }

  /**
   * Возвращает константу, соответствующую сочетанию признаков.
   *
   * @param aIsSingle boolean - признак, включены ли одиночные изображения
   * @param aIsAnimated boolean - признак, включены ли анимированные изображения
   * @return {@link EAnimationKind} - соответствующая константа
   */
  public static EAnimationKind getType( boolean aIsSingle, boolean aIsAnimated ) {
    if( aIsSingle ) {
      if( aIsAnimated ) {
        return BOTH;
      }
      return SINGLE;
    }
    if( aIsAnimated ) {
      return ANIMATED;
    }
    return BOTH;
  }

  /**
   * Определяет, подходит ли такой признак по критерии константы.
   *
   * @param aAnimated bollean - признак анимированности изображения
   * @return boolean - признак соответствия константе
   */
  public boolean accept( boolean aAnimated ) {
    switch( this ) {
      case BOTH:
        return true;
      case ANIMATED:
        return aAnimated;
      case SINGLE:
        return !aAnimated;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
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
   * @return EShowImageType - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EAnimationKind findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EAnimationKind item : values() ) {
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
   * @return EShowImageType - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EAnimationKind findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return EShowImageType - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EAnimationKind findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EAnimationKind item : values() ) {
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
   * @return EShowImageType - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EAnimationKind findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return EShowImageType - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EAnimationKind findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EAnimationKind item : values() ) {
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
   * @return EShowImageType - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EAnimationKind findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}
