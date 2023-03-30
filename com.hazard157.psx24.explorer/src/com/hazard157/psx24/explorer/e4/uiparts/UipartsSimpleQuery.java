package com.hazard157.psx24.explorer.e4.uiparts;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.explorer.e4.services.*;
import com.hazard157.psx24.explorer.gui.filters.*;
import com.hazard157.psx24.explorer.pq.*;

/**
 * Вью простого запроса, содержит фильтры по И, изменение любого немедленно отображает результат.
 *
 * @author goga
 */
public class UipartsSimpleQuery
    extends MwsAbstractPart {

  private final IGenericChangeListener anyFilterChamgeListener = aSource -> whenFilterChanged();

  @Inject
  ICurrentPqResultService currentPqResultService;

  PanelAllFiltersSet panel;

  @Override
  protected void doInit( Composite aParent ) {
    aParent.setLayout( new BorderLayout() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new PanelAllFiltersSet( aParent, ctx );
    panel.setLayoutData( BorderLayout.CENTER );
    panel.genericChangeEventer().addListener( anyFilterChamgeListener );
  }

  void whenFilterChanged() {
    // prepare
    IUnitEpisodes epman = tsContext().get( IUnitEpisodes.class );
    PqResultSet in = PqResultSet.createFull( epman );
    PqQueryProcessor proc = new PqQueryProcessor( in, epman );
    // query
    ITsCombiFilterParams fp = panel.getInquiryItem().getFilterParams();
    PqResultSet rs = proc.queryData( fp );
    currentPqResultService.setCurrent( rs );
  }

}
