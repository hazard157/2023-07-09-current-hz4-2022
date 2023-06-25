package com.hazard157.psx24.timeline.main;

import org.toxsoft.core.tsgui.graphics.image.*;

import com.hazard157.common.quants.secstep.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Timeline drawing canvas.
 *
 * @author hazard157
 */
public interface ITimelineCanvas
    extends IThumbSizeableEx, ISecondsSteppable {

  IEpisode getEpisode();

  void setEpisode( IEpisode aEpisode );

}
