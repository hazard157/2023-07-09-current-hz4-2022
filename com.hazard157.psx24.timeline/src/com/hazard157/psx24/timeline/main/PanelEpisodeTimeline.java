package com.hazard157.psx24.timeline.main;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.utils.ftstep.FrameTimeSteppableDropDownMenuCreator.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.ftstep.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.utils.ftstep.*;

/**
 * Episode timeline panel.
 *
 * @author hazard157
 */
public class PanelEpisodeTimeline
    extends TsPanel
    implements ITsActionHandler {

  private final TsToolbar       toolbar;
  private final ITimelineCanvas timelineCanvas;

  private IEpisode episode = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelEpisodeTimeline( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    // toolbar
    toolbar = TsToolbar.create( this, tsContext(), EIconSize.IS_24X24, //
        AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU, //
        AI_THUMB_SIZEABLE_ZOOM_MENU //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( this );
    // TODO timeline panel
    timelineCanvas = null; // FIXME null
    // setup
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, new ThumbSizeableDropDownMenuCreator( timelineCanvas,
        tsContext(), EIconSize.IS_16X16, PSX_MIN_FRAME_THUMB_SIZE, PSX_MAX_FRAME_THUMB_SIZE ) );
    FrameTimeSteppableDropDownMenuCreator ftsMc =
        new FrameTimeSteppableDropDownMenuCreator( timelineCanvas, tsContext(), EIconSize.IS_16X16 ) {

          @Override
          public void doSetFitSize( IFrameTimeSteppable aSubject ) {
            handleAction( AID_FRAME_TIME_STEPPABLE_ZOOM_FIT );
          }
        };
    ftsMc.setFitMenuItem( true );
    toolbar.setActionMenu( AID_FRAME_TIME_STEPPABLE_ZOOM_FIT, ftsMc );
    timelineCanvas.setEpisode( null );
    updateActionsState();

  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    // TODO wait cursor!
    // ITsCursorManager cursorManager = appContext().get( ITsCursorManager.class );
    switch( aActionId ) {
      case AID_FRAME_TIME_STEPPABLE_ZOOM_FIT:
      case ACTID_ZOOM_FIT_BEST:
      case ACTID_ZOOM_FIT_WIDTH: {
        // TODO PanelEpisodeTimeline.handleAction()
        TsDialogUtils.underDevelopment( getShell() );
        break;
      }
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        timelineCanvas.setThumbSize( timelineCanvas.defaultThumbSize() );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  void updateActionsState() {
    boolean isAlive = episode != null;
    ESecondsStep step = timelineCanvas.getFrameTimeStep();
    toolbar.setActionEnabled( ACTID_ZOOM_IN, !step.isMaxZoomIn() );
    toolbar.setActionEnabled( ACTID_ZOOM_OUT, !step.isMaxZoomOut() );
    toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL, isAlive && step != timelineCanvas.defaultFrameTimeStep() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the displayed episode.
   *
   * @return {@link IEpisode} - the displayed episode or <code>null</code>
   */
  public IEpisode getEpisode() {
    return episode;
  }

  /**
   * Sets the episode to view/edit.
   *
   * @param aEpisode {@link IEpisode} - the episode or <code>null</code>
   */
  public void setEpisode( IEpisode aEpisode ) {
    if( episode != aEpisode ) {
      episode = aEpisode;
      // TODO PanelEpisodeTimeline.setEpisode()
    }
  }

}
