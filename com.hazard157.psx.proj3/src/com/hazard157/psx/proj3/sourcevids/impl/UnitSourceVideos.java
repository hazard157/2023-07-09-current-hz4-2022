package com.hazard157.psx.proj3.sourcevids.impl;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Реализация {@link IUnitSourceVideos}.
 *
 * @author hazard157
 */
public class UnitSourceVideos
    extends AbstractSinentManager<ISourceVideo, SourceVideoInfo>
    implements IUnitSourceVideos {

  /**
   * Конструктор.
   */
  public UnitSourceVideos() {
    super( TsLibUtils.EMPTY_STRING, SourceVideoKeeper.KEEPER );
  }

  @Override
  protected ISourceVideo doCreateItem( String aId, SourceVideoInfo aInfo ) {
    return new SourceVideo( aId, aInfo );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IUnitSourceVideos
  //

  @Override
  public IStringMap<ISourceVideo> episodeSourceVideos( String aEpisodeId ) {
    TsNullArgumentRtException.checkNull( aEpisodeId );
    IStringMapEdit<ISourceVideo> map = new StringMap<>();
    for( ISourceVideo sv : items() ) {
      if( sv.episodeId().equals( aEpisodeId ) ) {
        map.put( sv.cameraId(), sv );
      }
    }
    return map;
  }

  @Override
  public IStridablesList<ISourceVideo> listCameraSourceVideos( String aCameraId ) {
    TsNullArgumentRtException.checkNull( aCameraId );
    IStridablesListEdit<ISourceVideo> ll = new StridablesList<>();
    for( ISourceVideo sv : items() ) {
      if( sv.cameraId().equals( aCameraId ) ) {
        ll.add( sv );
      }
    }
    return ll;
  }

}
