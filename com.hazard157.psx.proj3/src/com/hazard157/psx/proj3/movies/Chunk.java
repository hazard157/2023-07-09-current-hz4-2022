package com.hazard157.psx.proj3.movies;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;

/**
 * Кусок - непрерывная часть эпизода, снятая одной камерой, для использования в трейлерах и фильмах.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public final class Chunk
    implements IEpisodeIdable, ICameraIdable, IEpisodeIntervalable, IFrameable {

  private final String episodeId;
  private final String camId;
  private final String name;
  private final Secint in;
  private final IFrame frame;

  private final Svin svin;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aCamId String - идентификатор камеры
   * @param aName String - название куска
   * @param aIn {@link Secint} - интервал
   * @param aFrame {@link IFrame} - иллюстрирующий кадр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валдиный идентификатор эпизода
   * @throws TsIllegalArgumentRtException aCamId не ИД-путь
   */
  public Chunk( String aEpisodeId, String aCamId, String aName, Secint aIn, IFrame aFrame ) {
    episodeId = EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    camId = StridUtils.checkValidIdPath( aCamId );
    TsNullArgumentRtException.checkNulls( aIn, aFrame, aName );
    name = aName;
    in = aIn;
    frame = aFrame;
    svin = new Svin( aEpisodeId, aCamId, aIn, aFrame );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    return frame;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIdable
  //

  @Override
  public String episodeId() {
    return episodeId;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICameraIdable
  //

  @Override
  public String cameraId() {
    return camId;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIntervalable
  //

  @Override
  public Secint interval() {
    return in;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает название куска.
   *
   * @return String - название куска
   */
  public String name() {
    return name;
  }

  /**
   * Возвращает кусок как {@link Svin}.
   *
   * @return {@link Svin} - кусок как {@link Svin}
   */
  public Svin svin() {
    return svin;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return episodeId + ":" + camId + ":" + in.toString() + " - " + name;
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof Chunk that ) {
      return this.episodeId.equals( that.episodeId ) && this.camId.equals( that.camId ) && this.name.equals( that.name )
          && this.in.equals( that.in ) && this.frame.equals( that.frame );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + episodeId.hashCode();
    result = PRIME * result + camId.hashCode();
    result = PRIME * result + name.hashCode();
    result = PRIME * result + in.hashCode();
    result = PRIME * result + frame.hashCode();
    return result;
  }

}
