package com.hazard157.psx24.catnote.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.core.txtproj.lib.*;

import com.hazard157.psx24.catnote.*;
import com.hazard157.psx24.catnote.m5.*;
import com.hazard157.psx24.catnote.main.*;
import com.hazard157.psx24.catnote.main.impl.*;

/**
 * Module addon.
 *
 * @author hazard157
 */
public class AddonPsx24Catnote
    extends MwsAbstractAddon {

  public static final String UNITID_NOTEBOOK = "Notebook"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AddonPsx24Catnote() {
    super( Activator.PLUGIN_ID );
    TsValobjUtils.registerKeeperIfNone( ENbNoteKind.KEEPER_ID, ENbNoteKind.KEEPER );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ITsProject proj = aAppContext.get( ITsProject.class );
    TsInternalErrorRtException.checkNull( proj );
    // регистрация модулей проекта
    INbNotebook unitNotebook = new NbNotebook();
    proj.registerUnit( UNITID_NOTEBOOK, unitNotebook, true );
    aAppContext.set( INbNotebook.class, unitNotebook );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsx24CatnoteConstants.init( aWinContext );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new NoteKindM5Model() );
    m5.addModel( new NbCategoryM5Model() );
    m5.addModel( new NbNoteM5Model() );
  }

}
