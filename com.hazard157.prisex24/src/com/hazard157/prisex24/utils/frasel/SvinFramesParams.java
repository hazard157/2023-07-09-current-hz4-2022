package com.hazard157.prisex24.utils.frasel;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secstep.*;

/**
 * {@link ISvinFramesParams} implementation.
 *
 * @author hazard157
 */

public class SvinFramesParams
    implements ISvinFramesParams {

  private static final EAnimationKind DEFAULT_ANIMATION_KIND = EAnimationKind.ANIMATED;
  private static final ESecondsStep   DEFAULT_SECONDS_STEP   = ESecondsStep.SEC_10;

  private final GenericChangeEventer eventer;
  private final IStringListEdit      cameraIds = new StringArrayList();

  private EAnimationKind animationKind  = DEFAULT_ANIMATION_KIND;
  private boolean        onlySvinFrames = false;
  private ESecondsStep   secondsStep    = DEFAULT_SECONDS_STEP;
  private EFramesPerSvin framesPerSvin  = EFramesPerSvin.FORCE_ONE;

  /**
   * Constructor.
   */
  public SvinFramesParams() {
    eventer = new GenericChangeEventer( this );

  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeListener
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // ISvinFramesSelectionStrategy
  //

  @Override
  public EAnimationKind animationKind() {
    return animationKind;
  }

  @Override
  public void setAnimationKind( EAnimationKind aAnimationKind ) {
    TsNullArgumentRtException.checkNull( aAnimationKind );
    if( animationKind != aAnimationKind ) {
      animationKind = aAnimationKind;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public boolean isOnlySvinCams() {
    return onlySvinFrames;
  }

  @Override
  public void setOnlySvinCams( boolean aOnlySvinCams ) {
    if( onlySvinFrames != aOnlySvinCams ) {
      onlySvinFrames = aOnlySvinCams;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public IStringList cameraIds() {
    return cameraIds;
  }

  @Override
  public void setCameraIds( IStringList aCameraIds ) {
    TsNullArgumentRtException.checkNull( aCameraIds );
    if( !cameraIds.equals( aCameraIds ) ) {
      cameraIds.setAll( aCameraIds );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public ESecondsStep secondsStep() {
    return secondsStep;
  }

  @Override
  public void setSecondsStep( ESecondsStep aSecondsStep ) {
    TsNullArgumentRtException.checkNull( aSecondsStep );
    if( secondsStep != aSecondsStep ) {
      secondsStep = aSecondsStep;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public EFramesPerSvin framesPerSvin() {
    return framesPerSvin;
  }

  @Override
  public void setFramesPerSvin( EFramesPerSvin aFramesPerSvin ) {
    TsNullArgumentRtException.checkNull( aFramesPerSvin );
    if( framesPerSvin != aFramesPerSvin ) {
      framesPerSvin = aFramesPerSvin;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setParams( EAnimationKind aAnimationKind, Boolean aOnlySvinCams, IStringList aCameraIds,
      ESecondsStep aSecondsStep, EFramesPerSvin aFramesPerSvin ) {
    EAnimationKind newAk = aAnimationKind != null ? aAnimationKind : animationKind;
    boolean newSvinCams = aOnlySvinCams != null ? aOnlySvinCams.booleanValue() : onlySvinFrames;
    IStringList newCamIds = aCameraIds != null ? aCameraIds : cameraIds;
    ESecondsStep newSs = aSecondsStep != null ? aSecondsStep : secondsStep;
    EFramesPerSvin newFps = aFramesPerSvin != null ? aFramesPerSvin : framesPerSvin;
    if( newAk != animationKind || newFps != framesPerSvin || newSs != secondsStep || newSvinCams != isOnlySvinCams()
        || !newCamIds.equals( cameraIds ) ) {
      animationKind = newAk;
      onlySvinFrames = newSvinCams;
      cameraIds.setAll( aCameraIds );
      secondsStep = newSs;
      framesPerSvin = newFps;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setParams( ISvinFramesParams aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    setParams( aSource.animationKind(), Boolean.valueOf( aSource.isOnlySvinCams() ), aSource.cameraIds(),
        aSource.secondsStep(), aSource.framesPerSvin() );
  }

  // ------------------------------------------------------------------------------------
  // IAnimationKindable
  //

  @Override
  public EAnimationKind getDefaultAnimationKind() {
    return DEFAULT_ANIMATION_KIND;
  }

  @Override
  public EAnimationKind getShownAnimationKind() {
    return animationKind;
  }

  @Override
  public void setShownAnimationKind( EAnimationKind aAnimationKind ) {
    setAnimationKind( aAnimationKind );
  }

  // ------------------------------------------------------------------------------------
  // ISecondsSteppable
  //

  @Override
  public ESecondsStep getTimeStep() {
    return secondsStep;
  }

  @Override
  public void setTimeStep( ESecondsStep aStep ) {
    setSecondsStep( aStep );
  }

  @Override
  public ESecondsStep defaultTimeStep() {
    return DEFAULT_SECONDS_STEP;
  }

}
