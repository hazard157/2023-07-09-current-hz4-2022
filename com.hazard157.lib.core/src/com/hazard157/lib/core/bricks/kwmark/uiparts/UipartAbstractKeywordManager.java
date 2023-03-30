package com.hazard157.lib.core.bricks.kwmark.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.lib.core.bricks.kwmark.m5.*;
import com.hazard157.lib.core.bricks.kwmark.manager.*;

/**
 * Refbook "SexToys".
 *
 * @author goga
 */
public abstract class UipartAbstractKeywordManager
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {
    IKeywordManager kwMan = doGetKeywordManager();
    if( kwMan != null ) {
      IM5Model<String> model = m5().getModel( KwmanKeywordM5Model.MODEL_ID, String.class );
      ITsGuiContext ctx = new TsGuiContext( tsContext() );
      IM5LifecycleManager<String> lm = model.getLifecycleManager( kwMan );
      IM5ItemsProvider<String> ip = lm.itemsProvider();
      IM5CollectionPanel<String> panel = model.panelCreator().createCollEditPanel( ctx, ip, lm );
      panel.createControl( aParent );
    }
    else {
      Label l = new Label( aParent, SWT.CENTER );
      l.setText( "No IKeywordManager found" ); //$NON-NLS-1$
    }
  }

  protected abstract IKeywordManager doGetKeywordManager();

}
