package com.hazard157.psx24.core.e4.uiparts.eps;

import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tsgui.utils.layout.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.currep.*;

/**
 * Вью списка эпизодов.
 *
 * @author goga
 */
public class UipartEpisodesList
    extends MwsAbstractPart {

  private final ICurrentEntityChangeListener<IEpisode> currentEpisodeChangeListener = aCurrent -> {
    if( !this.isSelfChange ) {
      updateCurrentEpisode();
    }
  };

  private final ITsSelectionChangeListener<IEpisode> selectionChangeListener = ( aSource, aSelectedItem ) -> {
    this.isSelfChange = true;
    try {
      this.currentEpisodeService.setCurrent( aSelectedItem );
    }
    finally {
      this.isSelfChange = false;
    }
  };

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  IM5Model<IEpisode>           model;
  IM5CollectionPanel<IEpisode> panel;
  IUnitEpisodes                unitEpisodes;
  boolean                      isSelfChange = false;

  @Override
  protected void doInit( Composite aParent ) {
    currentEpisodeService.addCurrentEntityChangeListener( currentEpisodeChangeListener );
    model = m5().getModel( MID_EPISODE, IEpisode.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), avValobj( EThumbSize.SZ180 ) );
    IMultiPaneComponentConstants.OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( EIconSize.IS_64X64 ) );
    IM5LifecycleManager<IEpisode> lm = model.getLifecycleManager( null );
    panel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    aParent.setLayout( new BorderLayout() );
    panel.createControl( aParent );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionChangeListener );
    updateCurrentEpisode();
  }

  void updateCurrentEpisode() {
    panel.setSelectedItem( currentEpisodeService.current() );
  }

}
