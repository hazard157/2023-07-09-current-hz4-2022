package com.hazard157.lib.core.quants.haircol;

import static com.hazard157.lib.core.quants.haircol.IHzResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.*;

/**
 * Girl hair color.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EHairColor
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, STR_N_UNKNOWN, STR_D_UNKNOWN, ETsColor.WHITE.rgb() ),

  BLACK( "black", STR_N_BLACK, STR_D_BLACK, ETsColor.BLACK.rgb() ), //$NON-NLS-1$

  BLONDE( "blonde", STR_N_BLONDE, STR_D_BLONDE, ETsColor.YELLOW.rgb() ), //$NON-NLS-1$

  RED( "red", STR_N_RED, STR_D_RED, ETsColor.RED.rgb() ), //$NON-NLS-1$

  BROWN( "brown", STR_N_BROWN, STR_D_BROWN, ETsColor.DARK_YELLOW.rgb() ), //$NON-NLS-1$

  DARK_BROWN( "dark_brown", STR_N_DARK_BROWN, STR_D_DARK_BROWN, new RGB( 101, 67, 33 ) ), //$NON-NLS-1$

  BRUNETTE( "brunette", STR_N_BRUNETTE, STR_D_BRUNETTE, ETsColor.DARK_GRAY.rgb() ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EHairColor"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EHairColor> KEEPER = new StridableEnumKeeper<>( EHairColor.class );

  private static IStridablesListEdit<EHairColor> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final RGB    rgb;

  EHairColor( String aId, String aName, String aDescription, RGB aRgb ) {
    id = aId;
    name = aName;
    description = aDescription;
    rgb = aRgb;
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

  /**
   * Returns the color.
   *
   * @return {@link RGB} - the color as {@link RGB}
   */
  public RGB rgb() {
    return rgb;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EHairColor} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EHairColor> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EHairColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EHairColor getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EHairColor} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EHairColor findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EHairColor item : values() ) {
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
   * @return {@link EHairColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EHairColor getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
