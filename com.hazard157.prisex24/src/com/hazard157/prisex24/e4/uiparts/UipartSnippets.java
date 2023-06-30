package com.hazard157.prisex24.e4.uiparts;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;

import com.hazard157.prisex24.onetime.*;
import com.hazard157.prisex24.pdus.snippets.*;

/**
 * UIpart: {@link ISnippet} management.
 *
 * @author hazard157
 */
public class UipartSnippets
    extends PsxAbstractUipart {

  private IM5CollectionPanel<ISnippet> panel;

  @Override
  protected void doInit( Composite aParent ) {

    // TODO onetime ---
    ConvertSomePlansToSnippets c = new ConvertSomePlansToSnippets( tsContext() );
    c.convertStirsToSnippets();
    // ---

    IM5Model<ISnippet> model = m5().getModel( MID_SNIPPET, ISnippet.class );
    IM5LifecycleManager<ISnippet> lm = model.getLifecycleManager( unitSnippets() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    panel.createControl( aParent );
  }

}
