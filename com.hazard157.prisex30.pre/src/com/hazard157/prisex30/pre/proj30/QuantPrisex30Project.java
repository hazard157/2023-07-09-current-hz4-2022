package com.hazard157.prisex30.pre.proj30;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.bound.*;
import org.toxsoft.core.txtproj.lib.impl.*;

/**
 * PRISEX project quant.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class QuantPrisex30Project
    extends AbstractQuant {

  /**
   * Command line argument name for {@link ProgramArgs} to specify PSXv3 textual project file path.
   */
  public static final String CMDLINE_ARG_PROJECT_FILE_PATH = "project"; //$NON-NLS-1$

  private static final String DEFAULT_PROJ_FILE_PATH    = "/home/hmade/data/projects/ver30/prisex30.txt"; //$NON-NLS-1$
  private static final String PROJ_FILE_APP_ID          = "com.hazard157.prisex30";                       //$NON-NLS-1$
  private static final int    PROJ_FILE_FORMAT_VERSTION = 30;

  /**
   * Information about project file v30.
   */
  public static final TsProjectFileFormatInfo PROJECT_FILE_FORMAT_INFO =
      new TsProjectFileFormatInfo( PROJ_FILE_APP_ID, PROJ_FILE_FORMAT_VERSTION );

  public static final String UNITID_XXX = "Xxx"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public QuantPrisex30Project() {
    super( QuantPrisex30Project.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ITsProject proj = aAppContext.get( ITsProject.class );
    if( proj == null ) {
      proj = new TsProject( PROJECT_FILE_FORMAT_INFO );
      aAppContext.set( ITsProject.class, proj );
      ITsProjectFileBound bound = new TsProjectFileBound( proj, IOptionSet.NULL );
      aAppContext.set( ITsProjectFileBound.class, bound );
      ProgramArgs programArgs = aAppContext.get( ProgramArgs.class );
      String path = programArgs.getArgValue( CMDLINE_ARG_PROJECT_FILE_PATH, DEFAULT_PROJ_FILE_PATH );
      File projectFile = new File( path );
      bound.open( projectFile );
    }
    initProject( aAppContext );
    ITsProjectFileBound bound = aAppContext.get( ITsProjectFileBound.class );
    if( bound.hasFileBound() ) {
      LoggerUtils.defaultLogger().info( "OK loading project %s", bound.getFile().getAbsolutePath() ); //$NON-NLS-1$
    }
  }

  /**
   * Fills project (references from context) and context with PDUs.
   *
   * @param aAppContext {@link IEclipseContext} - the application level context
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void initProject( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    ITsProject proj = aAppContext.get( ITsProject.class );
    TsInternalErrorRtException.checkNull( proj );
    //
    // IUnitXxx unitXxx = new UnitXxx();
    // proj.registerUnit( UNITID_XXX, unitXxx, true );
    // aAppContext.set( IUnitXxx.class, unitXxx );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IPrisexProject30Constants.init( aWinContext );
  }

}
