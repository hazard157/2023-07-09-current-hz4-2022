package com.hazard157.psx24.timeline.main2;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.ftstep.*;

/**
 * Stripes painter component API for stripes implementations.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IStripeOwner {

  /**
   * Returns X coordinate of the second in episode.
   *
   * @param aSec int - second in episode
   * @return double - x coordinate in pixels
   * @throws TsIllegalArgumentRtException aSec < 0
   */
  double xCoor( int aSec );

  ESecondsStep timelineStep();

  EThumbSize getFrameThumbSize();

  int getDuration();

  ITsGuiContext tsContext();

}
