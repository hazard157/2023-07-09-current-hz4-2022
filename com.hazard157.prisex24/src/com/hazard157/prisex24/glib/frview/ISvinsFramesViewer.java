package com.hazard157.prisex24.glib.frview;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.widgets.*;

import com.hazard157.prisex24.glib.frasel.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Displays frames from the specified SVINs.
 *
 * @author hazard157
 */
public interface ISvinsFramesViewer
    extends ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame>, IThumbSizeChangeCapable {

  /**
   * Returns the editable SVINs used to display frames.
   *
   * @return {@link ISvinSeqEdit} - SVINs used as "input" to display frames
   */
  ISvinSeqEdit svinSeq();

  /**
   * Returns the customizable frames selection strategy from the SVINs.
   *
   * @return {@link ISvinFramesParams} - SVIN to frames strategy
   */
  ISvinFramesParams svinFramesParams();

  /**
   * Determines what labels are under frames.
   *
   * @see PgvVisualsProviderFrame#LF_DATE
   * @see PgvVisualsProviderFrame#LF_HMS
   * @see PgvVisualsProviderFrame#LF_CAM
   * @return int - ORed bits {@link PgvVisualsProviderFrame}<code>.LF_XXX</code>
   */
  int getDisplayedInfoFlags();

  /**
   * Sets what labels will be shown under frames.
   *
   * @param aPgvLfFlags int - ORed bits {@link PgvVisualsProviderFrame}<code>.LF_XXX</code>
   */
  void setDisplayedInfoFlags( int aPgvLfFlags );

  /**
   * Returns viewer implementing SWT control.
   *
   * @return {@link TsComposite} - the SWT implementation
   */
  TsComposite getControl();

}
