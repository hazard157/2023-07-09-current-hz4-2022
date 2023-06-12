package com.hazard157.psx24.planning.glib;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx.proj3.songs.*;
import com.hazard157.psx24.planning.e4.services.*;

/**
 * Холст отрисовки планируемого эпизода.
 *
 * @author hazard157
 */
class PlepTimelineCanvas
    extends AbstractLazyPanel<Control> {

  private static final ETsColor STIR_BKG_NORM = ETsColor.CYAN;
  private static final ETsColor STIR_BKG_SEL  = ETsColor.GREEN;

  private static final ETsColor TRACK_BKG_NORM = ETsColor.YELLOW;
  private static final ETsColor TRACK_BKG_SEL  = ETsColor.GREEN;

  private static final int SMALL_TICK_SECS        = 30;
  private static final int TICK_TEXT_MAGRIN       = 3;
  private static final int STRIS_START_X          = 50;
  private static final int STIR_TEXT_X            = 5;
  private static final int DUR_TEXT_RIGHT_MARGIN  = 5;
  private static final int DUR_TEXT_BOTTOM_MARGIN = 3;
  private static final int STRIS_TRACK_DIST       = 20;
  private static final int TRACKS_START_X         = 320;
  private static final int TRACKS_WIDTH           = 150;
  private static final int AFTER_TRACKS_DELTA     = 20;
  private static final int CANVAS_WIDTH           = TRACKS_START_X + TRACKS_WIDTH + AFTER_TRACKS_DELTA;

  private static final int EXTRA_DURATION = 300; // свободный таймлайн ПОСЛЕ окончания плана

  private final IGenericChangeListener plepChangeListener = aSource -> whenPlepChanged();

  Canvas canvas = null;
  IPlep  plep   = null;

  double zoomFactor = 1.0;

  final ICurrentStirService  currentStirService;
  final ICurrentTrackService currentTrackService;

  public PlepTimelineCanvas( ITsGuiContext aContext ) {
    super( aContext );
    currentStirService = tsContext().get( ICurrentStirService.class );
    currentTrackService = tsContext().get( ICurrentTrackService.class );
    currentStirService.addCurrentEntityChangeListener( c -> refresh() );
    currentTrackService.addCurrentEntityChangeListener( c -> refresh() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void refresh() {
    if( canvas == null ) {
      return;
    }
    canvas.redraw();
  }

  int yCoor( int aY ) {
    return (int)(aY * zoomFactor);
  }

  /**
   * Рисует шкалу времени
   *
   * @param aGc {@link GC} - холст рисования
   */
  private void paintScale( GC aGc ) {
    aGc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
    int maxSecs = plep != null ? (calcTimelineDuration() + 60) : 120;
    Point tp = aGc.textExtent( "00:00" ); //$NON-NLS-1$
    int lastY = -2 * tp.y;
    for( int sec = 0; sec <= maxSecs; sec += SMALL_TICK_SECS ) {
      int y = yCoor( sec );
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

  private void paintStirs( GC aGc ) {
    if( plep == null ) {
      return;
    }
    IStir selStir = currentStirService.current();
    aGc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
    int startSec = 0;
    int x1 = STRIS_START_X;
    int width = TRACKS_START_X - STRIS_TRACK_DIST - STRIS_START_X;
    for( int i = 0; i < plep.stirs().size(); i++ ) {
      IStir stir = plep.stirs().get( i );
      // background
      int y1 = yCoor( startSec );
      int height = yCoor( stir.duration() ) - 2;
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
      int y1 = yCoor( startSec );
      int height = yCoor( track.duration() ) - 2;
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
    int height = yCoor( duration );
    if( height < 100 ) {
      height = 100;
    }
    canvas.setSize( CANVAS_WIDTH, height );
    canvas.getParent().layout( true, true );
    refresh();
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractE4LazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    canvas = new Canvas( aParent, SWT.BORDER );
    canvas.setBackground( colorManager().getColor( ETsColor.WHITE ) );
    canvas.addPaintListener( e -> paint( e.gc ) );
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

  public int calcTimelineDuration() {
    int duration = EXTRA_DURATION;
    if( plep != null ) {
      duration += plep.computeDuration();
    }
    return duration;
  }

  // public ITsSelectionProvider<IStir> stirSelectionProvider() {
  //
  // }

}
