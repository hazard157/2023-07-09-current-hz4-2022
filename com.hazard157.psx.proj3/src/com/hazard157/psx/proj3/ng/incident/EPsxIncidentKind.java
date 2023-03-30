package com.hazard157.psx.proj3.ng.incident;

import static com.hazard157.psx.proj3.ng.incident.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.gaze.impl.*;

/**
 * Incident kind.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public enum EPsxIncidentKind
    implements IStridable {

  INC_EPISODE( "episode", STR_N_INC_EPISODE, STR_D_INC_EPISODE, //$NON-NLS-1$
      EpisodeUtils.CHAR_EPISODE_ID_PREFIX, EpisodeUtils.EPISODE_ID_VALIDATOR ),

  INC_GAZE( "gaze", STR_N_INC_GAZE, STR_D_INC_GAZE, //$NON-NLS-1$
      GazeUtils.CHAR_GAZE_ID_PREFIX, GazeUtils.GAZE_ID_STR_VALIDATOR ),

  ;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link ValobjUtils}.
   */
  public static final String KEEPER_ID = "EIncidentKind"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EPsxIncidentKind> KEEPER = new StridableEnumKeeper<>( EPsxIncidentKind.class );

  private static IStridablesListEdit<EPsxIncidentKind> list = null;

  private final String               id;
  private final String               name;
  private final String               description;
  private final char                 chIdPrefix;
  private final ITsValidator<String> idValidator;

  EPsxIncidentKind( String aId, String aName, String aDescription, char aIdPrefixChar,
      ITsValidator<String> aIdValidator ) {
    id = aId;
    name = aName;
    description = aDescription;
    chIdPrefix = aIdPrefixChar;
    idValidator = aIdValidator;
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
   * Returns the ID string validator for this kind of incident.
   *
   * @return {@link ITsValidator}&lt;String&gt; - the validator
   */
  public ITsValidator<String> getIdValidator() {
    return idValidator;
  }

  /**
   * Returns the prefix char of incident ID string "xYYYY_DD_MM".
   *
   * @return char - ID prefix symbol
   */
  public char getIdPrefixChar() {
    return chIdPrefix;
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EPsxIncidentKind} &gt; - список всех констант
   */
  public static IStridablesList<EPsxIncidentKind> asList() {
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
   * @return EIncidentKind - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EPsxIncidentKind findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EIncidentKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EPsxIncidentKind getById( String aId ) {
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
   * @return EIncidentKind - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EPsxIncidentKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EPsxIncidentKind item : values() ) {
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
   * @return EIncidentKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EPsxIncidentKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
