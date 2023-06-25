package com.hazard157.psx.common.stuff.sc;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.ftstep.*;

/**
 * {@link ISingleCriterion} implementation.
 *
 * @author hazard157
 */
public class SingleCriterion
    implements ISingleCriterion {

  private final String         episodeId;
  private final String         cameraId;
  private final Secint         interval;
  private final EAnimationKind kind;
  private final boolean        onlySecAligned;
  private final ESecondsStep   timeStep;

  private SingleCriterion( String aEpisodeId, String aCameraId, Secint aInterval, EAnimationKind aKind,
      boolean aSecAlign, ESecondsStep aTimeStep ) {
    episodeId = aEpisodeId;
    cameraId = aCameraId;
    interval = aInterval;
    kind = aKind;
    onlySecAligned = aSecAlign;
    timeStep = aTimeStep;
  }

  /**
   * Creates criterion.
   *
   * @param aEpisodeId String - valid episode ID
   * @param aCameraId String - camera ID, IDpat or an empty string
   * @param aInterval {@link Secint} - episode interval
   * @param aKind {@link EAnimationKind} - kind of frames to select
   * @param aTimeStep {@link ESecondsStep} - time step between selected frames
   * @return {@link ISingleCriterion} - created criterion
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid episode or camera ID
   */
  public static ISingleCriterion createSecAligned( String aEpisodeId, String aCameraId, Secint aInterval,
      EAnimationKind aKind, ESecondsStep aTimeStep ) {
    StridUtils.checkValidIdName( aEpisodeId );
    TsNullArgumentRtException.checkNulls( aCameraId, aInterval, aKind, aTimeStep );
    if( !aCameraId.isEmpty() ) {
      StridUtils.checkValidIdPath( aCameraId );
    }
    return new SingleCriterion( aEpisodeId, aCameraId, aInterval, aKind, true, aTimeStep );
  }

  /**
   * Creates criterion.
   *
   * @param aSvin {@link Svin} - episode interval with optional camera ID
   * @param aKind {@link EAnimationKind} - kind of frames to select
   * @param aTimeStep {@link ESecondsStep} - time step between selected frames
   * @return {@link ISingleCriterion} - created criterion
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ISingleCriterion createSecAligned( Svin aSvin, EAnimationKind aKind, ESecondsStep aTimeStep ) {
    TsNullArgumentRtException.checkNulls( aSvin, aKind, aTimeStep );
    return new SingleCriterion( aSvin.episodeId(), aSvin.cameraId(), aSvin.interval(), aKind, true, aTimeStep );
  }

  /**
   * Creates criterion for all animated frames in interval.
   *
   * @param aSvin {@link Svin} - episode interval with optional camera ID
   * @return {@link ISingleCriterion} - created criterion
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ISingleCriterion createAnims( Svin aSvin ) {
    TsNullArgumentRtException.checkNulls( aSvin );
    return new SingleCriterion( aSvin.episodeId(), aSvin.cameraId(), aSvin.interval(), EAnimationKind.ANIMATED, true,
        ESecondsStep.SEC_01 );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( episodeId );
    sb.append( ',' );
    sb.append( cameraId );
    sb.append( ',' );
    sb.append( interval.toString() );
    sb.append( ',' );
    sb.append( kind.id() );
    sb.append( ',' );
    sb.append( onlySecAligned ? "SEC-aligned" : "not-aligned" ); //$NON-NLS-1$ //$NON-NLS-2$
    sb.append( ',' );
    sb.append( timeStep.id() );
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsFilter
  //

  @Override
  public boolean accept( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    // самая быстрая проверка, на анимированность
    if( !kind.accept( aFrame.isAnimated() ) ) {
      return false;
    }
    // в первую очередь проверим, что кадр в заданном интервале
    if( !interval.contains( aFrame.secNo() ) ) {
      return false;
    }
    // проверим, что кадр от заданного эпизода
    if( !episodeId.equals( aFrame.episodeId() ) ) {
      return false;
    }
    // проверим, что подходит любая камера или кадр от запрошенной камеры
    if( !cameraId.isEmpty() && cameraId.equals( aFrame.cameraId() ) ) {
      return false;
    }
    // анимированные кадры не проверяются на выровненность по границе секунд
    if( aFrame.isAnimated() ) {
      return kind.isAnimated();
    }
    // если нужно, проверим что кадр на границе нужной секунды
    if( onlySecAligned ) {
      if( aFrame.isSecAligned() ) {
        if( (aFrame.secNo() % timeStep.stepSecs()) == 0 ) {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISingleCriterion
  //

  @Override
  public String episodeId() {
    return episodeId;
  }

  @Override
  public String cameraId() {
    return cameraId;
  }

  @Override
  public Secint interval() {
    return interval;
  }

  @Override
  public EAnimationKind kind() {
    return kind;
  }

  @Override
  public boolean isOnlySecAligned() {
    return onlySecAligned;
  }

  @Override
  public ESecondsStep timeStep() {
    return timeStep;
  }

}
