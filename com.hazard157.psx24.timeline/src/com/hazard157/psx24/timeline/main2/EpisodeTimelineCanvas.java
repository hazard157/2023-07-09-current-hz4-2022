package com.hazard157.psx24.timeline.main2;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secstep.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.timeline.main2.stripes.*;

/**
 * Canvas to draw episode timeline.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
class EpisodeTimelineCanvas
    extends AbstractLazyPanel<Control>
    implements IThumbSizeableEx, ISecondsSteppable, ITsUserInputListener {

  private static final int DEFAULT_NULL_EPISODE_DURATION_SECS = 3 * 60;
  private static final int MIN_DURATION_SECS                  = 2 * 60;
  private static final int COLUMN_0_WIDTH                     = 120;
  // private static final int INTERFRAME_GAP_PIXELS = 4;

  // ------------------------------------------------------------------------------------
  // frames thimb size
  private static final EThumbSize    DEFAULT_FRAME_THUMB_SIZE = EThumbSize.SZ128;
  private EThumbSize                 frameThumbSize           = DEFAULT_FRAME_THUMB_SIZE;
  private final GenericChangeEventer frameThumbSizeEventer;

  // ------------------------------------------------------------------------------------
  // time step
  private static final ESecondsStep DEFAULT_FRAME_TIME_STEP = ESecondsStep.SEC_20;
  private ESecondsStep              step                    = DEFAULT_FRAME_TIME_STEP;

  private final IStridablesListEdit<AbstractStripe> stripes = new StridablesList<>();

  private IEpisode episode = null;

  Canvas canvas = null;

  private final IStripeOwner stripeOwner = new IStripeOwner() {

    @Override
    public double xCoor( int aSec ) {
      double x = COLUMN_0_WIDTH;
      double pixelsPerSec = (frameThumbSize.size()) / ((double)step.stepSecs());
      return x + aSec * pixelsPerSec;
    }

    @Override
    public ITsGuiContext tsContext() {
      return EpisodeTimelineCanvas.this.tsContext();
    }

    @Override
    public ESecondsStep timelineStep() {
      return EpisodeTimelineCanvas.this.getTimeStep();
    }

    @Override
    public EThumbSize getFrameThumbSize() {
      return EpisodeTimelineCanvas.this.getFrameThumbSize();
    }

    @Override
    public int getDuration() {
      return EpisodeTimelineCanvas.this.getDuration();
    }
  };

  public EpisodeTimelineCanvas( ITsGuiContext aContext ) {
    super( aContext );
    frameThumbSizeEventer = new GenericChangeEventer( this );
    clearCanvas();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void clearCanvas() {
    stripes.clear();
    AbstractStripe s = new RangeScale( "range1", IOptionSet.NULL );
    stripes.add( s );
    s.initialize( stripeOwner );
    s = new ScaleStripe( "scale1", IOptionSet.NULL );
    stripes.add( s );
    s.initialize( stripeOwner );
    // TODO TimelineCanvas.setEpisode()
  }

  void refresh() {
    if( canvas == null ) {
      return;
    }
    // FIXME recalculate
    int duration = getDuration();
    // int framesCount = step.countStepsInDuration( duration ); // >= 1
    int width = (int)stripeOwner.xCoor( duration );
    // TODO EpisodeTimelineCanvas.refresh()
    int height = internalCalcHeight();
    canvas.setSize( width, height );

    canvas.redraw();
  }

  int getDuration() {
    int duration = DEFAULT_NULL_EPISODE_DURATION_SECS;
    if( episode != null ) {
      duration = episode.duration();
    }
    if( duration < MIN_DURATION_SECS ) {
      duration = MIN_DURATION_SECS;
    }
    return duration;
  }

  private static final int MIN_HEIGHT = 128;

  private int internalCalcHeight() {
    int height = 0;
    for( AbstractStripe r : stripes ) {
      height += r.getHeight();
    }
    if( height < MIN_HEIGHT ) {
      height = MIN_HEIGHT;
    }
    return height;
  }

  void paint( GC aGc ) {
    int x = 0;
    int y = 0;
    int startSec = 0;
    int endSec = getDuration() - 1;
    Point cnvSize = canvas.getSize();
    for( AbstractStripe s : stripes ) {
      int rowHeight = s.getHeight();
      TsRectangle flowArea = new TsRectangle( x + COLUMN_0_WIDTH, y, cnvSize.x - COLUMN_0_WIDTH, rowHeight );
      TsRectangle titleArea = new TsRectangle( x, y, COLUMN_0_WIDTH, rowHeight );
      s.paint( aGc, flowArea, titleArea, startSec, endSec );
      // aGc.setClipping( new Rectangle( flowArea.x1(), flowArea.y1(), flowArea.width(), flowArea.height() ) );
      // s.doPaintFlow( aGc, flowArea, startSec, endSec );
      // aGc.setClipping( new Rectangle( titleArea.x1(), titleArea.y1(), titleArea.width(), titleArea.height() ) );
      // s.doPaintTitle( aGc, titleArea, startSec, endSec );

      y += rowHeight;
    }
  }

  @SuppressWarnings( "unused" )
  AbstractStripe findAtCoors( int aMouseX, int aMouseY ) {

    // TODO EpisodeTimelineCanvas.findAtCoors()

    return null;
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractE4LazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    canvas = new Canvas( aParent, SWT.BORDER );
    canvas.setBackground( colorManager().getColor( ETsColor.WHITE ) );
    canvas.addPaintListener( e -> paint( e.gc ) );
    // canvas.addListener( SWT.Resize, new Listener() {
    //
    // @Override
    // public void handleEvent( Event aEvent ) {
    // Rectangle bounds = canvas.getBounds();
    // TsTestUtils.pl( " Canvas Resize: %s", bounds.toString() );
    // }
    // } );
    // canvas.addListener( SWT.Move, new Listener() {
    //
    // @Override
    // public void handleEvent( Event aEvent ) {
    // Rectangle bounds = canvas.getBounds();
    // TsTestUtils.pl( " Canvas Move: %s", bounds.toString() );
    // }
    // } );
    canvas.addMouseMoveListener( aE -> {
      AbstractStripe s = findAtCoors( aE.x, aE.y );
      if( s != null ) {
        // TODO Auto-generated method stub
      }
    } );
    refresh();
    return canvas;
  }

  // ------------------------------------------------------------------------------------
  // ITimeSteppable
  //

  @Override
  public ESecondsStep getTimeStep() {
    return step;
  }

  @Override
  public void setTimeStep( ESecondsStep aStep ) {
    TsNullArgumentRtException.checkNull( aStep );
    if( step != aStep ) {
      step = aStep;
      refresh();
    }
  }

  @Override
  public ESecondsStep defaultTimeStep() {
    return DEFAULT_FRAME_TIME_STEP;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public IEpisode getEpisode() {
    return episode;
  }

  public EThumbSize getFrameThumbSize() {
    return frameThumbSize;
  }

  public void setFrameThumbSize( EThumbSize aFrameThumbSize ) {
    TsNullArgumentRtException.checkNull( aFrameThumbSize );
    if( frameThumbSize != aFrameThumbSize ) {
      frameThumbSize = aFrameThumbSize;
      refresh();
    }
  }

  public void setEpisode( IEpisode aEpisode ) {
    if( Objects.equals( episode, aEpisode ) ) {
      return;
    }
    clearCanvas();
    episode = aEpisode;
    if( episode != null ) {
      IUnitSourceVideos usv = tsContext().get( IUnitSourceVideos.class );
      IStringMap<ISourceVideo> svMap = usv.episodeSourceVideos( episode.id() );
      for( ISourceVideo sv : svMap ) {
        AbstractStripe s = new SourceVideoStripe( sv, IOptionSet.NULL );
        stripes.add( s );
        s.initialize( stripeOwner );
      }
    }
    refresh();
  }

  // ширина вспомогательных областей, всех кроме области полос
  public int getWidthOfHelperAreas() {
    return COLUMN_0_WIDTH;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return getFrameThumbSize();
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( getFrameThumbSize() != aThumbSize ) {
      setFrameThumbSize( aThumbSize );
      frameThumbSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return DEFAULT_FRAME_THUMB_SIZE;
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return frameThumbSizeEventer;
  }

}
