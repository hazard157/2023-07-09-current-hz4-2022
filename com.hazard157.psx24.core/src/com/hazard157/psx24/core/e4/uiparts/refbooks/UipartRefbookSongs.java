package com.hazard157.psx24.core.e4.uiparts.refbooks;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx.proj3.songs.*;
import com.hazard157.psx24.core.m5.*;
import com.hazard157.psx24.core.m5.songs.*;

/**
 * Вью редактирования содержимого {@link IUnitSongs}.
 *
 * @author hazard157
 */
public class UipartRefbookSongs
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {
    IM5Model<ISong> model = m5().getModel( IPsxM5Constants.MID_SONG, ISong.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IUnitSongs sm = tsContext().get( IUnitSongs.class );
    IM5LifecycleManager<ISong> lm = model.getLifecycleManager( sm );
    IM5ItemsProvider<ISong> ip = lm.itemsProvider();
    IM5CollectionPanel<ISong> panel = model.panelCreator().createCollEditPanel( ctx, ip, lm );
    panel.createControl( aParent );

    // TODO Auto-generated method stub

  }

}
