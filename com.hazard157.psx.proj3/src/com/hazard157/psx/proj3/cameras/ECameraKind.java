package com.hazard157.psx.proj3.cameras;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx.proj3.cameras.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

import com.hazard157.lib.core.utils.*;

/**
 * Разновидности снимающих камер.
 *
 * @author hazard157
 */
public enum ECameraKind
    implements IRadioPropEnum {

  /**
   * Без уточнения вида.
   */
  GENERIC( "generic", STR_N_CK_GENERIC, STR_D_CK_GENERIC, ICONID_CAMERA_GENERIC, ICONID_CAMERA_GENERIC_DIMMED ), //$NON-NLS-1$

  /**
   * VHS видеокамера.
   */
  VHS( "vhs", STR_N_CK_VHS, STR_D_CK_VHS, ICONID_CAMERA_VHS, ICONID_CAMERA_VHS_DIMMED ), //$NON-NLS-1$

  /**
   * DV видекамера.
   */
  DV( "dv", STR_N_CK_DV, STR_D_CK_DV, ICONID_CAMERA_DV, ICONID_CAMERA_DV_DIMMED ), //$NON-NLS-1$

  /**
   * Web-камера.
   */
  WEB( "web", STR_N_CK_WEB, STR_D_CK_WEB, ICONID_CAMERA_WEB, ICONID_CAMERA_WEB_DIMMED ), //$NON-NLS-1$

  /**
   * Фотоаппарат.
   */
  FOTO( "foto", STR_N_CK_FOTO, STR_D_CK_FOTO, ICONID_CAMERA_FOTO, ICONID_CAMERA_FOTO_DIMMED ), //$NON-NLS-1$

  /**
   * Смартфон.
   */
  PHONE( "phone", STR_N_CK_PHONE, STR_D_CK_PHONE, ICONID_CAMERA_PHONE, ICONID_CAMERA_PHONE_DIMMED ), //$NON-NLS-1$

  ;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "CameraKind"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ECameraKind> KEEPER = new StridableEnumKeeper<>( ECameraKind.class );

  private static IStridablesListEdit<ECameraKind> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final String iconId;
  private final String iconIdDimmed;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   * @param aIconId String - идентифиатор значка
   * @param aIconIdDimmed String - идентифиатор тусклого значка
   */
  ECameraKind( String aId, String aName, String aDescription, String aIconId, String aIconIdDimmed ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
    iconIdDimmed = aIconIdDimmed;
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
   * Возвращает идентификатор значка для отображения вида камеры.
   *
   * @return String - идентификатор значка
   */
  @Override
  public String iconId() {
    return iconId;
  }

  /**
   * Возвращает идентификатор тусклого значка для отображения вида камеры.
   *
   * @return String - идентификатор тусклого значка
   */
  public String iconIdDimmed() {
    return iconIdDimmed;
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link ECameraKind} &gt; - список всех констант
   */
  public static IStridablesList<ECameraKind> asList() {
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
   * @return ECameraKind - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ECameraKind findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ECameraKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ECameraKind getById( String aId ) {
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
   * @return ECameraKind - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ECameraKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ECameraKind item : values() ) {
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
   * @return ECameraKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ECameraKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
