package com.hazard157.prisex24.cofs;

import static com.hazard157.prisex24.cofs.l10n.IPsxCofsSharedResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of XXX.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public enum EIncidentMediaKind
    implements IStridable {

  MASTER( "master", STR_IMK_MASTER, STR_IMK_MASTER_D ), //$NON-NLS-1$

  SOURCE( "source", STR_IMK_SOURCE, STR_IMK_SOURCE_D ), //$NON-NLS-1$

  OUTPUT( "output", STR_IMK_OUTPUT, STR_IMK_OUTPUT_D ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EIncidentMediaKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EIncidentMediaKind> KEEPER = new StridableEnumKeeper<>( EIncidentMediaKind.class );

  private static IStridablesListEdit<EIncidentMediaKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  EIncidentMediaKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EIncidentMediaKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EIncidentMediaKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EIncidentMediaKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EIncidentMediaKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EIncidentMediaKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EIncidentMediaKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EIncidentMediaKind item : values() ) {
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
   * @return {@link EIncidentMediaKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EIncidentMediaKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
