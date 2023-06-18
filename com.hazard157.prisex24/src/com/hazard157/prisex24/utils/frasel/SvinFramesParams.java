package com.hazard157.prisex24.utils.frasel;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;

/**
 * {@link ISvinFramesParams} implementation.
 *
 * @author hazard157
 */

public class SvinFramesParams
    implements ISvinFramesParams {

  private final GenericChangeEventer eventer;
  private final IStringListEdit      cameraIds = new StringArrayList();

  private EAnimationKind animationKind  = EAnimationKind.ANIMATED;
  private boolean        onlySvinFrames = false;
  private EFramesPerSvin framesPerSvin  = EFramesPerSvin.SELECTED;

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
      EFramesPerSvin aFramesPerSvin ) {
    EAnimationKind newAk = aAnimationKind != null ? aAnimationKind : animationKind;
    boolean newSvinCams = aOnlySvinCams != null ? aOnlySvinCams.booleanValue() : onlySvinFrames;
    IStringList newCamIds = aCameraIds != null ? aCameraIds : cameraIds;
    EFramesPerSvin newFps = aFramesPerSvin != null ? aFramesPerSvin : framesPerSvin;
    if( newAk != animationKind || newFps != framesPerSvin || newSvinCams != isOnlySvinCams()
        || !newCamIds.equals( cameraIds ) ) {
      animationKind = newAk;
      onlySvinFrames = newSvinCams;
      cameraIds.setAll( aCameraIds );
      framesPerSvin = newFps;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setParams( ISvinFramesParams aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    setParams( aSource.animationKind(), Boolean.valueOf( aSource.isOnlySvinCams() ), aSource.cameraIds(),
        aSource.framesPerSvin() );
  }

  // ------------------------------------------------------------------------------------
  // IAnimationKindable
  //

  @Override
  public EAnimationKind getDefaultAnimationKind() {
    return EAnimationKind.ANIMATED;
  }

  @Override
  public EAnimationKind getShownAnimationKind() {
    return animationKind;
  }

  @Override
  public void setShownAnimationKind( EAnimationKind aAnimationKind ) {
    setAnimationKind( aAnimationKind );
  }

}
