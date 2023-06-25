package com.hazard157.psx24.core.glib.frlstviewer_any;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secstep.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Panel shows one episode frames in specified intervals.
 * <p>
 * Intervals are set using methods {@link #setSvin(Svin)} or {@link #setSvins(IList)}.
 * <p>
 * Panel has toolbar with following functions:
 * <ul>
 * <li><b>Zoom</b> buttons - controls displayed thumbnails size;</li>
 * <li><b>Camera</b> selector - may be shown frames from all or any subset of episode cameras;</li>
 * <li><b>Animation</b> control allows to show animated, still or all frames;</li>
 * <li><b>Density</b> controls - how many seconds per show frames ({@link ESecondsStep});</li>
 * <li><b>Play</b> button to play short clip ear selected frame.</li>
 * </ul>
 * Note: only seconds boundary related still frames are shown by this panel.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPanelSvinFramesViewer
    extends ILazyControl<Control>, ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame>, IThumbSizeableEx,
    ISecondsSteppable {

  IList<Svin> getSvins();

  void setSvin( Svin aSvin );

  void setSvins( IList<Svin> aSvins );

  EAnimationKind getShownAnimationKind();

  void setShownAnimationKind( EAnimationKind aAnimationKind );

  IStringList getShownCameraIds();

  void setShownCameraIds( IStringList aCameraIds );

  boolean isOnlySvinCamsShown();

  void setOnlySvinCamsShown( boolean aSvinCams );

}
