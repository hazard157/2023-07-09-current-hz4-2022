package com.hazard157.psx24.timeline.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.utils.ftstep.*;

/**
 * Timeline drawing canvas.
 * <p>
 * Canvas has fixed length, depending on frame image size, episode duration and step seconds.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public interface ITimelineCanvas
    extends ILazyControl<Control>, IFrameTimeSteppable {

  IEpisode getEpisode();

  void setEpisode( IEpisode aEpisode );

}
