package com.hazard157.psx.proj3.trailers.impl;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.proj3.trailers.*;

/**
 * Управление трейлерами.
 *
 * @author hazard157
 */
class TrailersSinentManager
    extends AbstractSinentManager<Trailer, TrailerInfo> {

  /**
   * Конструктор.
   */
  public TrailersSinentManager() {
    super( TsLibUtils.EMPTY_STRING, TrailerKeeper.KEEPER );
  }

  @Override
  protected Trailer doCreateItem( String aId, TrailerInfo aInfo ) {
    return new Trailer( aId, aInfo );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает трейлеры заданного эпизода.
   * <p>
   * Если такого эпизода нет, возвращает пустую карту.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @return IStringMap&lt;{@link Trailer}&gt; - список трейлеров запрошенного эпизода
   * @throws TsNullArgumentRtException аргумент = null
   */
  public IStridablesList<Trailer> listTrailersByEpisode( String aEpisodeId ) {
    TsNullArgumentRtException.checkNull( aEpisodeId );
    IStridablesListBasicEdit<Trailer> map = new SortedStridablesList<>();
    for( Trailer item : items() ) {
      if( item.episodeId().equals( aEpisodeId ) ) {
        map.add( item );
      }
    }
    return map;
  }

}
