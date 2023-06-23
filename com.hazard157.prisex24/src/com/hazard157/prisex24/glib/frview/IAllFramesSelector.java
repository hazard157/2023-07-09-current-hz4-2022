package com.hazard157.prisex24.glib.frview;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.panels.lazy.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * All episodes all frames frames lister to view and select single frame.
 * <p>
 * Contains:
 * <ul>
 * <li>episodes list - at the left, allows to select episode;</li>
 * <li>{@link IEpisodeFrameSelector} - on the right, listing frames of the selected episode.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IAllFramesSelector
    extends ILazyControl<Control>, ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame> {

  // nop

}
