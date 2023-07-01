package com.hazard157.prisex24.glib.pleps;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.utils.TsMiscUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.e4.services.pleps.*;
import com.hazard157.psx.proj3.pleps.*;

/**
 * Vertical planned episode chart.
 *
 * @author hazard157
 */
public class PanelPlepTimeline
    extends TsPanel
    implements ITsUserInputListener {

  final ICurrentStirService  currentStirService;
  final ICurrentTrackService currentTrackService;

  private final ITsActionHandler toolbarListener = this::processAction;

  private final TsToolbar          toolbar;
  private final ScrolledComposite  scrollPanel;
  private final PlepTimelineCanvas ptc;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelPlepTimeline( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    currentStirService = tsContext().get( ICurrentStirService.class );
    currentTrackService = tsContext().get( ICurrentTrackService.class );
    // toolbar
    toolbar = TsToolbar.create( this, tsContext(), EIconSize.IS_24X24, //
        ACDEF_ZOOM_OUT, ACDEF_ZOOM_ORIGINAL_PUSHBUTTON, ACDEF_ZOOM_IN, ACDEF_ZOOM_FIT_HEIGHT_PUSHBUTTON //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // ptc
    scrollPanel = new ScrolledComposite( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
    // sc.setExpandHorizontal( true );
    // sc.setExpandVertical( true );
    scrollPanel.setLayoutData( BorderLayout.CENTER );
    ptc = new PlepTimelineCanvas( aContext );
    ptc.createControl( scrollPanel );
    scrollPanel.setContent( ptc.getControl() );
    ptc.addTsUserInputListener( this );
    // setup
    ptc.setCurrentPlep( null );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  void processAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ZOOM_IN:
        ptc.setZoomFactor( ptc.getZoomFactor() * 1.1 );
        break;
      case ACTID_ZOOM_ORIGINAL:
        ptc.setZoomFactor( 1.0 );
        break;
      case ACTID_ZOOM_OUT:
        ptc.setZoomFactor( ptc.getZoomFactor() / 1.1 );
        break;
      case ACTID_ZOOM_FIT_HEIGHT: {
        IPlep plep = ptc.getCurrentPlep();
        if( plep != null ) {
          double height = scrollPanel.getBounds().height;
          double duration = ptc.calcTimelineDuration();
          ptc.setZoomFactor( height / duration );
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  void updateActionsState() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  private IStir findStirBySec( int aSec ) {
    IPlep plep = ptc.getCurrentPlep();
    if( plep == null ) {
      return null;
    }
    if( aSec < 0 ) {
      return plep.stirs().first();
    }
    if( aSec == Integer.MAX_VALUE ) {
      return plep.stirs().last();
    }
    int end = 0;
    for( IStir s : plep.stirs() ) {
      end += s.duration();
      if( aSec < end ) {
        return s;
      }
    }
    return null;
  }

  private ITrack findTrackBySec( int aSec ) {
    IPlep plep = ptc.getCurrentPlep();
    if( plep == null ) {
      return null;
    }
    if( aSec < 0 ) {
      return plep.tracks().first();
    }
    if( aSec == Integer.MAX_VALUE ) {
      return plep.tracks().last();
    }
    int end = 0;
    for( ITrack s : plep.tracks() ) {
      end += s.duration();
      if( aSec < end ) {
        return s;
      }
    }
    return null;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    IPlep plep = ptc.getCurrentPlep();
    if( plep == null ) {
      return false;
    }
    int sec = ptc.y2sec( aCoors.y() );
    IStir stir = findStirBySec( sec );
    if( stir != null ) {
      currentStirService.setCurrent( stir );
    }
    ITrack track = findTrackBySec( sec );
    if( track != null ) {
      currentTrackService.setCurrent( track );
    }
    return true;
  }

  @Override
  public boolean onMouseWheel( Object aSource, int aState, ITsPoint aCoors, Control aWidget, int aScrollLines ) {
    int modifiers = aState & SWT.MODIFIER_MASK;
    // SHIFT+ wheel up/down -> zoom IN/OUT
    if( isBitsAll( modifiers, SWT.SHIFT ) ) {
      String zoomActId = TsLibUtils.EMPTY_STRING;
      if( aScrollLines > 0 ) {
        zoomActId = ACTID_ZOOM_IN;
      }
      if( aScrollLines < 0 ) {
        zoomActId = ACTID_ZOOM_OUT;
      }
      if( !zoomActId.isBlank() ) {
        processAction( zoomActId );
      }
      return true;
    }
    return false;
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
    ptc.setCurrentPlep( aPlep );
  }

}
