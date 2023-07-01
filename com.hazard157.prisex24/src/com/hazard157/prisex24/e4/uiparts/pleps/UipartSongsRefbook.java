package com.hazard157.prisex24.e4.uiparts.pleps;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx.proj3.songs.*;

/**
 * UIpart: edit songs {@link IUnitSongs#items()}.
 *
 * @author hazard157
 */
public class UipartSongsRefbook
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {
    IM5Model<ISong> model = m5().getModel( MID_SONG, ISong.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IUnitSongs sm = tsContext().get( IUnitSongs.class );
    IM5LifecycleManager<ISong> lm = model.getLifecycleManager( sm );
    IM5ItemsProvider<ISong> ip = lm.itemsProvider();
    IM5CollectionPanel<ISong> panel = model.panelCreator().createCollEditPanel( ctx, ip, lm );
    panel.createControl( aParent );
  }

}
