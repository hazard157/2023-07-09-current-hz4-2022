package com.hazard157.prisex24.m5.frames;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Lifecycle manager for {@link FrameM5Model}.
 *
 * @author hazard157
 */
class FrameM5LifecycleManager
    extends M5LifecycleManager<IFrame, Object> {

  public FrameM5LifecycleManager( IM5Model<IFrame> aModel, Object aMaster ) {
    super( aModel, true, true, true, false, aMaster );
  }

  private static IFrame makeFrame( IM5Bunch<IFrame> aValues ) {
    String episodeId = FrameM5Model.EPISODE_ID.getFieldValue( aValues );
    String cameraId = FrameM5Model.CAM_ID.getFieldValue( aValues );
    int frameNo = FrameM5Model.FRAME_NO.getFieldValue( aValues ).asInt();
    boolean isAnimated = FrameM5Model.IS_ANIMATED.getFieldValue( aValues ).asBool();
    return new Frame( episodeId, cameraId, frameNo, isAnimated );
  }

  @Override
  protected IFrame doCreate( IM5Bunch<IFrame> aValues ) {
    return makeFrame( aValues );
  }

  @Override
  protected IFrame doEdit( IM5Bunch<IFrame> aValues ) {
    return makeFrame( aValues );
  }

  @Override
  protected void doRemove( IFrame aEntity ) {
    // nop
  }

}
