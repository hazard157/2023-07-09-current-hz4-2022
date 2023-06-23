package com.hazard157.prisex24.glib.frview;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Single episode frames lister to view and select single frame.
 * <p>
 * Contains:
 * <ul>
 * <li>toolbar - on top, allows you to change display options and work with frames;</li>
 * <li>frame tables - on the left, contains a list of frames of episode {@link #getEpisodeId()};</li>
 * <li>frame preview - on the right, contains the image of the frame selected in the table.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IEpisodeFrameSelector
    extends ILazyControl<Control>, ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame> {

  /**
   * Returns the displayed frames episode.
   *
   * @return String - the episode ID or <code>null</code>
   */
  String getEpisodeId();

  /**
   * Sets the displayed frames episode.
   *
   * @param aEpisodeId String - the episode ID or <code>null</code>
   */
  void setEpisodeId( String aEpisodeId );

  /**
   * Returns the frames.
   * <p>
   * Really displayed frames may be subset of this list.
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - list of really displayed frames
   */
  IList<IFrame> listShownFrames();

}
