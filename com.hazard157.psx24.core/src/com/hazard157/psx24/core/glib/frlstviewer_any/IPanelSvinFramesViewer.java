package com.hazard157.psx24.core.glib.frlstviewer_any;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.ftstep.*;
import com.hazard157.psx24.core.utils.ftstep.*;

/**
 * Panel shows one episode frames in specified intervals.
 * <p>
 * Intervals are set using methods {@link #setSvin(Svin)} or {@link #setSvins(IList)}.
 * <p>
 * Panel has toolbar with following functions:
 * <ul>
 * <li><b>Zoom</b> buttons - controls displayed thumnails size;</li>
 * <li><b>Camera</b> selector - may be shown frames from all or any subset of episode cameras;</li>
 * <li><b>Animation</b> control allows to show animated, still or all frames;</li>
 * <li><b>Density</b> controls - how many secons per show frames ({@link ESecondsStep});</li>
 * <li><b>Play</b> button to play short clip ear selected frame.</li>
 * </ul>
 * Note: only seconds boundary related still frames are shown by this panel.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPanelSvinFramesViewer
    extends ILazyControl<Control>, ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame>, IThumbSizeableEx,
    IFrameTimeSteppable {

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
