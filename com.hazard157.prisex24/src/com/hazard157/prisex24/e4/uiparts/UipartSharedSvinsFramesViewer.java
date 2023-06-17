package com.hazard157.prisex24.e4.uiparts;

import javax.inject.*;

import org.eclipse.swt.widgets.*;

import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;

/**
 * Shared UIpart: displays frames of {@link IPsxSelectedSvinsService#svins()}.
 * <p>
 * When {@link IPsxSelectedSvinsService#eventer()} or {@link IPsxSelectedSvinsService#framesSelectionParams()} eventers
 * fire the change event refreshes the view.
 *
 * @author hazard157
 */
public class UipartSharedSvinsFramesViewer
    extends PsxAbstractUipart {

  @Inject
  IPsxSelectedSvinsService selectedSvinsService;

  private ISvinsFramesViewer svinViewer;

  @Override
  protected void doInit( Composite aParent ) {
    svinViewer = new SvinsFramesViewer( aParent, tsContext() );
    selectedSvinsService.eventer().addListener( s -> refreshView() );
    selectedSvinsService.framesSelectionParams().genericChangeEventer().addListener( s -> refreshView() );
  }

  private void refreshView() {
    svinViewer.svinFramesParams().setParams( selectedSvinsService.framesSelectionParams() );
    svinViewer.svinSeq().svins().setAll( selectedSvinsService.svins() );
  }

}
