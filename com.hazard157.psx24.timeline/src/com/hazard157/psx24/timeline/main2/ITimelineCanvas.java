package com.hazard157.psx24.timeline.main2;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;

import com.hazard157.common.quants.secstep.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Timeline drawing canvas.
 * <p>
 * Canvas has fixed length, depending on frame image size, episode duration and step seconds.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITimelineCanvas
    extends ILazyControl<Control>, ISecondsSteppable {

  IEpisode getEpisode();

  void setEpisode( IEpisode aEpisode );

}
