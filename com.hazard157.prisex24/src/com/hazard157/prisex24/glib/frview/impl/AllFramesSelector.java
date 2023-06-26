package com.hazard157.prisex24.glib.frview.impl;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * {@link IAllFramesSelector} implementation.
 *
 * @author hazard157
 */
public class AllFramesSelector
    extends AbstractTsStdEventsProducerLazyPanel<IFrame, Control>
    implements IAllFramesSelector, IPsxGuiContextable {

  private final IM5CollectionPanel<IEpisode> episodesList;
  private final IEpisodeFrameSelector        frameSelector;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AllFramesSelector( ITsGuiContext aContext ) {
    super( aContext );
    IM5Model<IEpisode> model = m5().getModel( MID_EPISODE, IEpisode.class );
    IM5LifecycleManager<IEpisode> lm = model.getLifecycleManager( unitEpisodes() );
    episodesList = model.panelCreator().createCollViewerPanel( tsContext(), lm.itemsProvider() );
    frameSelector = new EpisodeFramesSelector( tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsStdEventsProducerLazyPanel
  //

  @Override
  public IFrame selectedItem() {
    return frameSelector.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    if( aItem == null || !aItem.isDefined() ) {
      frameSelector.setSelectedItem( null );
      return;
    }
    String epId = aItem.episodeId();
    episodesList.setSelectedItem( unitEpisodes().items().findByKey( epId ) );
    frameSelector.setSelectedItem( aItem );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // episodesList
    episodesList.createControl( sfMain );
    // frameSelector
    frameSelector.createControl( sfMain );
    // setup
    frameSelector.addTsSelectionListener( selectionChangeEventHelper );
    episodesList.addTsSelectionListener( ( src, sel ) -> frameSelector.setEpisodeId( sel != null ? sel.id() : null ) );
    sfMain.setWeights( 1200, 8800 );
    return sfMain;
  }

}
