package com.hazard157.psx.proj3.sourcevids;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.txtproj.lib.*;

import com.hazard157.psx.proj3.sourcevids.impl.*;

/**
 * Квант работы с с исходными видео-материалами.
 *
 * @author hazard157
 */
public class QuantSourceVideos
    extends AbstractQuant {

  /**
   * Экземпляр-синглтон кванта.
   */
  public static final AbstractQuant INSTANCE = new QuantSourceVideos();

  private static final String UNITID_SOURCE_VIDEOS = "SourceVideos"; //$NON-NLS-1$

  private QuantSourceVideos() {
    super( QuantSourceVideos.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsProject proj = aWinContext.get( ITsProject.class );
    IUnitSourceVideos unit = new UnitSourceVideos();
    proj.registerUnit( UNITID_SOURCE_VIDEOS, unit, true );
    aWinContext.set( IUnitSourceVideos.class, unit );
  }

}
