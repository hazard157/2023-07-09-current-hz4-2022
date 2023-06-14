package com.hazard157.prisex24.e4.uiparts.refbooks;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;

import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.m5.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * UIpart: tags refbook editor.
 *
 * @author hazard157
 */
public class UipartRefbookTags
    extends PsxAbstractUipart {

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
