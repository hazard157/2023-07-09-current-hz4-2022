package com.hazard157.psx24.gazes.e4.uiparts;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.gazes.IPsx24GazesConstants.*;
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
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;

import com.hazard157.lib.core.glib.pgviewer.*;
import com.hazard157.lib.core.glib.pgviewer.impl.*;
import com.hazard157.lib.core.incub.optedfile.*;
import com.hazard157.lib.core.utils.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.gazes.e4.services.*;
import com.hazard157.psx24.gazes.utils.*;

/**
 * Displays content of selected gaze media files.
 *
 * @author hazard157
 */
public class UipartGazeMedia
    extends MwsAbstractPart
    implements IPsxStdReferences, ITsKeyInputListener {

  /**
   * Change panel contetn when current gaze changes.
   */
  private final ICurrentEntityChangeListener<IGaze> serviceSelectionChangeListener = this::showContentOfGaze;

  /**
   * Tooolbar listener calls {@link #processAction(String)}.
   */
  private final ITsActionHandler toolbarListener = this::processAction;

  /**
   * Update {@link #pgViewer} properties on app preferences change.
   */
  private final ITsCollectionChangeListener appSettingsChangeListener = ( aSource, aOp, aItem ) -> {
    String opId = (String)aItem;
    if( opId == null || opId.equals( APREF_MEDIA_THUMB_SIZE.id() ) ) {
      EThumbSize thumbSize = APREF_MEDIA_THUMB_SIZE.getValue( this.prefBundle.prefs() ).asValobj();
      this.pgViewer.setThumbSize( thumbSize );
    }
    updateActionsState();
  };

  private final ITsDoubleClickListener<OptedFile> doubleClickListener = ( aSource, aSelectedItem ) -> {
    playItem( aSelectedItem );
    updateActionsState();
  };

  private final ITsSelectionChangeListener<OptedFile> selectionListenet =
      ( aSource, aSelectedItem ) -> updateActionsState();

  @Inject
  ICurrentGazeService currentGazeService;

  @Named( value = PREFS_BUNDLE_ID_GAZES )
  @Inject
  IPrefBundle prefBundle;

  TsToolbar                  toolbar;
  IPicsGridViewer<OptedFile> pgViewer;

  @Override
  protected void doInit( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    prefBundle.prefs().addCollectionChangeListener( appSettingsChangeListener );
    currentGazeService.addCurrentEntityChangeListener( serviceSelectionChangeListener );
    // toolbar
    toolbar = TsToolbar.create( board, tsContext(), EIconSize.IS_24X24, //
        AI_PLAY, ACDEF_SEPARATOR, AI_THUMB_SIZEABLE_ZOOM_MENU //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // pgViewer
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    EThumbSize defThumbSize = APREF_MEDIA_THUMB_SIZE.defaultValue().asValobj();
    IPicsGridViewerConstants.OP_DEFAULT_THUMB_SIZE.setValue( ctx.params(), avValobj( defThumbSize ) );
    IPicsGridViewerConstants.OP_IS_LABEL2_SHOWN.setValue( ctx.params(), AV_FALSE );
    IPicsGridViewerConstants.OP_IS_LABELS_SHOWN.setValue( ctx.params(), AV_TRUE );
    IPicsGridViewerConstants.OP_IS_TOOLTIPS_SHOWN.setValue( ctx.params(), AV_TRUE );
    IPicsGridViewerConstants.OP_SELECTION_BORDER_COLOR.setValue( ctx.params(), avValobj( ETsColor.BLACK.rgb() ) );
    pgViewer = new PicsGridViewer<>( board, ctx );
    pgViewer.setVisualsProvider( new GazeMediaFileVisualsProvider( ctx ) );
    pgViewer.setThumbSize( APREF_MEDIA_THUMB_SIZE.getValue( prefBundle.prefs() ).asValobj() );
    pgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    pgViewer.addTsDoubleClickListener( doubleClickListener );
    pgViewer.addTsSelectionListener( selectionListenet );
    pgViewer.addTsKeyInputListener( this );
    // setup
    IMenuCreator mc1 = new ThumbSizeableDropDownMenuCreator( pgViewer, tsContext(), EIconSize.IS_24X24, EThumbSize.SZ96,
        EThumbSize.SZ724 );
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, mc1 );
  }

  boolean processAction( String aActionId ) {
    OptedFile sel = pgViewer.selectedItem();
    switch( aActionId ) {
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        pgViewer.setThumbSize( pgViewer.defaultThumbSize() );
        break;
      }
      case AID_PLAY: {
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
    toolbar.setActionEnabled( AID_PLAY, isSel );
  }

  void showContentOfGaze( IGaze aGaze ) {
    if( aGaze == null ) {
      pgViewer.setItems( IList.EMPTY );
      return;
    }
    IList<OptedFile> ll = fsOriginalMedia().listFiles( aGaze.incidentDate() );
    pgViewer.setItems( ll );
    updateActionsState();
  }

  void playItem( OptedFile aFile ) {
    if( aFile != null ) {
      PsxUtils.runDefaultApp( aFile.file() );
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
