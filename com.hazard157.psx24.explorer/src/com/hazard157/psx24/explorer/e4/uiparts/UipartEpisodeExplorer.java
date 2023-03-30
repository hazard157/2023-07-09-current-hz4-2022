package com.hazard157.psx24.explorer.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.psx24.explorer.gui.resview.*;

/**
 * Выборки для конкретного эпизода.
 *
 * @author goga
 */
public class UipartEpisodeExplorer
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.VERTICAL );
    //
    TabFolder tabFolder = new TabFolder( sfMain, SWT.TOP );
    TabItem tabItem1 = new TabItem( tabFolder, SWT.NONE );
    tabItem1.setText( "Inquiries" );
    TabItem tabItem2 = new TabItem( tabFolder, SWT.NONE );
    tabItem2.setText( "Direct" );

    //
    ResultsPanelAsSimpleList resultsPanel = new ResultsPanelAsSimpleList( aParent, tsContext() );

  }

}
