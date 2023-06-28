package com.hazard157.prisex24.glib.frview;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Displays frames as the grid.
 * <p>
 * Accepts {@link IPicsGridViewerConstants} options.
 *
 * @author hazard157
 */
public interface IFramesGridViewer
    extends ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame>, //
    IThumbSizeChangeCapable, IGenericChangeEventCapable, ITsGuiContextable {

  /**
   * TODO mouse and keyboard handler ???
   */

  /**
   * Lists the frames really displayed in grid, this is filtered subset of {@link #getFrames()}.
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - displayed frames list
   */
  IList<IFrame> listDisplayedFrames();

  /**
   * Returns the frames set by {@link #setFrames(IList)}.
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - the unfiltered frames list
   */
  IList<IFrame> getFrames();

  /**
   * Set frames to be filtered and displayed.
   *
   * @param aFrames {@link IList}&lt;{@link IFrame}&gt; - the frames list
   */
  void setFrames( IList<IFrame> aFrames );

  /**
   * Returns the currently used filter to select frames from {@link #getFrames()} to {@link #listDisplayedFrames()}.
   *
   * @return {@link ITsFilter}&lt;{@link IFrame}&gt; - current filter, never is <code>null</code>
   */
  ITsFilter<IFrame> getFrameFiler();

  /**
   * Sets filter {@link #getFrameFiler()}.
   *
   * @param aFilter {@link ITsFilter}&lt;{@link IFrame}&gt; - the filter, use {@link ITsFilter#ALL} for no filtering
   */
  void setFrameFilter( ITsFilter<IFrame> aFilter );

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
