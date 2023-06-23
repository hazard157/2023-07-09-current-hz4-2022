package com.hazard157.prisex24.glib.frview.impl;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
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
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // episodesList
    episodesList.createControl( board );
    episodesList.getControl().setLayoutData( BorderLayout.EAST );
    // frameSelector
    frameSelector.createControl( board );
    frameSelector.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    frameSelector.addTsSelectionListener( selectionChangeEventHelper );
    episodesList.addTsSelectionListener( ( src, sel ) -> frameSelector.setEpisodeId( sel != null ? sel.id() : null ) );
    return board;
  }

}
