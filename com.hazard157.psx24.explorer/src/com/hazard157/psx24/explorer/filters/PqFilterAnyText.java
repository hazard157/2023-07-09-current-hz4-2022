package com.hazard157.psx24.explorer.filters;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Filter of kind {@link EPqSingleFilterKind#ANY_TEXT}.
 *
 * @author hazard157
 */
class PqFilterAnyText
    implements ITsFilter<SecondSlice> {

  /**
   * The filter type ID.
   */
  public static final String TYPE_ID = "pq.filter.Text"; //$NON-NLS-1$

  /**
   * The filter factory.
   */
  public static final ITsSingleFilterFactory<SecondSlice> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, SecondSlice.class ) {

        @Override
        protected ITsFilter<SecondSlice> doCreateFilter( IOptionSet aParams ) {
          String text = aParams.getStr( OPID_TEXT );
          ETextMatchMode matchMode = aParams.getValobj( OPID_MATCH_MODE );
          boolean inTags = aParams.getBool( OPID_IN_TAGS );
          boolean inScenes = aParams.getBool( OPID_IN_SCENES );
          boolean inPlanes = aParams.getBool( OPID_IN_PLANES );
          boolean inNotes = aParams.getBool( OPID_IN_NOTES );
          return new PqFilterAnyText( text, matchMode, inTags, inScenes, inNotes, inPlanes );
        }
      };

  public static final String OPID_TEXT       = "text";      //$NON-NLS-1$
  public static final String OPID_MATCH_MODE = "matchMode"; //$NON-NLS-1$
  public static final String OPID_IN_TAGS    = "inTags";    //$NON-NLS-1$
  public static final String OPID_IN_SCENES  = "inScenes";  //$NON-NLS-1$
  public static final String OPID_IN_NOTES   = "inNotes";   //$NON-NLS-1$
  public static final String OPID_IN_PLANES  = "inPlanes";  //$NON-NLS-1$

  private final String         text;
  private final ETextMatchMode matchMode;
  private final TextMatcher    textMatcher;
  private final boolean        checkTags;
  private final boolean        checkScenes;
  private final boolean        checkNotes;
  private final boolean        checkPlanes;

  /**
   * Constructor.
   *
   * @param aText String - the search text, the left operand of the comparison
   * @param aMatchMode {@link ETextMatchMode} - comparison operation
   * @param aInTags boolean - the sign to search in the tags
   * @param aInScenes boolean - the sign to search in the scenes names
   * @param aInNotes boolean - the sign to search in the notes
   * @param aInPlanes boolean - the sign to search in the plane names
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException text is an empty string
   * @throws TsIllegalArgumentRtException all signs are <code>false</code>
   */
  public PqFilterAnyText( String aText, ETextMatchMode aMatchMode, boolean aInTags, boolean aInScenes, boolean aInNotes,
      boolean aInPlanes ) {
    TsErrorUtils.checkNonEmpty( aText );
    TsNullArgumentRtException.checkNull( aMatchMode );
    TsErrorUtils.checkNonEmpty( aText );
    TsNullArgumentRtException.checkNull( aMatchMode );
    text = aText;
    matchMode = aMatchMode;
    textMatcher = new TextMatcher( matchMode, text );
    checkTags = aInTags;
    checkScenes = aInScenes;
    checkNotes = aInNotes;
    checkPlanes = aInPlanes;
  }

  /**
   * Creates filter parameters.
   *
   * @param aText String - the search text, the left operand of the comparison
   * @param aMatchMode {@link ETextMatchMode} - comparison operation
   * @param aInTags boolean - the sign to search in the tags
   * @param aInScenes boolean - the sign to search in the scenes names
   * @param aInNotes boolean - the sign to search in the notes
   * @param aInPlanes boolean - the sign to search in the plane names
   * @return {@link IOptionSet} - created parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException text is an empty string
   * @throws TsIllegalArgumentRtException all signs are <code>false</code>
   */
  public static ITsSingleFilterParams makeFilterParams( String aText, ETextMatchMode aMatchMode, boolean aInTags,
      boolean aInScenes, boolean aInNotes, boolean aInPlanes ) {
    TsErrorUtils.checkNonEmpty( aText );
    TsNullArgumentRtException.checkNull( aMatchMode );
    IOptionSetEdit p = new OptionSet();
    p.setStr( OPID_TEXT, aText );
    p.setValobj( OPID_MATCH_MODE, aMatchMode );
    p.setBool( OPID_IN_TAGS, aInTags );
    p.setBool( OPID_IN_SCENES, aInScenes );
    p.setBool( OPID_IN_NOTES, aInNotes );
    p.setBool( OPID_IN_PLANES, aInPlanes );
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
    return String.format( "Text: '%s'", aParams.params().getStr( OPID_TEXT ) );
  }

  @Override
  public boolean accept( SecondSlice aObj ) {
    if( checkTags ) {
      for( String tagId : aObj.tagIds() ) {
        if( textMatcher.match( tagId ) ) {
          return true;
        }
      }
    }
    if( checkScenes ) {
      for( IScene scene : aObj.scenes() ) {
        if( textMatcher.match( scene.info().name() ) ) {
          return true;
        }
      }
    }
    if( checkPlanes ) {
      if( textMatcher.match( aObj.plane().name() ) ) {
        return true;
      }
    }
    if( checkNotes ) {
      for( String note : aObj.notes() ) {
        if( textMatcher.match( note ) ) {
          return true;
        }
      }
    }
    return false;
  }

}
