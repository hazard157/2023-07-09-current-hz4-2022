package com.hazard157.psx24.explorer.e4.uiparts;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.explorer.e4.services.*;
import com.hazard157.psx24.explorer.gui.filters.*;
import com.hazard157.psx24.explorer.m5.*;
import com.hazard157.psx24.explorer.pq.*;
import com.hazard157.psx24.explorer.unit.*;

/**
 * Вью просмотра и редакттирования выборок обозревателя.
 *
 * @author hazard157
 */
public class UipartsInquiries
    extends MwsAbstractPart {

  // TODO как-то вынести в GUI соответствующий checkbox
  static boolean useLiveFilter = true;

  /**
   * При переходе на другую выборку.
   */
  private final ITsSelectionChangeListener<Inquiry> selectionChangeListener = ( aSource, aSelectedItem ) -> {
    if( this.lastSelection != null ) {
      this.lastSelection.eventer().removeListener( this.inquiryChangeListener );
    }
    if( aSelectedItem != null ) {
      aSelectedItem.eventer().addListener( this.inquiryChangeListener );
    }
    this.lastSelection = aSelectedItem;
    updateItemsPanel();
    updateQueryResults();
  };

  /**
   * При изменении параметров выборки {@link Inquiry}.
   */
  final IGenericChangeListener inquiryChangeListener = aSource -> updateQueryResults();

  /**
   * При изменении параметров выборки живого фильтра.
   */
  final IGenericChangeListener liveFilterChangeListener = aSource -> {
    if( useLiveFilter ) {
      updateQueryResults();
    }
  };

  @Inject
  ICurrentPqResultService currentPqResultService;

  @Inject
  IUnitExplorer unitExplorer;

  IM5CollectionPanel<Inquiry>     panelInquiry;
  IM5CollectionPanel<InquiryItem> panelItems;
  PanelAllFiltersSet              liveFilterPanel;

  Inquiry lastSelection = null;

  @Override
  protected void doInit( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.VERTICAL );
    // Inquiries list
    IM5Model<Inquiry> modelInquiry = m5().getModel( InquiryM5Model.MODEL_ID, Inquiry.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<Inquiry> lmInquiry = modelInquiry.getLifecycleManager( unitExplorer );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    panelInquiry = modelInquiry.panelCreator().createCollEditPanel( ctx, lmInquiry.itemsProvider(), lmInquiry );
    panelInquiry.createControl( sfMain );
    // filters list
    IM5Model<InquiryItem> modelItem = m5().getModel( InquiryItemM5Model.MODEL_ID, InquiryItem.class );
    ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_COLUMN_HEADER.setValue( ctx.params(), AV_FALSE );
    IM5LifecycleManager<InquiryItem> lmFp = null;
    panelItems = modelItem.panelCreator().createCollEditPanel( ctx, null, lmFp );
    panelItems.createControl( sfMain );
    // filterPanel
    liveFilterPanel = new PanelAllFiltersSet( sfMain, ctx );
    liveFilterPanel.genericChangeEventer().addListener( liveFilterChangeListener );
    // setup
    sfMain.setWeights( 330, 150, 500 );
    panelInquiry.addTsSelectionListener( selectionChangeListener );
    updateItemsPanel();
    updateQueryResults();
    panelInquiry.getControl().addDisposeListener( e -> {
      if( lastSelection != null ) {
        lastSelection.eventer().removeListener( inquiryChangeListener );
      }
    } );
  }

  void updateQueryResults() {
    Inquiry inquiry = panelInquiry.selectedItem();
    // prepare
    IUnitEpisodes epman = tsContext().get( IUnitEpisodes.class );
    ISvinSeq rs = ISvinSeq.EMPTY;
    // queries
    if( inquiry != null && !inquiry.items().isEmpty() ) {
      rs = PqQueryProcessor.createFull( epman );
      for( InquiryItem item : inquiry.items() ) {
        PqQueryProcessor proc = new PqQueryProcessor( rs, epman );
        rs = proc.queryData( item.getFilterParams() );
      }
    }
    // liveFilter
    if( useLiveFilter ) {
      PqQueryProcessor proc = new PqQueryProcessor( rs, epman );
      ITsCombiFilterParams cfp = liveFilterPanel.getInquiryItem().getFilterParams();
      if( cfp != ITsCombiFilterParams.NONE ) {
        rs = proc.queryData( cfp );
      }
    }
    currentPqResultService.setCurrent( rs );
  }

  void updateItemsPanel() {
    Inquiry inquiry = panelInquiry.selectedItem();
    IM5Model<InquiryItem> modelItem = m5().getModel( InquiryItemM5Model.MODEL_ID, InquiryItem.class );
    IM5LifecycleManager<InquiryItem> lm = null;
    IM5ItemsProvider<InquiryItem> ip = null;
    if( inquiry != null ) {
      lm = modelItem.getLifecycleManager( inquiry );
      ip = lm.itemsProvider();
    }
    panelItems.setLifecycleManager( lm );
    panelItems.setItemsProvider( ip );
    panelItems.refresh();
  }

}
