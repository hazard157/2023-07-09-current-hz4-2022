package com.hazard157.psx24.core.bricks.pq.filters;

import static com.hazard157.psx24.core.bricks.pq.filters.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * The kind of the single PSX filter..
 *
 * @author hazard157
 */
public enum EPqSingleFilterKind
    implements IStridable {

  /**
   * Search text in various places.
   */
  ANY_TEXT( PqFilterAnyText.TYPE_ID, STR_N_ANY_TEXT, STR_D_ANY_TEXT, PqFilterAnyText.FACTORY ),

  /**
   * Search for tag marks.
   */
  TAG_IDS( PqFilterTagIds.TYPE_ID, STR_N_TAG_IDS, STR_D_TAG_IDS, PqFilterTagIds.FACTORY ),

  /**
   * Select episodes.
   */
  EPISODE_IDS( PqFilterEpisodeIds.TYPE_ID, STR_N_EPISODE_IDS, STR_D_EPISODE_IDS, PqFilterEpisodeIds.FACTORY ),

  ;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "EPqSingleFilterKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EPqSingleFilterKind> KEEPER =
      new StridableEnumKeeper<>( EPqSingleFilterKind.class );

  private static IStridablesListEdit<EPqSingleFilterKind> list = null;

  private final String                              id;
  private final String                              name;
  private final String                              description;
  private final ITsSingleFilterFactory<SecondSlice> factory;

  EPqSingleFilterKind( String aId, String aName, String aDescription, ITsSingleFilterFactory<SecondSlice> aFactory ) {
    id = aId;
    name = aName;
    description = aDescription;
    factory = aFactory;
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
   * Returns the filter factory.
   *
   * @return {@link ITsSingleFilterFactory}&lt;{@link SecondSlice}&gt; - the filter factory
   */
  public ITsSingleFilterFactory<SecondSlice> factory() {
    return factory;
  }

  /**
   * Returns a human-readable string of filter options.
   *
   * @param aParams {@link ITsSingleFilterParams} - the filter parameters
   * @return String - single-line text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException arguemnt is a parameters of an unknown filter
   */
  public static final String makeHumanReadableString( ITsSingleFilterParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    EPqSingleFilterKind kind = asList().getByKey( aParams.typeId() );
    return switch( kind ) {
      case ANY_TEXT -> PqFilterAnyText.makeHumanReadableString( aParams );
      case TAG_IDS -> PqFilterTagIds.makeHumanReadableString( aParams );
      case EPISODE_IDS -> PqFilterEpisodeIds.makeHumanReadableString( aParams );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Returns all constant as a list/.
   *
   * @return {@link IStridablesList}&lt; {@link EPqSingleFilterKind} &gt; - the list of constants.
   */
  public static IStridablesList<EPqSingleFilterKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  @SuppressWarnings( "javadoc" )
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  @SuppressWarnings( "javadoc" )
  public static EPqSingleFilterKind findById( String aId ) {
    return asList().findByKey( aId );
  }

  @SuppressWarnings( "javadoc" )
  public static EPqSingleFilterKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  @SuppressWarnings( "javadoc" )
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  @SuppressWarnings( "javadoc" )
  public static EPqSingleFilterKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EPqSingleFilterKind item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  @SuppressWarnings( "javadoc" )
  public static EPqSingleFilterKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
