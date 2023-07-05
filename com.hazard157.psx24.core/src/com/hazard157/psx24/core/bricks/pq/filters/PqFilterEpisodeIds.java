package com.hazard157.psx24.core.bricks.pq.filters;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Filter of kind {@link EPqSingleFilterKind#EPISODE_IDS}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PqFilterEpisodeIds
    implements ITsFilter<SecondSlice> {

  /**
   * The filter type ID.
   */
  public static final String TYPE_ID = "pq.filter.EpisodeIds"; //$NON-NLS-1$

  /**
   * The filter factory.
   */
  public static final ITsSingleFilterFactory<SecondSlice> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, SecondSlice.class ) {

        @Override
        protected ITsFilter<SecondSlice> doCreateFilter( IOptionSet aParams ) {
          IStringList episodeIds = aParams.getValobj( OPID_EPISODE_IDS );
          return new PqFilterEpisodeIds( episodeIds );
        }
      };

  public static final String OPID_EPISODE_IDS = "episodeIds"; //$NON-NLS-1$

  private final IStringList episodeIds;

  /**
   * Constructor.
   *
   * @param aEpisodeIds {@link IStringList} - the episode IDs
   */
  public PqFilterEpisodeIds( IStringList aEpisodeIds ) {
    episodeIds = new StringArrayList( aEpisodeIds );
  }

  /**
   * Creates filter parameters.
   *
   * @param aEpisodeIds {@link IStringList} - the episode IDs
   * @return {@link ITsSingleFilterParams} - the filter parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsSingleFilterParams makeFilterParams( IStringList aEpisodeIds ) {
    IOptionSetEdit p = new OptionSet();
    p.setValobj( OPID_EPISODE_IDS, aEpisodeIds );
    return new TsSingleFilterParams( TYPE_ID, p );
  }

  /**
   * Returns a human-readable string of filter options.
   *
   * @param aParams {@link ITsSingleFilterParams} - the filter parameters
   * @return String - single-line text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the parameters if not of this filter
   */
  @SuppressWarnings( "nls" )
  public static final String makeHumanReadableString( ITsSingleFilterParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    TsIllegalArgumentRtException.checkFalse( aParams.typeId().equals( TYPE_ID ) );
    StringBuilder sb = new StringBuilder();
    IStringList episodeIds = aParams.params().getValobj( PqFilterEpisodeIds.OPID_EPISODE_IDS );
    for( int i = 0, n = episodeIds.size(); i < n; i++ ) {
      sb.append( StridUtils.getLast( episodeIds.get( i ) ) );
      sb.append( ' ' );
    }
    return String.format( "Episodes: %s", sb.toString() );
  }

  @Override
  public boolean accept( SecondSlice aObj ) {
    if( episodeIds.isEmpty() ) {
      return true;
    }
    return episodeIds.hasElem( aObj.episode().id() );
  }

}
