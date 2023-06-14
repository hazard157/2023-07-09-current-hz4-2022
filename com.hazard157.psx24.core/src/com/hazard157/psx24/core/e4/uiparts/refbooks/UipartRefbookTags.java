package com.hazard157.psx24.core.e4.uiparts.refbooks;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx24.core.m5.*;

/**
 * UIpart: tags refbook editor.
 *
 * @author hazard157
 */
public class UipartRefbookTags
    extends MwsAbstractPart {

  @Inject
  IRootTag rootTag;

  @Override
  protected void doInit( Composite aParent ) {
    IM5Model<ITag> model = m5().getModel( IPsxM5Constants.MID_TAG, ITag.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<ITag> lm = model.getLifecycleManager( rootTag );
    // DETAILS_PANE_PLACEMENT.setValue( ctx.params(), EBorderLayoutPlacement.SOUTH );
    IM5CollectionPanel<ITag> panel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    panel.createControl( aParent );
  }

}
