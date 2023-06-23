package com.hazard157.psx.proj3.episodes.impl;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Реализация {@link IUnitEpisodes}.
 *
 * @author hazard157
 */
public class UnitEpisodes
    extends AbstractSinentManager<IEpisode, EpisodeInfo>
    implements IUnitEpisodes {

  /**
   * Кеш: карта "Ид эпизода" - "список использованных кадров".
   * <p>
   * Отсуствие элемента для эпизода означает, что надо создать кеш.
   * <p>
   * Списки в карте отсторированы по парвилам {@link IFrame#compareTo(IFrame)}.
   */
  private final IStringMapEdit<IListBasicEdit<IFrame>> usedFramesCache = new StringMap<>();

  /**
   * Конструктор.
   */
  public UnitEpisodes() {
    super( TsLibUtils.EMPTY_STRING, EpisodeKeeper.KEEPER );
    for( String epId : items().keys() ) {
      initUsedFramesCacheForEpisode( epId );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected IEpisode doCreateItem( String aId, EpisodeInfo aInfo ) {
    return new Episode( aId, aInfo );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private static void addIfNeeded( IListBasicEdit<IFrame> aList, IFrame aFrame ) {
    if( aFrame.isDefined() ) {
      if( !aList.hasElem( aFrame ) ) {
        aList.add( aFrame );
      }
    }
  }

  private IList<IFrame> initUsedFramesCacheForEpisode( String aEpisodeId ) {

    // FIXME обновление кеша при правке эпизода???

    IEpisode e = items().getByKey( aEpisodeId );
    IListBasicEdit<IFrame> ll = new SortedElemLinkedBundleList<>();
    addIfNeeded( ll, e.frame() );
    for( IScene scene : e.story().allScenesBelow() ) {
      addIfNeeded( ll, scene.frame() );
    }
    for( Mark<PlaneGuide> mark : e.planesLine().marksList() ) {
      addIfNeeded( ll, mark.marker().frame() );
    }
    for( IFrame f : e.getIllustrations( true ) ) {
      addIfNeeded( ll, f );
    }
    usedFramesCache.put( aEpisodeId, ll );
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // IUnitEpisodes
  //

  public IList<IFrame> listUsedFrames( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    if( !items().hasKey( aSvin.episodeId() ) ) {
      return IList.EMPTY;
    }
    IList<IFrame> all = usedFramesCache.findByKey( aSvin.episodeId() );
    if( all == null ) {
      all = initUsedFramesCacheForEpisode( aSvin.episodeId() );
    }
    if( aSvin.hasCam() && !aSvin.isWholeEpisode() ) {
      return all;
    }
    IListEdit<IFrame> result = new ElemLinkedBundleList<>();
    for( IFrame f : all ) {
      if( aSvin.hasCam() ) {
        if( !f.cameraId().equals( aSvin.cameraId() ) ) {
          continue;
        }
      }
      if( !aSvin.isWholeEpisode() ) {
        int frameSecNo = f.frameNo() / FPS;
        if( !aSvin.interval().contains( frameSecNo ) ) {
          continue;
        }
      }
      result.add( f );
    }
    return result;
  }

}
