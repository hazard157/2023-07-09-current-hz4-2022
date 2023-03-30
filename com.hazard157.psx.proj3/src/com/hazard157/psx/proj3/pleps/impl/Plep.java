package com.hazard157.psx.proj3.pleps.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.pleps.*;

/**
 * Реализация {@link IPlep}.
 *
 * @author hazard157
 */
class Plep
    extends AbstractSinentity<PlepInfo>
    implements IPlep {

  private final INotifierListEdit<IStir>  stirs  = new NotifierListEditWrapper<>( new ElemArrayList<IStir>() );
  private final INotifierListEdit<ITrack> tracks = new NotifierListEditWrapper<>( new ElemArrayList<ITrack>() );
  private final IListReorderer<IStir>     stirsReorderer;
  private final IListReorderer<ITrack>    tracksReorderer;

  private IUnitPleps unitPleps = null;

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор плана
   * @param aInfo {@link EpisodeInfo} - информация о планируемом эпизоде
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException идентификатор не ИД-путь
   */
  Plep( String aId, PlepInfo aInfo ) {
    super( aId, aInfo );
    stirsReorderer = new ListReorderer<>( stirs );
    tracksReorderer = new ListReorderer<>( tracks );
    stirs.addCollectionChangeListener( childCollChangeListener );
    tracks.addCollectionChangeListener( childCollChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  void setOwner( IUnitPleps aUnit ) {
    unitPleps = aUnit;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPlep
  //

  @Override
  public IList<IStir> stirs() {
    return stirs;
  }

  @Override
  public IStir newStir( int aIndex, IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    TsIllegalArgumentRtException.checkTrue( aIndex < -1 || aIndex > stirs.size() );
    Stir s = new Stir();
    s.setPlep( this );
    s.params().addAll( aParams );
    s.genericChangeEventer().addListener( eventer );
    if( aIndex < 0 || aIndex == stirs.size() ) {
      stirs.add( s );
    }
    else {
      stirs.insert( aIndex, s );
    }
    eventer.fireChangeEvent();
    return s;
  }

  @Override
  public void removeStir( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= stirs.size() );
    IStir s = stirs.removeByIndex( aIndex );
    s.genericChangeEventer().removeListener( eventer );
    eventer.fireChangeEvent();
  }

  @Override
  public IListReorderer<IStir> stirsReorderer() {
    return stirsReorderer;
  }

  @Override
  public INotifierListEdit<ITrack> tracks() {
    return tracks;
  }

  @Override
  public ITrack newTrack( int aIndex, String aSongId, Secint aInterval ) {
    StridUtils.checkValidIdPath( aSongId );
    TsNullArgumentRtException.checkNull( aInterval );
    TsIllegalArgumentRtException.checkTrue( aIndex < -1 || aIndex > tracks.size() );
    Track t = new Track( aSongId, aInterval );
    t.setPlep( this );
    if( aIndex < 0 || aIndex == tracks.size() ) {
      tracks.add( t );
    }
    else {
      tracks.insert( aIndex, t );
    }
    eventer.fireChangeEvent();
    return t;
  }

  @Override
  public void removeTrack( int aIndex ) {
    tracks.removeByIndex( aIndex );
    eventer.fireChangeEvent();
  }

  @Override
  public IListReorderer<ITrack> tracksReorderer() {
    return tracksReorderer;
  }

  @Override
  public int computeDuration() {
    int d1 = 0;
    for( IStir s : stirs ) {
      d1 += s.duration();
    }
    int d2 = 0;
    for( ITrack t : tracks ) {
      d2 += t.duration();
    }
    return Math.max( d1, d2 );
  }

  @Override
  public IUnitPleps unit() {
    TsIllegalStateRtException.checkNull( unitPleps );
    return unitPleps;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String nmName() {
    return info().name();
  }

  @Override
  public String description() {
    return info().description();
  }

}
