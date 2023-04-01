package com.hazard157.psx24.core.e4.uiparts;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.core.glib.frlstviewer_any.*;

/**
 * UI part to view
 *
 * @author hazard157
 */
public class UipartSvinsFrames
    extends MwsAbstractPart {

  private final IGenericChangeListener svinsListChangeListener =
      aSource -> this.viewer.setSvins( this.psxSelectedSvinsService.svins() );

  @Inject
  IPsxSelectedSvinsService psxSelectedSvinsService;

  IPanelSvinFramesViewer viewer;

  @Override
  protected void doInit( Composite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    viewer = new PanelSvinFramesViewer( ctx );
    viewer.createControl( aParent );
    viewer.setSvins( psxSelectedSvinsService.svins() );

    // DEBUG ---
    viewer.setOnlySvinCamsShown( false );
    // ---

    psxSelectedSvinsService.eventer().addListener( svinsListChangeListener );
  }

}
