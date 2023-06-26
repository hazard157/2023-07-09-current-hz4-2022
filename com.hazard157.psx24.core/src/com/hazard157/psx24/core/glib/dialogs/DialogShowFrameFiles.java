package com.hazard157.psx24.core.glib.dialogs;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.dialogs.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.utils.*;

/**
 * Adapts {@link DialogShowImageFiles} dialog for items of kind {@link IFrame}.
 *
 * @author hazard157
 */
public class DialogShowFrameFiles {

  /**
   * Invokes modal dialog to display images.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aFrame {@link IFrame} - initially selected frame or <code>null</code>
   * @param aFrames {@link IList}&lt;{@link IFrame}&gt; - frames list or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void show( ITsGuiContext aContext, IFrame aFrame, IList<IFrame> aFrames ) {
    TsNullArgumentRtException.checkNull( aContext );
    IList<IFrame> frames = aFrames != null ? aFrames : IList.EMPTY;
    IDialogShowImageFilesVisualsProvider<IFrame> vp = new FrameVisualsProvider( aContext );
    DialogShowImageFiles.showItems( aContext, frames, aFrame, vp );
  }

}
