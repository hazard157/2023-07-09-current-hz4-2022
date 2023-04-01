package com.hazard157.psx24.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.valobj.*;

import com.hazard157.lib.core.incub.projtree.*;
import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.bricks.projtree.*;
import com.hazard157.psx24.core.e4.services.currep.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.e4.services.psxgui.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.core.m5.camera.*;
import com.hazard157.psx24.core.m5.episode.*;
import com.hazard157.psx24.core.m5.frame.*;
import com.hazard157.psx24.core.m5.note.*;
import com.hazard157.psx24.core.m5.plane.*;
import com.hazard157.psx24.core.m5.songs.*;
import com.hazard157.psx24.core.m5.srcvids.*;
import com.hazard157.psx24.core.m5.svin.*;
import com.hazard157.psx24.core.m5.tags.*;
import com.hazard157.psx24.core.m5.todos.*;
import com.hazard157.psx24.core.m5.trailer.*;
import com.hazard157.psx24.core.m5.visumple.*;
import com.hazard157.psx24.core.utils.*;
import com.hazard157.psx24.core.valeds.frames.*;

/**
 * Адон приложения.
 *
 * @author hazard157
 */
public class AddonPsx24Core
    extends MwsAbstractAddon {

  /**
   * Конструктор.
   */
  public AddonPsx24Core() {
    super( Activator.PLUGIN_ID );
    TsValobjUtils.registerKeeper( FrameSelectionCriteria.KEEPER_ID, FrameSelectionCriteria.KEEPER );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    VideoFileDurationCache dc = new VideoFileDurationCache();
    aAppContext.set( VideoFileDurationCache.class, dc );
    //
    IPsxSelectedSvinsService psss = new PsxSelectedSvinsService();
    aAppContext.set( IPsxSelectedSvinsService.class, psss );
    //
    IPlayMenuSupport playMenuSupport = new PlayMenuSupport();
    aAppContext.set( IPlayMenuSupport.class, playMenuSupport );
    //
    IProjTreeBuilder ptb = new ProjTreeBuilder();
    aAppContext.set( IProjTreeBuilder.class, ptb );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsx24CoreConstants.init( aWinContext );
    //
    ITsGuiContext ctx = new TsGuiContext( aWinContext );
    //
    IPsxFileSystem fileSystem = new PsxFileSystem( aWinContext );
    aWinContext.set( IPsxFileSystem.class, fileSystem );
    aWinContext.set( IPfsOriginalMedia.class, fileSystem.originalMedia() );
    //
    IPrisexService ps = new PrisexService( aWinContext );
    aWinContext.set( IPrisexService.class, ps );
    //
    ICurrentEpisodeService ces = new CurrentEpisodeService( aWinContext );
    aWinContext.set( ICurrentEpisodeService.class, ces );
    //
    ICurrentFramesListService cfls = new CurrentFramesListService( aWinContext );
    aWinContext.set( ICurrentFramesListService.class, cfls );
    //
    IPrisexGuiService prisexGuiService = new PrisexGuiService( ctx );
    aWinContext.set( IPrisexGuiService.class, prisexGuiService );
    //
    IProjTreeBuilder ptb = aWinContext.get( IProjTreeBuilder.class );
    ptb.registerMaker( new PtnMakerUnitCameras() );
    ptb.registerMaker( new PtnMakerUnitSourceVideos() );
    ptb.registerMaker( new PtnMakerUnitEpisodes() );
    ptb.registerMaker( new PtnMakerUnitTags() );

    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SvinM5Model() );
    m5.addModel( new FrameM5Model() );
    m5.addModel( new EpisodeM5Model() );
    m5.addModel( new TagM5Model() );
    m5.addModel( new SceneM5Model() );
    m5.addModel( new MarkPlaneGuideM5Model() );
    m5.addModel( new PlaneGuideM5Model() );
    m5.addModel( new MarkNoteM5Model( aWinContext ) );
    m5.addModel( new EpisodeTrailerM5Model() );
    m5.addModel( new TrailerM5Model() );
    m5.addModel( new SourceVideoM5Model() );
    m5.addModel( new PriorityM5Model() );
    m5.addModel( new ReminderM5Model() );
    m5.addModel( new FulfilStageM5Model() );
    m5.addModel( new TodoM5Model() );
    m5.addModel( new CameraM5Model() );
    m5.addModel( new SongM5Model() );
    m5.addModel( new VisumpleM5Model() );

    //
    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedFrameFactory.FACTORY );
    vcfRegistry.registerFactory( ValedAvFrameFactory.FACTORY );

    /**
     * FIXME resource tracking
     */
    Resource.setNonDisposeHandler( aT -> {
      /**
       * !!!
       */
      // TsTestUtils.pl( "ResourseErr: %s", aT.toString() ); //$NON-NLS-1$
      // TsTestUtils.p( "" ); //$NON-NLS-1$
    } );

  }

}
