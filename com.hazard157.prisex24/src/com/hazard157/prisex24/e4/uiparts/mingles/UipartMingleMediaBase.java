package com.hazard157.prisex24.e4.uiparts.mingles;

import static com.hazard157.common.IHzConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.common.utils.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.e4.services.gazes.*;
import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.utils.*;
import com.hazard157.psx.proj3.mingle.*;

/**
 * Displays content of selected mingle media files of the specified kind.
 *
 * @author hazard157
 */
public class UipartMingleMediaBase
    extends PsxAbstractUipart
    implements ITsKeyInputListener {

  /**
   * Change panel content when current mingle changes.
   */
  private final ICurrentEntityChangeListener<IMingle> serviceSelectionChangeListener = this::showContentOfMingle;

  /**
   * Tooolbar listener calls {@link #processAction(String)}.
   */
  private final ITsActionHandler toolbarListener = this::processAction;

  /**
   * Update {@link #pgViewer} properties on app preferences change.
   */
  private final ITsCollectionChangeListener appSettingsChangeListener = ( aSource, aOp, aItem ) -> {
    EThumbSize oldSize = this.pgViewer.thumbSize();
    EThumbSize newSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    if( oldSize != newSize ) {
      this.pgViewer.setThumbSize( newSize );
      updateActionsState();
    }
  };

  private final ITsDoubleClickListener<OptedFile> doubleClickListener = ( aSource, aSelectedItem ) -> {
    playItem( aSelectedItem );
    updateActionsState();
  };

  private final ITsSelectionChangeListener<OptedFile> selectionListenet =
      ( aSource, aSelectedItem ) -> updateActionsState();

  @Inject
  ICurrentMingleService currentMingleService;

  final EIncidentMediaKind incidentMediaKind;

  TsToolbar                  toolbar;
  IPicsGridViewer<OptedFile> pgViewer;

  protected UipartMingleMediaBase( EIncidentMediaKind aMediaKind ) {
    incidentMediaKind = TsNullArgumentRtException.checkNull( aMediaKind );
  }

  @Override
  protected void doInit( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( appSettingsChangeListener );
    currentMingleService.addCurrentEntityChangeListener( serviceSelectionChangeListener );
    // toolbar
    toolbar = TsToolbar.create( board, tsContext(), EIconSize.IS_24X24, //
        ACDEF_PLAY, ACDEF_SEPARATOR, AI_THUMB_SIZEABLE_ZOOM_MENU //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // pgViewer
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    EThumbSize defThumbSize = APPREF_THUMB_SIZE_IN_GRIDS.defaultValue().asValobj();
    IPicsGridViewerConstants.OPDEF_DEFAULT_THUMB_SIZE.setValue( ctx.params(), avValobj( defThumbSize ) );
    IPicsGridViewerConstants.OPDEF_IS_LABELS_SHOWN.setValue( ctx.params(), AV_TRUE );
    IPicsGridViewerConstants.OPDEF_IS_TOOLTIPS_SHOWN.setValue( ctx.params(), AV_TRUE );
    pgViewer = new PicsGridViewer<>( board, ctx );
    pgViewer.setVisualsProvider( new MingleMediaFileVisualsProvider( ctx ) );
    pgViewer.setThumbSize( defThumbSize );
    pgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    pgViewer.addTsDoubleClickListener( doubleClickListener );
    pgViewer.addTsSelectionListener( selectionListenet );
    pgViewer.addTsKeyInputListener( this );
    // setup
    IMenuCreator mc1 = new ThumbSizeableDropDownMenuCreator( pgViewer, tsContext(), EIconSize.IS_24X24, EThumbSize.SZ96,
        EThumbSize.SZ512 );
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, mc1 );
    showContentOfMingle( currentMingleService.current() );
  }

  boolean processAction( String aActionId ) {
    OptedFile sel = pgViewer.selectedItem();
    switch( aActionId ) {
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        pgViewer.setThumbSize( pgViewer.defaultThumbSize() );
        break;
      }
      case ACTID_PLAY: {
        playItem( sel );
        break;
      }
      default:
        return false;
    }
    updateActionsState();
    return true;
  }

  void updateActionsState() {
    OptedFile sel = pgViewer.selectedItem();
    boolean isSel = sel != null;
    toolbar.setActionEnabled( ACTID_PLAY, isSel );
  }

  void showContentOfMingle( IMingle aMingle ) {
    if( aMingle == null ) {
      pgViewer.setItems( IList.EMPTY );
      return;
    }
    IList<OptedFile> ll = cofsMingles().listMediaFiles( aMingle.incidentDate(), incidentMediaKind );
    pgViewer.setItems( ll );
    updateActionsState();
  }

  void playItem( OptedFile aFile ) {
    if( aFile != null ) {
      HzUtils.runDefaultMediaApp( aFile.file() );
    }
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    OptedFile sel = pgViewer.selectedItem();
    switch( aCode ) {
      case SWT.CR:
      case SWT.KEYPAD_CR:
        playItem( sel );
        updateActionsState();
        break;
      default:
        return false;
    }
    return true;
  }

}
