package com.hazard157.prisex24.glib.frep;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IEpisodeFramesViewer} implementation.
 *
 * @author hazard157
 */
public class EpisodeFramesViewer
    extends AbstractTsStdEventsProducerLazyPanel<IFrame, Control>
    implements IEpisodeFramesViewer, IPsxGuiContextable {

  private final IM5CollectionPanel<IFrame> framesViewer;

  private String episodeId = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EpisodeFramesViewer( ITsGuiContext aContext ) {
    super( aContext );
    IM5Model<IFrame> model = m5().getModel( MID_FRAME, IFrame.class );
    framesViewer = model.panelCreator().createCollViewerPanel( tsContext(), IM5ItemsProvider.EMPTY );
  }

  // ------------------------------------------------------------------------------------
  // EpisodeFramesListViewer
  //

  @Override
  public IFrame selectedItem() {
    return framesViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    framesViewer.setSelectedItem( aItem );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    framesViewer.createControl( aParent );
    return framesViewer.getControl();
  }

  // ------------------------------------------------------------------------------------
  // IEpisodeFramesViewer
  //

  @Override
  public String getEpisodeId() {
    return episodeId;
  }

  @Override
  public void setEpisodeId( String aEpisodeId ) {
    if( !isControlValid() || Objects.equals( episodeId, aEpisodeId ) ) {
      return;
    }
    if( aEpisodeId == null || !unitEpisodes().items().hasKey( aEpisodeId ) ) {
      episodeId = null;
      framesViewer.setItemsProvider( IM5ItemsProvider.EMPTY );
    }
    else {
      IM5ItemsProvider<IFrame> ip = () -> cofsFrames().listEpisodeFrames( aEpisodeId ).items();
      framesViewer.setItemsProvider( ip );
    }
    framesViewer.refresh();
  }

  @Override
  public IList<IFrame> listShownFrames() {
    return framesViewer.items();
  }

}
