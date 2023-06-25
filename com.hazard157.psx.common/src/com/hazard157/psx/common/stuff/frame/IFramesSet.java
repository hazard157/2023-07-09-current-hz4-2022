package com.hazard157.psx.common.stuff.frame;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Container of frames.
 *
 * @author hazard157
 */
public interface IFramesSet {

  /**
   * Empty immutable frames set ingleton.
   */
  IFramesSet EMPTY = new InternalEmptyFramesSet();

  /**
   * Determines if set is empty (contains no frames).
   *
   * @return boolean - empty collection flag
   */
  default boolean isEmpty() {
    return items().isEmpty();
  }

  /**
   * Returns all elements in container.
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - sorted list of all frames in set
   */
  IList<IFrame> items();

  /**
   * Returns all episodes mentioned in the frames.
   * <p>
   * Episode KEEPER_ID for {@link IFrame#NONE} (if such frame is i list) is included.
   *
   * @return {@link IStringList} - list of episode IDs
   */
  IStringList listEpisodesIds();

  /**
   * Determines if all {@link #items} contains frames of one episode.
   * <p>
   * For an empty {@link #items} returns <code>null</code>.
   *
   * @return String - the only episode ID or <code>null</code>
   */
  String singleEpisodeId();

  /**
   * Returns all episodes mentioned in the frames.
   * <p>
   * Camera KEEPER_ID for {@link IFrame#NONE} (if such frame is i list) is included.
   *
   * @return {@link IStringList} - list of episode IDs
   */
  IStringList listCameraIds();

  /**
   * Selects frames by episode IDs.
   *
   * @param aEpisodeId String - the episode ID
   * @return {@link IList}&lt;{@link IFrame}&gt; - selected frames
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such episode
   */
  IList<IFrame> listByEpisodeId( String aEpisodeId );

  /**
   * Selects frames by camera IDs.
   *
   * @param aCameraId String the camera KEEPER_ID
   * @return {@link IList}&lt;{@link IFrame}&gt; - selected frames
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such camera
   */
  IList<IFrame> listByCameraId( String aCameraId );

  /**
   * Selects frames by episode and camera IDs.
   *
   * @param aEpisodeId String - the episode KEEPER_ID
   * @param aCameraId String the camera KEEPER_ID
   * @return {@link IList}&lt;{@link IFrame}&gt; - selected frames
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such episode
   * @throws TsItemNotFoundRtException no such camera
   */
  IList<IFrame> listByBothIds( String aEpisodeId, String aCameraId );

  /**
   * Adds to the argument list frames from interfal.
   *
   * @param aInterval {@link Secint} - the interval
   * @param aList {@link IListEdit}&lt;{@link IFrame}&gt; - the list to add items
   */
  void collectInInterval( Secint aInterval, IListEdit<IFrame> aList );

  /**
   * Filter out items and returns new instance of {@link IFramesSet}.
   *
   * @param aFilter {@link ITsFilter}&lt;{@link IFrame}&gt; - the filter
   * @return {@link IFramesSet} - new instance of the set
   */
  IFramesSet filterOut( ITsFilter<IFrame> aFilter );

  /**
   * Returns the index of the first item in {@link #items()} with {@link IFrame#secNo()} >= <code>aSec</code>.
   * <p>
   * For an empty list returns -1.
   *
   * @param aSec int - the second
   * @return int - index of first item or -1 if all items are "below" the argument
   */
  int startIndex( int aSec );

  /**
   * Returns the index of the last item in {@link #items()} with {@link IFrame#secNo()} <= <code>aSec</code>.
   * <p>
   * For an empty list returns -1.
   *
   * @param aSec int - the second
   * @return int - index of first item or -1 if all items are "above" the argument
   */
  int endIndex( int aSec );

}

class InternalEmptyFramesSet
    implements IFramesSet {

  @Override
  public IList<IFrame> items() {
    return IList.EMPTY;
  }

  @Override
  public IStringList listEpisodesIds() {
    return IStringList.EMPTY;
  }

  @Override
  public String singleEpisodeId() {
    return null;
  }

  @Override
  public IStringList listCameraIds() {
    return IStringList.EMPTY;
  }

  @Override
  public IList<IFrame> listByEpisodeId( String aEpisodeId ) {
    return IList.EMPTY;
  }

  @Override
  public IList<IFrame> listByCameraId( String aCameraId ) {
    return IList.EMPTY;
  }

  @Override
  public IList<IFrame> listByBothIds( String aEpisodeId, String aCameraId ) {
    return IList.EMPTY;
  }

  @Override
  public void collectInInterval( Secint aInterval, IListEdit<IFrame> aList ) {
    // nop
  }

  @Override
  public IFramesSet filterOut( ITsFilter<IFrame> aFilter ) {
    return EMPTY;
  }

  @Override
  public int startIndex( int aSec ) {
    return -1;
  }

  @Override
  public int endIndex( int aSec ) {
    return -1;
  }

}
