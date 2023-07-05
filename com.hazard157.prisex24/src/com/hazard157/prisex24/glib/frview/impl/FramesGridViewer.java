package com.hazard157.prisex24.glib.frview.impl;

import static com.hazard157.prisex24.glib.frview.impl.PgvVisualsProviderFrame.*;
import static org.toxsoft.core.tsgui.panels.pgv.IPicsGridViewerConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IFramesGridViewer} implementation.
 *
 * @author hazard157
 */
public class FramesGridViewer
    extends TsStdEventsProducerPanel<IFrame>
    implements IFramesGridViewer {

  private final IListEdit<IFrame>       allFrames = new ElemLinkedBundleList<>();
  private final IPicsGridViewer<IFrame> pgViewer;

  private PgvVisualsProviderFrame visualsProvider = new PgvVisualsProviderFrame( tsContext(), LF_DATE | LF_HMS );
  private ITsFilter<IFrame>       frameFilter     = ITsFilter.ALL;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FramesGridViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    pgViewer = new PicsGridViewer<>( this, tsContext() );
    pgViewer.setVisualsProvider( visualsProvider );
    pgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    pgViewer.addTsDoubleClickListener( doubleClickEventHelper );
    pgViewer.addTsSelectionListener( selectionChangeEventHelper );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void reinitViewedItems() {
    IListEdit<IFrame> llFiltered = new ElemArrayList<>( allFrames.size() );
    for( IFrame f : allFrames ) {
      if( frameFilter.accept( f ) ) {
        llFiltered.add( f );
      }
    }
    pgViewer.setItems( llFiltered );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public IFrame selectedItem() {
    return pgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    pgViewer.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeChangeCapable
  //

  @Override
  public IThumbSizeableEx thumbSizeManager() {
    return pgViewer;
  }

  // ------------------------------------------------------------------------------------
  // IFramesGridViewer
  //

  @Override
  public IList<IFrame> listDisplayedFrames() {
    return pgViewer.items();
  }

  @Override
  public IList<IFrame> getFrames() {
    return allFrames;
  }

  @Override
  public void setFrames( IList<IFrame> aFrames ) {
    TsNullArgumentRtException.checkNull( aFrames );
    allFrames.setAll( aFrames );
    reinitViewedItems();
  }

  @Override
  public ITsFilter<IFrame> getFrameFiler() {
    return frameFilter;
  }

  @Override
  public void setFrameFilter( ITsFilter<IFrame> aFilter ) {
    TsNullArgumentRtException.checkNull( aFilter );
    frameFilter = aFilter;
    reinitViewedItems();
  }

  @Override
  public int getDisplayedInfoFlags() {
    return visualsProvider.getDisplayedInfoFlags();
  }

  @Override
  public void setDisplayedInfoFlags( int aPgvLfFlags ) {
    if( getDisplayedInfoFlags() != aPgvLfFlags ) {
      visualsProvider = new PgvVisualsProviderFrame( tsContext(), aPgvLfFlags );
      OPDEF_LABEL_LINES.setValue( pgViewer.tsContext().params(), avInt( visualsProvider.getNameLinesCount() ) );
      pgViewer.setVisualsProvider( visualsProvider );
    }
  }

  @Override
  public boolean isForceStill() {
    return pgViewer.isForceStill();
  }

  @Override
  public void setFocreStill( boolean aForceStill ) {
    pgViewer.setFocreStill( aForceStill );
  }

  @Override
  public TsComposite getControl() {
    return this;
  }

}
