package com.hazard157.psx.common.stuff.svin;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;

/**
 * The episode videos part identification.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class Svin
    implements IEpisodeIdable, IEpisodeIntervalable, Comparable<Svin>, ICameraIdable, IFrameable {

  /**
   * Singleton of NONE object.
   */
  public static final Svin NULL = new Svin( EpisodeUtils.EPISODE_ID_NONE, IStridable.NONE_ID, Secint.MAXIMUM );

  private final String episodeId;
  private final String camId;
  private final Secint in;
  private final IFrame frame;

  /**
   * Создает экземпляр со всеми инвариантами.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aCamId String - идентификатор камеры или {@link IStridable#NONE_ID} для камеры по умолчанию
   * @param aInterval {@link Secint} - интервал эпизода или {@link Secint#MAXIMUM} для всего эпизода
   * @param aFrame {@link IFrame} - иллюстрирующие кадр или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валидный идентификатор эпизода
   * @throws TsIllegalArgumentRtException aCamId не ИД-путь
   * @throws TsIllegalArgumentRtException aFrame не от этого эпизода
   */
  public Svin( String aEpisodeId, String aCamId, Secint aInterval, IFrame aFrame ) {
    episodeId = EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    camId = StridUtils.checkValidIdPath( aCamId );
    if( aInterval == null || aFrame == null ) {
      throw new TsNullArgumentRtException();
    }
    in = aInterval;
    // если задан валидный кадр и валидный ИД эпизода, кадр должен быть от этого эпизода
    if( aFrame.isDefined() && !aEpisodeId.equals( EpisodeUtils.EPISODE_ID_NONE ) ) {
      TsIllegalArgumentRtException.checkFalse( aFrame.episodeId().equals( aEpisodeId ) );
    }
    frame = aFrame;
  }

  /**
   * Создает экземпляр.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aCamId String - идентификатор камеры или {@link IStridable#NONE_ID} для камеры по умолчанию
   * @param aInterval {@link Secint} - интервал эпизода или {@link Secint#MAXIMUM} для всего эпизода
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валидный идентификатор эпизода
   * @throws TsIllegalArgumentRtException aCamId не ИД-путь
   */
  public

  // private

  Svin( String aEpisodeId, String aCamId, Secint aInterval ) {
    this( aEpisodeId, aCamId, aInterval, IFrame.NONE );
  }

  /**
   * Создает экземпляр без указания камеры.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aInterval {@link Secint} - интервал эпизода или {@link Secint#MAXIMUM} для всего эпизода
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валидный идентификатор эпизода
   */
  public Svin( String aEpisodeId, Secint aInterval ) {
    this( aEpisodeId, IStridable.NONE_ID, aInterval, IFrame.NONE );
  }

  /**
   * Создает экземпляр для всего эпизода.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валидный идентификатор эпизода
   */
  public Svin( String aEpisodeId ) {
    this( aEpisodeId, IStridable.NONE_ID, Secint.MAXIMUM, IFrame.NONE );
  }

  /**
   * Создает копию части с убранным идентификатором камеры.
   *
   * @param aSvin {@link Svin} - исходная часть
   * @return {@link Svin} - созданная часть
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static Svin removeCamId( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    if( !aSvin.hasCam() ) {
      return aSvin;
    }
    return new Svin( aSvin.episodeId, IStridable.NONE_ID, aSvin.in );
  }

  /**
   * Создает копию части с заменой идентификатора камеры.
   *
   * @param aSvin {@link Svin} - исходная часть
   * @param aCamId String - новый идентификатор камеры
   * @return {@link Svin} - созданная часть
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public static Svin replaceCamId( Svin aSvin, String aCamId ) {
    TsNullArgumentRtException.checkNull( aSvin );
    StridUtils.checkValidIdPath( aCamId );
    if( aSvin.cameraId().equals( aCamId ) ) {
      return aSvin;
    }
    return new Svin( aSvin.episodeId, aCamId, aSvin.in );
  }

  // ------------------------------------------------------------------------------------
  // IEpisodeIdable
  //

  @Override
  public String episodeId() {
    return episodeId;
  }

  // ------------------------------------------------------------------------------------
  // ICameraIdable
  //

  @Override
  public String cameraId() {
    return camId;
  }

  // ------------------------------------------------------------------------------------
  // IEpisodeIntervalable
  //

  @Override
  public Secint interval() {
    return in;
  }

  // ------------------------------------------------------------------------------------
  // IFrameable
  //

  @Override
  public IFrame frame() {
    return frame;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, что интервал охватывает весь эпизод, то есть {@link #interval()} равен {@link Secint#MAXIMUM}.
   *
   * @return boolean - признак, что интервал охватывает весь эпизод
   */
  public boolean isWholeEpisode() {
    return in == Secint.MAXIMUM;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return String.format( "Svin: %s - %s %s", episodeId, camId, in == Secint.MAXIMUM ? "[all]" : in.toString() );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof Svin that ) {
      return episodeId.equals( that.episodeId ) && camId.equals( that.camId ) && in.equals( that.in );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + episodeId.hashCode();
    result = TsLibUtils.PRIME * result + camId.hashCode();
    result = TsLibUtils.PRIME * result + in.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( Svin aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    int c = episodeId.compareTo( aThat.episodeId );
    if( c != 0 ) {
      return c;
    }
    c = TsLibUtils.compare( in, aThat.in );
    if( c != 0 ) {
      return c;
    }
    return TsLibUtils.compare( camId, aThat.camId );
  }

}
