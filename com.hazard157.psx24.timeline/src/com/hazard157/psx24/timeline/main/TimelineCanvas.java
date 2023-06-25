package com.hazard157.psx24.timeline.main;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

import com.hazard157.common.quants.secstep.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * {@link ITimelineCanvas} implementation.
 *
 * @author hazard157
 */
public class TimelineCanvas
    extends TsAbstractCanvas
    implements ITimelineCanvas {

  private IEpisode episode = null;

  /**
   * Constructor with no style bits.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   */
  public TimelineCanvas( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // TsAbstractCanvas
  //

  @Override
  public void paint( GC aGc, ITsRectangle aPaintBounds ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    // TODO Auto-generated method stub

  }

  @Override
  public EThumbSize defaultThumbSize() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IFrameTimeSteppable
  //

  @Override
  public ESecondsStep getTimeStep() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setTimeStep( ESecondsStep aStep ) {
    // TODO Auto-generated method stub

  }

  @Override
  public ESecondsStep defaultTimeStep() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ITimelineCanvas
  //

  @Override
  public IEpisode getEpisode() {
    return episode;
  }

  @Override
  public void setEpisode( IEpisode aEpisode ) {
    if( episode == aEpisode ) {
      return;
    }
    episode = aEpisode;

    // TODO TimelineCanvas.setEpisode()

  }

}
