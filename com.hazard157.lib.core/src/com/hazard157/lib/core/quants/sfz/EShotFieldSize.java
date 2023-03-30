package com.hazard157.lib.core.quants.sfz;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.quants.sfz.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * План — «крупность» изображения, пойманная объективом, условное деление для приложений SX.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public enum EShotFieldSize
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, STR_D_SFZ_UNKNOWN, STR_N_SFZ_UNKNOWN, ICON_SFZ_UNKNOWN ),

  FULL( "Full", STR_D_SFZ_FULL, STR_N_SFZ_FULL, ICON_SFZ_FULL ), //$NON-NLS-1$

  AMERICAN( "American", STR_D_SFZ_AMERICAN, STR_N_SFZ_AMERICAN, ICON_SFZ_AMERICAN ), //$NON-NLS-1$

  MEDIUM( "Medium", STR_D_SFZ_MEDUM, STR_N_SFZ_MEDUM, ICON_SFZ_MEDIUM ), //$NON-NLS-1$

  CLOSEUP( "Closeup", STR_D_SFZ_CLOSEUP, STR_N_SFZ_CLOSEUP, ICON_SFZ_CLOSEUP ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EShotFieldSize"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EShotFieldSize> KEEPER = new StridableEnumKeeper<>( EShotFieldSize.class );

  private static IStridablesListEdit<EShotFieldSize> list = null;

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
   * @param aIconId String - идентификатор значка, может быть null
   */
  EShotFieldSize( String aId, String aDescr, String aName, String aIconId ) {
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
  // IRadioPropEnum
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EShotFieldSize} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EShotFieldSize> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EShotFieldSize} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EShotFieldSize getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EShotFieldSize} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EShotFieldSize findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EShotFieldSize item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EShotFieldSize} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EShotFieldSize getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
