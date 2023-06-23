package com.hazard157.prisex24.glib.dialogs;

import static com.hazard157.prisex24.glib.dialogs.IPsxResources.*;
import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Dialog to show and work with frames around the specified frame.
 *
 * @author hazard157
 */
public class DialogWorkWithFrames
    extends AbstractTsDialogPanel<IFrame, IEclipseContext> {

  static final String SETTINGS_NODE_ID = "DialogWorkWithFrames.FramesListViewer"; //$NON-NLS-1$

  final IEpisodeFrameSelector framesViewer;

  DialogWorkWithFrames( Composite aParent, TsDialog<IFrame, IEclipseContext> aOwnerDialog, String aEpisodeId ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    framesViewer = new EpisodeFramesSelector( tsContext() );
    framesViewer.createControl( aParent ).setLayoutData( BorderLayout.CENTER );
    framesViewer.setEpisodeId( aEpisodeId );
  }

  @Override
  protected void doSetDataRecord( IFrame aData ) {
    framesViewer.setSelectedItem( aData );
  }

  @Override
  protected IFrame doGetDataRecord() {
    return framesViewer.selectedItem();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Displays a dialog for working with frames nearby the specified frame.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aInitialFrame {@link IFrame} - the initial frame
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the frame is undefined
   * @throws TsItemNotFoundRtException the episode of the frame does not exists
   */
  public static void open( ITsGuiContext aContext, IFrame aInitialFrame ) {
    // check preconditions
    TsNullArgumentRtException.checkNulls( aContext, aInitialFrame );
    TsIllegalArgumentRtException.checkFalse( aInitialFrame.isDefined() );
    IUnitEpisodes ue = aContext.get( IUnitEpisodes.class );
    TsItemNotFoundRtException.checkFalse( ue.items().hasKey( aInitialFrame.episodeId() ) );
    // invoke dialog
    Shell shell = aContext.get( Shell.class );
    IDialogPanelCreator<IFrame, IEclipseContext> creator =
        ( aParent, aOwnerDialog ) -> new DialogWorkWithFrames( aParent, aOwnerDialog, aInitialFrame.episodeId() );
    TsDialogInfo cdi = new TsDialogInfo( aContext, shell, DLG_DWWF, DLG_DWWF_D, DF_NO_APPROVE | DF_NONMODAL );
    cdi.setMinSizeShellRelative( 65, 80 );
    TsDialog<IFrame, IEclipseContext> d = new TsDialog<>( cdi, aInitialFrame, aContext.eclipseContext(), creator );
    d.execDialog();
  }

}
