package com.hazard157.lib.core.quants.eyecol;

import static com.hazard157.lib.core.quants.eyecol.IHzResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.*;

/**
 * Girl eye color.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EEyeColor
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, STR_N_UNKNOWN, STR_D_UNKNOWN, ETsColor.WHITE.rgb() ),

  BLACK( "black", STR_N_BLACK, STR_D_BLACK, ETsColor.BLACK.rgb() ), //$NON-NLS-1$

  GREEN( "green", STR_N_GREEN, STR_D_GREEN, ETsColor.GREEN.rgb() ), //$NON-NLS-1$

  GREY( "grey", STR_N_GREY, STR_D_GREY, ETsColor.GRAY.rgb() ), //$NON-NLS-1$

  BROWN( "brown", STR_N_BROWN, STR_D_BROWN, ETsColor.DARK_YELLOW.rgb() ), //$NON-NLS-1$

  BLUE( "blue", STR_N_BLUE, STR_D_BLUE, ETsColor.BLUE.rgb() ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EEyeColor"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EEyeColor> KEEPER = new StridableEnumKeeper<>( EEyeColor.class );

  private static IStridablesListEdit<EEyeColor> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final RGB    rgb;

  EEyeColor( String aId, String aName, String aDescription, RGB aRgb ) {
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
   * @return {@link IStridablesList}&lt; {@link EEyeColor} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EEyeColor> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EEyeColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EEyeColor getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EEyeColor} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EEyeColor findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EEyeColor item : values() ) {
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
   * @return {@link EEyeColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EEyeColor getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
