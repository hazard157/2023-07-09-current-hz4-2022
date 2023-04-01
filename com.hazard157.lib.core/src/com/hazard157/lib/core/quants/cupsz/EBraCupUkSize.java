package com.hazard157.lib.core.quants.cupsz;

import static com.hazard157.lib.core.quants.cupsz.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.*;

/**
 * The enumeration of XXX.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EBraCupUkSize
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, STR_N_UNKNOWN, STR_D_UNKNOWN, -1 ),

  CUP_A( "Cup_A", STR_N_CUP_A, STR_D_CUP_A, 1 ), //$NON-NLS-1$

  CUP_B( "Cup_B", STR_N_CUP_B, STR_D_CUP_B, 2 ), //$NON-NLS-1$

  CUP_C( "Cup_C", STR_N_CUP_C, STR_D_CUP_C, 3 ), //$NON-NLS-1$

  CUP_D( "Cup_D", STR_N_CUP_D, STR_D_CUP_D, 4 ), //$NON-NLS-1$

  CUP_DD( "Cup_DD", STR_N_CUP_DD, STR_D_CUP_DD, 5 ), //$NON-NLS-1$

  CUP_E( "Cup_E", STR_N_CUP_E, STR_D_CUP_E, 6 ), //$NON-NLS-1$

  CUP_F( "Cup_F", STR_N_CUP_F, STR_D_CUP_F, 7 ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EBraCupUkSize"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EBraCupUkSize> KEEPER = new StridableEnumKeeper<>( EBraCupUkSize.class );

  private static IStridablesListEdit<EBraCupUkSize> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    bbDiff;

  EBraCupUkSize( String aId, String aName, String aDescription, int aBbDiff ) {
    id = aId;
    name = aName;
    description = aDescription;
    bbDiff = aBbDiff;
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
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EBraCupUkSize} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EBraCupUkSize> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns difference between the bust and the band measurements.
   * <p>
   * The bust/band difference determines bra size.
   *
   * @return int - bust/band difference in inches
   */
  public int bbDiff() {
    return bbDiff;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EBraCupUkSize} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EBraCupUkSize getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EBraCupUkSize} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EBraCupUkSize findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EBraCupUkSize item : values() ) {
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
   * @return {@link EBraCupUkSize} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EBraCupUkSize getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
