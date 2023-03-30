package com.hazard157.lib.core.quants.naughty;

import static com.hazard157.lib.core.quants.naughty.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.*;

/**
 * The degree of debauchery of a woman.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ENaughtiness
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, STR_N_UNKNOWN, STR_D_UNKNOWN ),

  POSING( "posing", STR_N_POSING, STR_D_POSING ), //$NON-NLS-1$

  SHOWING( "showing", STR_N_SHOWING, STR_D_SHOWING ), //$NON-NLS-1$

  GAPING( "gaping", STR_N_GAPING, STR_D_GAPING ), //$NON-NLS-1$

  INSERTING( "inserting", STR_N_INSERTING, STR_D_INSERTING ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ENaughtiness"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ENaughtiness> KEEPER = new StridableEnumKeeper<>( ENaughtiness.class );

  private static IStridablesListEdit<ENaughtiness> list = null;

  private final String id;
  private final String name;
  private final String description;

  ENaughtiness( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
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
   * @return {@link IStridablesList}&lt; {@link ENaughtiness} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ENaughtiness> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ENaughtiness} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ENaughtiness getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ENaughtiness} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ENaughtiness findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ENaughtiness item : values() ) {
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
   * @return {@link ENaughtiness} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ENaughtiness getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
