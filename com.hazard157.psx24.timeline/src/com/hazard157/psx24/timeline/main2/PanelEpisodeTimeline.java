package com.hazard157.psx24.timeline.main2;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.utils.ftstep.FrameTimeSteppableDropDownMenuCreator.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
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
 * Вертикальный грфик плана.
 *
 * @author hazard157
 */
public class PanelEpisodeTimeline
    extends TsPanel {

  // ------------------------------------------------------------------------------------
  // frame thumb size drop-down menu
  // private static final int AID_FRAME_THUMB_SIZE = ACTID_FIRST_LOCAL_ID + 1;
  // private static final ITsActionInfo AI_FRAME_THUMB_SIZE = new TsActDropDownMenuInfo( AID_FRAME_THUMB_SIZE, //
  // STR_T_FRAME_THUMB_SIZE, STR_P_FRAME_THUMB_SIZE, ICON_PSX_IMAGE_STACK, null );

  private static final ESecondsStep ORIGINAL_ZOOM_TIMELINE_STEP = ESecondsStep.SEC_20;

  private final ITsActionHandler toolbarListener = this::processAction;

  private final TsToolbar             toolbar;
  private final ScrolledComposite     scrollPanel;
  private final EpisodeTimelineCanvas ptc;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
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
    toolbar.addListener( toolbarListener );
    // ptc
    scrollPanel = new ScrolledComposite( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
    // sc.setExpandHorizontal( true );
    // sc.setExpandVertical( true );
    scrollPanel.setLayoutData( BorderLayout.CENTER );
    ptc = new EpisodeTimelineCanvas( aContext );
    ptc.createControl( scrollPanel );
    scrollPanel.setContent( ptc.getControl() );
    // setup
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, new ThumbSizeableDropDownMenuCreator( ptc, tsContext(),
        EIconSize.IS_16X16, PSX_MIN_FRAME_THUMB_SIZE, PSX_MAX_FRAME_THUMB_SIZE ) );
    FrameTimeSteppableDropDownMenuCreator ftsMc =
        new FrameTimeSteppableDropDownMenuCreator( ptc, tsContext(), EIconSize.IS_16X16 ) {

          @Override
          public void doSetFitSize( IFrameTimeSteppable aSubject ) {
            processAction( AID_FRAME_TIME_STEPPABLE_ZOOM_FIT );
          }
        };
    ftsMc.setFitMenuItem( true );
    toolbar.setActionMenu( AID_FRAME_TIME_STEPPABLE_ZOOM_FIT, ftsMc );
    ptc.setFrameTimeStep( ORIGINAL_ZOOM_TIMELINE_STEP );
    ptc.setEpisode( null );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  ESecondsStep calcFitFrameTimeStep() {
    IEpisode ep = ptc.getEpisode();
    if( ep == null ) {
      return ptc.defaultFrameTimeStep();
    }
    double stripesAreaWidth = scrollPanel.getClientArea().width - ptc.getWidthOfHelperAreas();
    double duration = ep.duration();
    double bestPixelsPerSec = stripesAreaWidth / duration;
    double bestStepSecs = ptc.getFrameThumbSize().size() / bestPixelsPerSec;
    ESecondsStep bestStep = ESecondsStep.getFloor( (int)bestStepSecs );
    return bestStep;
  }

  void processAction( String aActionId ) {
    // TODO wait cursor!
    // ITsCursorManager cursorManager = appContext().get( ITsCursorManager.class );
    switch( aActionId ) {
      case AID_FRAME_TIME_STEPPABLE_ZOOM_FIT:
      case ACTID_ZOOM_FIT_BEST:
      case ACTID_ZOOM_FIT_WIDTH: {
        ESecondsStep bestStep = calcFitFrameTimeStep();
        ptc.setFrameTimeStep( bestStep );
        break;
      }
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        ptc.setThumbSize( ptc.defaultThumbSize() );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  void updateActionsState() {
    boolean isAlive = ptc.getEpisode() != null;
    ESecondsStep step = ptc.getFrameTimeStep();
    toolbar.setActionEnabled( ACTID_ZOOM_IN, !step.isMaxZoomIn() );
    toolbar.setActionEnabled( ACTID_ZOOM_OUT, !step.isMaxZoomOut() );
    toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL, isAlive && step != ORIGINAL_ZOOM_TIMELINE_STEP );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает эпизод для просмотра/редактирования.
   *
   * @param aEpisode {@link IEpisode} - эпизод или <code>null</code>
   */
  public void setEpisode( IEpisode aEpisode ) {
    ptc.setEpisode( aEpisode );
  }

}
