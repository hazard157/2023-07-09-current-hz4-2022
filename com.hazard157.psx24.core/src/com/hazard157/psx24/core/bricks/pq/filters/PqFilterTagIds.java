package com.hazard157.psx24.core.bricks.pq.filters;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Filter of kind {@link EPqSingleFilterKind#TAG_IDS}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PqFilterTagIds
    implements ITsFilter<SecondSlice> {

  /**
   * The filter type ID.
   */
  public static final String TYPE_ID = "pq.filter.TagIds"; //$NON-NLS-1$

  /**
   * The filter factory.
   */
  public static final ITsSingleFilterFactory<SecondSlice> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, SecondSlice.class ) {

        @Override
        protected ITsFilter<SecondSlice> doCreateFilter( IOptionSet aParams ) {
          IStringList tagIds = aParams.getValobj( OPID_TAG_IDS );
          boolean iaAny = aParams.getBool( OPID_IS_ANY );
          return new PqFilterTagIds( tagIds, iaAny );
        }
      };

  public static final String OPID_TAG_IDS = "tagIds"; //$NON-NLS-1$

  public static final String OPID_IS_ANY = "isAny"; //$NON-NLS-1$

  private final IStringList tagIds;
  private final boolean     any;

  /**
   * Constructor.
   *
   * @param aTagIds {@link IStringList} - the tag IDs
   * @param aIsAny boolean - sign that at least one tag must match, not all tags
   */
  public PqFilterTagIds( IStringList aTagIds, boolean aIsAny ) {
    tagIds = new StringArrayList( aTagIds );
    any = aIsAny;
  }

  /**
   * Creates filter parameters.
   *
   * @param aTagIds {@link IStringList} - the tag IDs
   * @param aIsAny boolean - sign that at least one tag must match, not all tags
   * @return {@link ITsSingleFilterParams} - the filter parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsSingleFilterParams makeFilterParams( IStringList aTagIds, boolean aIsAny ) {
    IOptionSetEdit p = new OptionSet();
    p.setValobj( OPID_TAG_IDS, aTagIds );
    p.setBool( OPID_IS_ANY, aIsAny );
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
    IStringList tagIds = aParams.params().getValobj( PqFilterTagIds.OPID_TAG_IDS );
    for( int i = 0, n = tagIds.size(); i < n; i++ ) {
      sb.append( StridUtils.getLast( tagIds.get( i ) ) );
      sb.append( ' ' );
    }
    if( tagIds.size() > 1 ) {
      boolean isAny = aParams.params().getBool( PqFilterTagIds.OPID_IS_ANY );
      String strIsAny = isAny ? "ANY" : "ALL";
      return String.format( "Tags: %s %s", strIsAny, sb.toString() );
    }
    return String.format( "Tags: %s", sb.toString() );
  }

  @Override
  public boolean accept( SecondSlice aObj ) {
    if( any ) {
      return TsCollectionsUtils.intersects( aObj.tagIds(), tagIds );
    }
    return TsCollectionsUtils.contains( aObj.tagIds(), tagIds );
  }

}
