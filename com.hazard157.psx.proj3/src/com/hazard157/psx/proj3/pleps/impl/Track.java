package com.hazard157.psx.proj3.pleps.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.proj3.pleps.*;

/**
 * Неизменяемая реализация {@link ITrack}.
 *
 * @author hazard157
 */
class Track
    implements ITrack {

  /**
   * Синглтон кипера.
   */
  public static final IEntityKeeper<ITrack> KEEPER =
      new AbstractEntityKeeper<>( ITrack.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITrack aEntity ) {
          aSw.writeAsIs( aEntity.songId() );
          aSw.writeSeparatorChar();
          Secint.KEEPER.write( aSw, aEntity.interval() );
        }

        @Override
        protected ITrack doRead( IStrioReader aSr ) {
          String songId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          Secint interval = Secint.KEEPER.read( aSr );
          return new Track( songId, interval );
        }
      };

  private final String songId;
  private final Secint interval;

  private IPlep plep = null;

  /**
   * Конструктор.
   *
   * @param aSongId String - идентификатор песни
   * @param aInterval Secint - интервал внутри песни
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public Track( String aSongId, Secint aInterval ) {
    songId = StridUtils.checkValidIdPath( aSongId );
    interval = TsNullArgumentRtException.checkNull( aInterval );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  void setPlep( IPlep aPlep ) {
    plep = aPlep;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITrack
  //

  @Override
  public String songId() {
    return songId;
  }

  @Override
  public Secint interval() {
    return interval;
  }

  @Override
  public IPlep plep() {
    TsIllegalStateRtException.checkNull( plep );
    return plep;
  }

  @Override
  public Secint getIntervalInPlep() {
    int start = 0;
    for( ITrack t : plep().tracks() ) {
      if( t == this ) {
        return new Secint( start, start + duration() - 1 );
      }
      start += t.duration();
    }
    throw new TsInternalErrorRtException();
  }

}
