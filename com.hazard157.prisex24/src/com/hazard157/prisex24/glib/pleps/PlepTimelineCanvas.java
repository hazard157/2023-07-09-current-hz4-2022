package com.hazard157.prisex24.glib.pleps;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.prisex24.e4.services.pleps.*;
import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx.proj3.songs.*;

/**
 * The rendering canvas for the planned episode.
 *
 * @author hazard157
 */
class PlepTimelineCanvas
    extends AbstractLazyPanel<Control>
    implements ITsUserInputProducer {

  private static final ETsColor STIR_BKG_NORM = ETsColor.CYAN;
  private static final ETsColor STIR_BKG_SEL  = ETsColor.GREEN;

  private static final ETsColor TRACK_BKG_NORM = ETsColor.YELLOW;
  private static final ETsColor TRACK_BKG_SEL  = ETsColor.GREEN;

  private static final int SMALL_TICK_SECS        = 30;
  private static final int TICK_TEXT_MAGRIN       = 3;
  private static final int STIRS_START_X          = 50;
  private static final int STIR_TEXT_X            = 5;
  private static final int DUR_TEXT_RIGHT_MARGIN  = 5;
  private static final int DUR_TEXT_BOTTOM_MARGIN = 3;
  private static final int STRIS_TRACK_DIST_X     = 20; // gap after STRI and before TRACK
  private static final int TRACKS_START_X         = 320;
  private static final int TRACKS_WIDTH           = 150;
  private static final int AFTER_TRACKS_DELTA     = 20;

  private static final int CANVAS_WIDTH = TRACKS_START_X + TRACKS_WIDTH + AFTER_TRACKS_DELTA;

  private static final int EXTRA_DURATION = 300; // free timeline AFTER the end of the plan

  private final IGenericChangeListener plepChangeListener = aSource -> whenPlepChanged();

  Canvas canvas = null;
  IPlep  plep   = null;

  double zoomFactor = 1.0;

  final ICurrentStirService  currentStirService;
  final ICurrentTrackService currentTrackService;

  final TsUserInputEventsBinder userInputEventsBinder;

  public PlepTimelineCanvas( ITsGuiContext aContext ) {
    super( aContext );
    currentStirService = tsContext().get( ICurrentStirService.class );
    currentTrackService = tsContext().get( ICurrentTrackService.class );
    currentStirService.addCurrentEntityChangeListener( c -> whenSelectedStirChanges() );
    currentTrackService.addCurrentEntityChangeListener( c -> whenSelectedTrackChanges() );
    userInputEventsBinder = new TsUserInputEventsBinder( this );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Makes visible the specified second.
   * <p>
   * Out of range values are "fitten" in the plep range.
   *
   * @param aSecs int - the second
   */
  void revealSecond( int aSecs ) {
    if( plep == null ) {
      return;
    }
    int sec = aSecs;
    int dur = plep.computeDuration();
    if( sec >= dur ) {
      sec = dur - 1;
    }
    if( sec < 0 ) {
      sec = 0;
    }

    // TODO PlepTimelineCanvas.revealSecond()
  }

  void whenSelectedStirChanges() {
    if( canvas != null ) {
      IStir sel = currentStirService.current();
      if( sel != null ) {
        int sec = sel.getIntervalInPlep().middle();
        revealSecond( sec );
      }
      canvas.redraw();
    }
  }

  void whenSelectedTrackChanges() {
    if( canvas != null ) {
      ITrack sel = currentTrackService.current();
      if( sel != null ) {
        int sec = sel.getIntervalInPlep().middle();
        revealSecond( sec );
      }
      canvas.redraw();
    }
  }

  void refresh() {
    if( canvas == null ) {
      return;
    }
    canvas.redraw();
  }

  int sec2y( int aSec ) {
    return (int)(aSec * zoomFactor);
  }

  /**
   * Draws a time scale.
   *
   * @param aGc {@link GC} - the canvas
   */
  private void paintScale( GC aGc ) {
    aGc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
    int maxSecs = plep != null ? (calcTimelineDuration() + 60) : 120;
    Point tp = aGc.textExtent( "00:00" ); //$NON-NLS-1$
    int lastY = -2 * tp.y;
    for( int sec = 0; sec <= maxSecs; sec += SMALL_TICK_SECS ) {
      int y = sec2y( sec );
      if( y - lastY < tp.y ) {
        continue;
      }
      lastY = y;
      if( (sec % 60) == 0 ) { // минуты
        String s = HmsUtils.mmss( sec );
        aGc.drawText( s, TICK_TEXT_MAGRIN, y - tp.y / 2 );
        aGc.setLineStyle( SWT.LINE_SOLID );
        aGc.drawLine( 2 * TICK_TEXT_MAGRIN + tp.x, y, CANVAS_WIDTH, y );
      }
      else {
        if( (sec % SMALL_TICK_SECS) == 0 ) { // SMALL_TICK_SECS секунд
          String s = HmsUtils.mmss( sec );
          aGc.drawText( s, TICK_TEXT_MAGRIN, y - tp.y / 2 );
          aGc.setLineStyle( SWT.LINE_DOT );
          aGc.drawLine( 2 * TICK_TEXT_MAGRIN + tp.x, y, CANVAS_WIDTH, y );
        }
      }
    }
    aGc.setLineStyle( SWT.LINE_SOLID );
  }

  /**
   * Draws the STIRs.
   *
   * @param aGc {@link GC} - the canvas
   */
  private void paintStirs( GC aGc ) {
    if( plep == null ) {
      return;
    }
    IStir selStir = currentStirService.current();
    aGc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
    int startSec = 0;
    int x1 = STIRS_START_X;
    int width = TRACKS_START_X - STRIS_TRACK_DIST_X - STIRS_START_X;
    for( int i = 0; i < plep.stirs().size(); i++ ) {
      IStir stir = plep.stirs().get( i );
      // background
      int y1 = sec2y( startSec );
      int height = sec2y( stir.duration() ) - 2;
      ETsColor bkg = STIR_BKG_NORM;
      if( stir == selStir ) {
        bkg = STIR_BKG_SEL;
      }
      aGc.setBackground( colorManager().getColor( bkg ) );
      aGc.fillRectangle( x1, y1, width, height );
      // text
      Point tp = aGc.textExtent( stir.name() );
      String text = String.format( "%02d. %s", Integer.valueOf( i + 1 ), stir.name() ); //$NON-NLS-1$
      aGc.drawText( text, x1 + STIR_TEXT_X, y1 + (height - tp.y) / 2 );
      // duration text
      String durText = HmsUtils.mmmss( stir.duration() );
      tp = aGc.textExtent( durText );
      int dtX = x1 + width - DUR_TEXT_RIGHT_MARGIN - tp.x;
      int dtY = y1 + height - DUR_TEXT_BOTTOM_MARGIN - tp.y;
      aGc.drawText( durText, dtX, dtY );
      // outline
      aGc.drawRectangle( x1, y1, width, height );
      startSec += stir.duration();
    }
  }

  /**
   * Draws the TRACKs.
   *
   * @param aGc {@link GC} - the canvas
   */
  private void paintTracks( GC aGc ) {
    if( plep == null ) {
      return;
    }
    aGc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
    int startSec = 0;
    int x1 = TRACKS_START_X;
    int width = TRACKS_WIDTH;
    ITrack selTrack = currentTrackService.current();
    for( ITrack track : plep.tracks() ) {
      int y1 = sec2y( startSec );
      int height = sec2y( track.duration() ) - 2;
      // background
      ETsColor bkg = TRACK_BKG_NORM;
      if( track == selTrack ) {
        bkg = TRACK_BKG_SEL;
      }
      aGc.setBackground( colorManager().getColor( bkg ) );
      aGc.fillRectangle( x1, y1, width, height );
      // outline
      aGc.drawRectangle( x1, y1, width, height );
      // name
      String name = getSongName( track.songId() );
      Point tp = aGc.textExtent( name );
      aGc.drawText( name, x1 + (width - tp.x) / 2, y1 + (height - tp.y) / 2 );
      startSec += track.duration();
    }
  }

  private String getSongName( String aSongId ) {
    IUnitSongs unitSongs = tsContext().get( IUnitSongs.class );
    ISong song = unitSongs.items().findByKey( aSongId );
    if( song != null ) {
      return song.nmName();
    }
    return aSongId;
  }

  void paint( GC aGc ) {
    paintScale( aGc );
    paintStirs( aGc );
    paintTracks( aGc );
  }

  void whenPlepChanged() {
    int duration = calcTimelineDuration();
    int height = sec2y( duration );
    if( height < 100 ) {
      height = 100;
    }
    canvas.setSize( CANVAS_WIDTH, height );
    canvas.getParent().layout( true, true );
    refresh();
  }

  // ------------------------------------------------------------------------------------
  // AbstractE4LazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    canvas = new Canvas( aParent, SWT.BORDER );
    canvas.setBackground( colorManager().getColor( ETsColor.WHITE ) );
    canvas.addPaintListener( e -> paint( e.gc ) );
    userInputEventsBinder.bindToControl( canvas, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    refresh();
    return canvas;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает планируемый эпизод для просмотра/редактирования.у
   *
   * @param aPlep {@link IPlep} - планируемый эпизод или <code>null</code>
   */
  public void setCurrentPlep( IPlep aPlep ) {
    if( plep != null ) {
      plep.eventer().removeListener( plepChangeListener );
    }
    plep = aPlep;
    if( plep != null ) {
      plep.eventer().addListener( plepChangeListener );
      zoomFactor = 1.0;
    }
    whenPlepChanged();
    refresh();
  }

  public IPlep getCurrentPlep() {
    return plep;
  }

  public double getZoomFactor() {
    return zoomFactor;
  }

  public void setZoomFactor( double aZooomFactor ) {
    zoomFactor = aZooomFactor;
    if( aZooomFactor < 0.01 ) {
      zoomFactor = 0.01;
    }
    if( aZooomFactor > 100.0 ) {
      zoomFactor = 100.0;
    }
    whenPlepChanged();
    refresh();
  }

  /**
   * Returns the seconds value on vertical scale for the Y coordinate.
   * <p>
   * If YCoordinate is out of PLEP duration, returns either -1 or {@link Integer#MAX_VALUE}.
   *
   * @param aYCoor int - Y coordinate relative to the canvas widget
   * @return int - seconds of time or -1 or {@link Integer#MAX_VALUE}
   */
  public int y2sec( int aYCoor ) {
    int sec = (int)(aYCoor / zoomFactor);
    if( sec < 0 ) {
      return -1;
    }
    if( sec >= plep.computeDuration() ) {
      return Integer.MAX_VALUE;
    }
    return sec;
  }

  public int calcTimelineDuration() {
    int duration = EXTRA_DURATION;
    if( plep != null ) {
      duration += plep.computeDuration();
    }
    return duration;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputProducer
  //

  @Override
  public void addTsUserInputListener( ITsUserInputListener aListener ) {
    userInputEventsBinder.addTsUserInputListener( aListener );
  }

  @Override
  public void removeTsUserInputListener( ITsUserInputListener aListener ) {
    userInputEventsBinder.removeTsUserInputListener( aListener );
  }

}
