package com.hazard157.prisex24.glib.frview.impl;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.glib.frasel.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.utils.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * {@link ISvinsFramesViewer} implementation.
 *
 * @author hazard157
 */
public class SvinsFramesViewer
    extends TsStdEventsProducerPanel<IFrame>
    implements ISvinsFramesViewer, ITsActionHandler {

  private final ISvinSeqEdit      svinSeq = new SvinSeq();
  private final ISvinFramesParams svinFramnesPatams;

  private final TsToolbar         toolbar;
  private final IFramesGridViewer fgViewer;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SvinsFramesViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    svinFramnesPatams = new SvinFramesParams();
    this.setLayout( new BorderLayout() );
    // fgViewer
    toolbar = new TsToolbar( tsContext() );
    toolbar.setVertical( false );
    toolbar.setActionDefs( //
        ACDEF_GIF_CREATE_MENU, ACDEF_SEPARATOR, //
        // FIXME ACDEF_SHOW_SINGLE, ACDEF_CAM_ID_MENU, ACDEF_SEPARATOR, //
        ACDEF_VIEW_AS_TREE_MENU, ACDEF_VIEW_AS_LIST, ACDEF_SEPARATOR, //
        ACDEF_COLLAPSE_ALL, ACDEF_EXPAND_ALL, //
        ACDEF_ZOOM_ORIGINAL_PUSHBUTTON, //
        // FIXME ACDEF_COPY_FRAME, //
        ACDEF_PLAY_MENU, //
        ACDEF_GO_PREV, ACDEF_GO_NEXT, //
        //
        // FIXME ACDEF_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL_MENU, // Frame time step meny (density)
        // FIXME ACDEF_THUMB_SIZEABLE_ZOOM_MENU, // Thumb size menu (zoom)
        // FIXME ACDEF_SHOW_AK_BOTH, ACDEF_SHOW_AK_ANIMATED, ACDEF_SHOW_AK_SINGLE, ACDEF_SEPARATOR, // Animation
        // FIXME ACDEF_CAM_ID_MENU, ACDEF_SEPARATOR, // Camera
        ACDEF_WORK_WITH_FRAMES, // Play menu
        // FIXME ACDEF_ONE_BY_ONE, //
        ACDEF_SEPARATOR, ACDEF_RUN_KDENLIVE //

    );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // fgViewer
    fgViewer = new FramesGridViewer( this, tsContext() );
    fgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    toolbar.setActionMenu( ACTID_GIF_CREATE, new AbstractGifManagemntDropDownMenuCreator( tsContext(), this ) {

      @Override
      protected IFrame doGetFrame() {
        return selectedItem();
      }

    } );
    svinSeq.genericChangeEventer().addListener( s -> whenSvinSeqChanged() );
    svinFramnesPatams.genericChangeEventer().addListener( s -> whenSvinFramesStrategyChanged() );
    updateActionState();
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    // IFrame sel = selectedItem();
    switch( aActionId ) {
      case ACTID_ABOUT: {
        TsDialogUtils.underDevelopment( getShell() );
        break;
      }
      default:
        TsDialogUtils.warn( getShell(), aActionId );
    }
    updateActionState();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateActionState() {
    // TODO SvinsFramesViewer.handleAction()
  }

  private void recrateUnfilteredFramesListAndSetToFgViewer() {
    // optimize
    int totalDuration = 0;
    for( Svin s : svinSeq.svins() ) {
      totalDuration += s.interval().duration();
    }
    int order = estimateOrder( 5 * totalDuration );
    // SVINs -> frames
    SvinFramesSelector sfs = new SvinFramesSelector( tsContext() );
    IListBasicEdit<IFrame> ll = new SortedElemLinkedBundleList<>( getListInitialCapacity( order ), true );
    for( Svin s : svinSeq.svins() ) {
      ll.addAll( sfs.selectFrames( s, svinFramnesPatams ) );
    }
    // display
    fgViewer.setFrames( ll );
  }

  private void whenSvinFramesStrategyChanged() {
    recrateUnfilteredFramesListAndSetToFgViewer();
  }

  private void whenSvinSeqChanged() {
    recrateUnfilteredFramesListAndSetToFgViewer();
  }

  // ------------------------------------------------------------------------------------
  // TsStdEventsProducerPanel
  //

  @Override
  public IFrame selectedItem() {
    return fgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    fgViewer.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeChangeCapable
  //

  @Override
  public IThumbSizeableEx thumbSizeManager() {
    return fgViewer.thumbSizeManager();
  }

  // ------------------------------------------------------------------------------------
  // ISvinsFramesViewer
  //

  @Override
  public ISvinSeqEdit svinSeq() {
    return svinSeq;
  }

  @Override
  public ISvinFramesParams svinFramesParams() {
    return svinFramnesPatams;
  }

  @Override
  public int getDisplayedInfoFlags() {
    return fgViewer.getDisplayedInfoFlags();
  }

  @Override
  public void setDisplayedInfoFlags( int aPgvLfFlags ) {
    fgViewer.setDisplayedInfoFlags( aPgvLfFlags );
  }

  @Override
  public TsComposite getControl() {
    return this;
  }

}
