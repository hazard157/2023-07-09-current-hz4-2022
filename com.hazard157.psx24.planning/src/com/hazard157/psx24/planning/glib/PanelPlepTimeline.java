package com.hazard157.psx24.planning.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Вертикальный грфик плана.
 *
 * @author goga
 */
public class PanelPlepTimeline
    extends TsPanel {

  private final ITsActionHandler toolbarListener = this::processAction;

  private final TsToolbar          toolbar;
  private final ScrolledComposite  scrollPanel;
  private final PlepTimelineCanvas ptc;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelPlepTimeline( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
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
    // TODO PanelPlepTimeline.setCurrentPlep()
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
