package com.hazard157.prisex24.glib.frview.impl;

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

  private final ISvinSeqEdit                 svinSeq = new SvinSeq();
  private final ISvinFramesSelectionStrategy svinFramnesStrategy;

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
    svinFramnesStrategy = new SvinFramesSelectionStrategy( tsContext() );
    this.setLayout( new BorderLayout() );
    // fgViewer
    toolbar = new TsToolbar( tsContext() );
    toolbar.setVertical( true );
    toolbar.setActionDefs( ACDEF_ABOUT );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.WEST );
    // fgViewer
    fgViewer = new FramesGridViewer( this, tsContext() );
    fgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    svinSeq.genericChangeEventer().addListener( s -> whenSvinSeqChanged() );
    svinFramnesStrategy.genericChangeEventer().addListener( s -> whenSvinFramesStrategyChanged() );
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
    IListBasicEdit<IFrame> ll = new SortedElemLinkedBundleList<>( getListInitialCapacity( order ), true );
    for( Svin s : svinSeq.svins() ) {
      ll.addAll( svinFramnesStrategy.selectFrames( s ) );
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
        break;
    }
    updateActionState();
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
  public ISvinFramesSelectionStrategy svinStrategy() {
    return svinFramnesStrategy;
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
