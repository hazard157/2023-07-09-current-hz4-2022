package com.hazard157.psx.common.stuff.frame;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.*;

/**
 * Неизменяемая реализация {@link IFrame}.
 *
 * @author hazard157
 */
public class Frame
    implements IFrame {

  private final int     hashCode;
  private final String  episodeId;
  private final String  camId;
  private final int     frameNo;
  private final boolean isAnimated;

  private transient LocalDate date;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aEpisodeId String - идентификатор эпизода или {@link IStridable#NONE_ID}
   * @param aCamId String - идентификатор камеры или {@link IStridable#NONE_ID}
   * @param aFrameNo int - номер первого кадра или -1
   * @param aIsAnimated boolean - признак анимированной последовательности
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId невалидный идентификатор эпизода
   * @throws TsIllegalArgumentRtException aCamId не ИД-путь
   */
  public Frame( String aEpisodeId, String aCamId, int aFrameNo, boolean aIsAnimated ) {
    if( !StridUtils.isNullId( aEpisodeId ) ) {
      EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    }
    episodeId = aEpisodeId;
    camId = StridUtils.checkValidIdPath( aCamId );
    frameNo = aFrameNo;
    isAnimated = aIsAnimated;
    hashCode = calcHashCode();
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private int calcHashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + episodeId.hashCode();
    result = PRIME * result + cameraId().hashCode();
    result = PRIME * result + frameNo();
    return result;
  }

  /**
   * Возвращает удобочитаемую строку для идентификации кадра эпизода (например, в таблицах).
   *
   * @param aSf {@link IFrame} - описание значка
   * @return String - удобочитаемая строка для идентификации значка
   * @throws TsNullArgumentRtException аргумент = null
   */
  @SuppressWarnings( "nls" )
  public static String humanReadableString( IFrame aSf ) {
    TsNullArgumentRtException.checkNull( aSf );
    StringBuilder sb = new StringBuilder();
    sb.append( aSf.episodeId() );
    sb.append( ' ' );
    sb.append( HhMmSsFfUtils.mmssff( aSf.frameNo() ) );
    sb.append( ' ' );
    sb.append( aSf.isAnimated() ? "ANIM" : "SOLE" );
    sb.append( ' ' );
    sb.append( aSf.cameraId() );
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrame
  //

  @Override
  public String episodeId() {
    return episodeId;
  }

  @Override
  public LocalDate whenDate() {
    if( date == null ) {
      date = EpisodeUtils.episodeId2LocalDate( episodeId );
    }
    return date;
  }

  @Override
  public String cameraId() {
    return camId;
  }

  @Override
  public int frameNo() {
    return frameNo;
  }

  @Override
  public boolean isAnimated() {
    return isAnimated;
  }

  @Override
  public boolean isDefined() {
    return frameNo >= 0 && !episodeId.equals( IStridable.NONE_ID ) && !camId.equals( IStridable.NONE_ID );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof IFrame ) {
      IFrame that = (IFrame)aObj;
      return frameNo() == that.frameNo() && isAnimated == that.isAnimated() && cameraId().equals( that.cameraId() )
          && episodeId.equals( that.episodeId() );
    }
    return false;
  }

  @Override
  public String toString() {
    return humanReadableString( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( IFrame aThat ) {
    int c = this.episodeId().compareTo( aThat.episodeId() );
    if( c != 0 ) {
      return c;
    }
    c = Integer.compare( this.frameNo(), aThat.frameNo() );
    if( c != 0 ) {
      return c;
    }
    c = this.cameraId().compareTo( aThat.cameraId() );
    if( c != 0 ) {
      return c;
    }
    if( this.isAnimated() == aThat.isAnimated() ) {
      return 0;
    }
    return aThat.isAnimated() ? 1 : -1;
  }

}
