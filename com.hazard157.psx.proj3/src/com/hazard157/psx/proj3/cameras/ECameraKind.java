package com.hazard157.psx.proj3.cameras;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx.proj3.l10n.IPsxCommonSharedResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.utils.*;

/**
 * Varieties of filming cameras.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ECameraKind
    implements IRadioPropEnum {

  GENERIC( "generic", STR_ECK_GENERIC, STR_ECK_GENERIC_D, ICONID_CAMERA_GENERIC, ICONID_CAMERA_GENERIC_DIMMED ), //$NON-NLS-1$

  VHS( "vhs", STR_ECK_VHS, STR_ECK_VHS_D, ICONID_CAMERA_VHS, ICONID_CAMERA_VHS_DIMMED ), //$NON-NLS-1$

  DV( "dv", STR_ECK_DV, STR_ECK_DV_D, ICONID_CAMERA_DV, ICONID_CAMERA_DV_DIMMED ), //$NON-NLS-1$

  WEB( "web", STR_ECK_WEB, STR_ECK_WEB_D, ICONID_CAMERA_WEB, ICONID_CAMERA_WEB_DIMMED ), //$NON-NLS-1$

  FOTO( "foto", STR_ECK_FOTO, STR_ECK_FOTO_D, ICONID_CAMERA_FOTO, ICONID_CAMERA_FOTO_DIMMED ), //$NON-NLS-1$

  PHONE( "phone", STR_ECK_PHONE, STR_ECK_PHONE_D, ICONID_CAMERA_PHONE, ICONID_CAMERA_PHONE_DIMMED ), //$NON-NLS-1$

  ;

  /**
   * The registered keepe ID.
   */
  public static final String KEEPER_ID = "CameraKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ECameraKind> KEEPER = new StridableEnumKeeper<>( ECameraKind.class );

  private static IStridablesListEdit<ECameraKind> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final String iconId;
  private final String iconIdDimmed;

  ECameraKind( String aId, String aName, String aDescription, String aIconId, String aIconIdDimmed ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
    iconIdDimmed = aIconIdDimmed;
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

  // ----------------------------------------------------------------------------------
  // API
  //

  @Override
  public String iconId() {
    return iconId;
  }

  /**
   * Returns the dimmed icon ID.
   *
   * @return String - the dimmed icon ID
   */
  public String iconIdDimmed() {
    return iconIdDimmed;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ECameraKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ECameraKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ECameraKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ECameraKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECameraKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECameraKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ECameraKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
