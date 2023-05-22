package com.hazard157.psx24.timeline.main;

import org.toxsoft.core.tsgui.graphics.image.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.utils.ftstep.*;

/**
 * Timeline drawing canvas.
 *
 * @author hazard157
 */
public interface ITimelineCanvas
    extends IThumbSizeableEx, IFrameTimeSteppable {

  IEpisode getEpisode();

  void setEpisode( IEpisode aEpisode );

}
