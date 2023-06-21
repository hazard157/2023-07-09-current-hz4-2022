package com.hazard157.prisex24.e4.addons;

import static com.hazard157.prisex24.Activator.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.rcp.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.common.*;
import com.hazard157.lib.core.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.Activator;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.cofs.impl.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.services.psx.*;
import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.m5.camera.*;
import com.hazard157.prisex24.m5.episodes.*;
import com.hazard157.prisex24.m5.frames.*;
import com.hazard157.prisex24.m5.note.*;
import com.hazard157.prisex24.m5.plane.*;
import com.hazard157.prisex24.m5.srcvideo.*;
import com.hazard157.prisex24.m5.tags.*;
import com.hazard157.prisex24.m5.todos.*;
import com.hazard157.prisex24.valeds.frames.*;
import com.hazard157.psx.proj3.*;

/**
 * Plugin addon - initializes all subsystems and modules..
 *
 * @author hazard157
 */
public class AddonPrisex24Core
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonPrisex24Core() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantTsGuiRcp() );
    aQuantRegistrator.registerQuant( new QuantHzLibCore() );
    aQuantRegistrator.registerQuant( new QuantHzCommon_HzCore_Compatible() );
    aQuantRegistrator.registerQuant( new QuantPsx3Project() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // window and application icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( PLUGIN_ID, ICONID_APP_ICON, IS_48X48 ) );
    // COFS
    aAppContext.set( IPsxCofs.class, new PsxCofs() );
    // E4 services
    aAppContext.set( ICurrentEpisodeService.class, new CurrentEpisodeService( aAppContext ) );
    aAppContext.set( IPsxSelectedSvinsService.class, new PsxSelectedSvinsService() );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPrisex24CoreConstants.init( aWinContext );
    //
    aWinContext.set( IPrisex24Service.class, new Prisex24Service( new TsGuiContext( aWinContext ) ) );
    // M5
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new FulfilStageM5Model() );
    m5.addModel( new TodoM5Model() );
    m5.addModel( new TagM5Model() );
    m5.addModel( new FrameM5Model() );
    m5.addModel( new EpisodeM5Model() );
    m5.addModel( new SceneM5Model() );
    m5.addModel( new MarkPlaneGuideM5Model() );
    m5.addModel( new MarkNoteM5Model() );
    m5.addModel( new CameraM5Model() );
    m5.addModel( new SourceVideoM5Model() );
    // VALEDs
    IValedControlFactoriesRegistry vcfReg = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfReg.registerFactory( ValedFrameFactory.FACTORY );
    vcfReg.registerFactory( ValedAvValobjFrameEditor.FACTORY );

    // DEBUG --- resource tracking
    Resource.setNonDisposeHandler( aT -> {
      TsTestUtils.pl( "ResourseErr: %s", aT.toString() ); //$NON-NLS-1$
      TsTestUtils.p( "" ); //$NON-NLS-1$
    } );
    // ---

  }

}
