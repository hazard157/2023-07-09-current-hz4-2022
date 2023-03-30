package com.hazard157.psx24.explorer.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.txtproj.lib.*;

import com.hazard157.psx24.explorer.*;
import com.hazard157.psx24.explorer.e4.services.*;
import com.hazard157.psx24.explorer.m5.*;
import com.hazard157.psx24.explorer.unit.*;
import com.hazard157.psx24.explorer.unit.impl.*;

/**
 * Адон плагина.
 *
 * @author goga
 */
public class AddonPsx24Explorer
    extends MwsAbstractAddon {

  /**
   * Ключевое слово компоненты проекта.
   */
  public static final String UNITID_EXPLORER = "Explorer_ts4"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public AddonPsx24Explorer() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ICurrentPqResultService pqrs = new CurrentPqResultService();
    aAppContext.set( ICurrentPqResultService.class, pqrs );
    // инициализация модуля проекта
    ITsProject proj = aAppContext.get( ITsProject.class );
    IUnitExplorer explorerUnit = new UnitExplorer();
    proj.registerUnit( UNITID_EXPLORER, explorerUnit, true );
    aAppContext.set( IUnitExplorer.class, explorerUnit );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // инициализация констант в интерфейсах
    IPsx24ExplorerConstants.init( aWinContext );
    // m5
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new InquiryItemM5Model() );
    m5.addModel( new InquiryM5Model() );
  }

}
