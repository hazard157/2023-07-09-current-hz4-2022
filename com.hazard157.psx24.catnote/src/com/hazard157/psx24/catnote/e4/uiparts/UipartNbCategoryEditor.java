package com.hazard157.psx24.catnote.e4.uiparts;

import static com.hazard157.psx24.catnote.m5.INbNotebookM5Constants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * View: {@link INbNotebook#rootCategory()} editor.
 *
 * @author goga
 */
public class UipartNbCategoryEditor
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {
    IM5Model<INbCategory> model = m5().getModel( MID_NB_CATEGORY, INbCategory.class );
    INbNotebook nbNotebook = tsContext().get( INbNotebook.class );
    IM5LifecycleManager<INbCategory> lm = model.getLifecycleManager( nbNotebook );
    IM5ItemsProvider<INbCategory> ip = lm.itemsProvider();
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5CollectionPanel<INbCategory> panel = model.panelCreator().createCollEditPanel( ctx, ip, lm );
    panel.createControl( aParent );
  }

}
