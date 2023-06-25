package com.hazard157.psx.common.stuff.frame;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * {@link IFramesSet} implementation.
 *
 * @author hazard157
 */
public class FramesSet
    implements IFramesSet {

  private final INotifierListBasicEdit<IFrame> items =
      new NotifierListBasicEditWrapper<>( new SortedElemLinkedBundleList<IFrame>() );

  private IIntListEdit                                      cacheMinutesIndexes = null;
  private IStringMapEdit<IListEdit<IFrame>>                 cacheMapByEpisodes  = null;
  private IStringMapEdit<IListEdit<IFrame>>                 cacheMapByCameras   = null;
  private IStringMapEdit<IStringMapEdit<IListEdit<IFrame>>> cacheMapByEpAndCam  = null;

  /**
   * Constructor.
   */
  public FramesSet() {
    items.addCollectionChangeListener( ( src, op, item ) -> resetCaches() );
  }

  /**
   * Constructor.
   *
   * @param aFrame {@link IList}&lt;{@link IFrame}&gt; - initial content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FramesSet( IList<IFrame> aFrame ) {
    this();
    items.addAll( aFrame );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void resetCaches() {
    cacheMinutesIndexes = null;
    cacheMapByEpisodes = null;
    cacheMapByCameras = null;
    cacheMapByEpAndCam = null;
  }

  void ensureCaches() {
    cacheMapByEpisodes = new StringMap<>();
    cacheMapByCameras = new StringMap<>();
    cacheMapByEpAndCam = new StringMap<>();
    cacheMinutesIndexes = new IntArrayList();

    int prevMinute = -1;

    for( int i = 0, n = items.size(); i < n; i++ ) {
      IFrame f = items.get( i );
      String epId = f.episodeId();
      String camId = f.cameraId();
      // minutes starting indexes cache
      int currMinute = f.secNo() / 60;
      for( int jj = prevMinute + 1; jj <= currMinute; jj++ ) {
        cacheMinutesIndexes.add( i );
      }
      prevMinute = currMinute;
      // episodes map
      IListEdit<IFrame> llEp = cacheMapByEpisodes.findByKey( camId );
      if( llEp == null ) {
        llEp = new ElemLinkedBundleList<>();
        cacheMapByEpisodes.put( epId, llEp );
      }
      llEp.add( f );
      // cameras map
      IListEdit<IFrame> llCam = cacheMapByCameras.findByKey( camId );
      if( llCam == null ) {
        llCam = new ElemLinkedBundleList<>();
        cacheMapByCameras.put( camId, llCam );
      }
      llCam.add( f );
      // episode & cameras map
      IStringMapEdit<IListEdit<IFrame>> mapEpCam = cacheMapByEpAndCam.findByKey( epId );
      if( mapEpCam == null ) {
        mapEpCam = new StringMap<>();
        cacheMapByEpAndCam.put( epId, mapEpCam );
      }
      IListEdit<IFrame> llEpCam = mapEpCam.findByKey( camId );
      if( llEpCam == null ) {
        llEpCam = new ElemLinkedBundleList<>();
        mapEpCam.put( camId, llEpCam );
      }
      llEpCam.add( f );
    }
  }

  // ------------------------------------------------------------------------------------
  // IFramesSet
  //

  @Override
  public IListBasicEdit<IFrame> items() {
    return items;
  }

  @Override
  public IStringList listEpisodesIds() {
    ensureCaches();
    return cacheMapByEpisodes.keys();
  }

  @Override
  public String singleEpisodeId() {
    ensureCaches();
    if( cacheMapByEpisodes.size() == 1 ) {
      return cacheMapByEpisodes.keys().first();
    }
    return null;
  }

  @Override
  public IStringList listCameraIds() {
    ensureCaches();
    return cacheMapByCameras.keys();
  }

  @Override
  public IList<IFrame> listByEpisodeId( String aEpisodeId ) {
    ensureCaches();
    return cacheMapByEpisodes.getByKey( aEpisodeId );
  }

  @Override
  public IList<IFrame> listByCameraId( String aCameraId ) {
    ensureCaches();
    return cacheMapByCameras.getByKey( aCameraId );
  }

  @Override
  public IList<IFrame> listByBothIds( String aEpisodeId, String aCameraId ) {
    ensureCaches();
    return cacheMapByEpAndCam.getByKey( aEpisodeId ).getByKey( aCameraId );
  }

  @Override
  public void collectInInterval( Secint aInterval, IListEdit<IFrame> aList ) {
    TsNullArgumentRtException.checkNulls( aInterval, aList );
    ensureCaches();
    int startMinute = aInterval.start() / 60;
    int endMinute = 1 + aInterval.end() / 60;
    int maxMinutes = cacheMinutesIndexes.size();
    if( startMinute >= maxMinutes ) {
      return;
    }
    int startIndex = cacheMinutesIndexes.getValue( startMinute );
    int endIndex = items.size() - 1;
    if( endMinute < maxMinutes ) {
      endIndex = cacheMinutesIndexes.getValue( endMinute );
    }
    for( int i = startIndex; i <= endIndex; i++ ) {
      IFrame f = items.get( i );
      if( aInterval.contains( f.secNo() ) ) {
        aList.add( f );
      }
    }
  }

  @Override
  public IFramesSet filterOut( ITsFilter<IFrame> aFilter ) {
    TsNullArgumentRtException.checkNull( aFilter );
    FramesSet fs = new FramesSet();
    for( IFrame f : items ) {
      if( aFilter.accept( f ) ) {
        fs.items.add( f );
      }
    }
    return fs;
  }

  @Override
  public int startIndex( int aSec ) {
    if( items.isEmpty() ) {
      return -1;
    }
    // OPTIMIZE change to the binary search
    for( int i = 0; i < items.size(); i++ ) {
      if( items.get( i ).secNo() >= aSec ) {
        return i;
      }
    }
    return items.size() - 1;
  }

  @Override
  public int endIndex( int aSec ) {
    if( items.isEmpty() ) {
      return -1;
    }
    // OPTIMIZE change to the binary search
    for( int i = items.size() - 1; i >= 0; i-- ) {
      if( items.get( i ).secNo() <= aSec ) {
        return i;
      }
    }
    return -1;
  }

}
