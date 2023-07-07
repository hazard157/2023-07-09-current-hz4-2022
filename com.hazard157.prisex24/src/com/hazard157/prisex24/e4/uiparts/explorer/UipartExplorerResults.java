package com.hazard157.prisex24.e4.uiparts.explorer;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.services.currpq.*;
import com.hazard157.prisex24.e4.services.selsvins.*;
import com.hazard157.prisex24.explorer.*;
import com.hazard157.prisex24.explorer.pq.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Вью отображения результатов выборки.
 *
 * @author hazard157
 */
public class UipartExplorerResults
    extends MwsAbstractPart {

  @SuppressWarnings( "unchecked" )
  private final ICurrentEntityChangeListener<ISvinSeq> resultChangedListener = aCurrent -> {
    if( aCurrent != null ) {
      this.viewer.setPqResults( aCurrent );
      if( this.unitEpisodes == null ) {
        return;
      }
      IList<Svin> selSvins = this.viewer.getSelectionSvins();
      if( selSvins.isEmpty() ) { // если нет выбранных, покажем ВСН Svin-ы
        this.psxSelectedSvinsService.setSvins( this.viewer.getAllSvins() );
      }
      else {
        this.psxSelectedSvinsService.setSvins( selSvins );
      }
    }
    else {
      this.viewer.setPqResults( ISvinSeq.EMPTY );
      this.psxSelectedSvinsService.setSvins( IList.EMPTY );
    }
  };

  private final ITsSelectionChangeListener<Svin> selectionChangeListener = ( aSource, aSelectedItem ) -> {
    this.psxSelectedSvinsService.setSvins( this.viewer.getSelectionSvins() );
  };

  @Inject
  ICurrentPqResultService currentPqResultService;

  @Inject
  IPsxSelectedSvinsService psxSelectedSvinsService;

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  @Inject
  IUnitEpisodes unitEpisodes;

  @Inject
  IUnitSourceVideos unitSourceVideos;

  ResultsPanelAsSimpleList viewer;

  @Override
  protected void doInit( Composite aParent ) {
    currentPqResultService.addCurrentEntityChangeListener( resultChangedListener );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    viewer = new ResultsPanelAsSimpleList( aParent, ctx );
    viewer.addTsSelectionListener( selectionChangeListener );
    // покажем все эпизоды
    ISvinSeq rs = PqQueryProcessor.createFull( unitEpisodes );
    viewer.setPqResults( rs );
  }

  IFrame chooseBestFrameForSvin( Svin aSvin ) {
    IEpisode e = unitEpisodes.items().findByKey( aSvin.episodeId() );
    if( e == null ) {
      return IFrame.NONE;
    }
    // если иллюстрация сцены хорошо попадает в интервал, возьмем его
    IScene scene = e.story().findBestSceneFor( aSvin.interval(), true );
    if( aSvin.interval().containsEx( scene.frame().secNo(), 2, 2 ) ) {
      return scene.frame();
    }
    // попробуем кадр камерой иллюстрации сцены в интервале
    IFrame foundFrame = chooseBestFrameWithCam( aSvin, scene.frame().cameraId() );
    if( foundFrame != null ) {
      return foundFrame;
    }
    // попробуем кадр любой камерой
    foundFrame = chooseBestFrameWithCam( aSvin, IStridable.NONE_ID );
    if( foundFrame != null ) {
      return foundFrame;
    }
    // тогда возьмем средний НЕ анимированный кадр
    String camId = scene.frame().cameraId();
    if( camId.equals( IStridable.NONE_ID ) ) {
      camId = e.frame().cameraId();
      if( camId.equals( IStridable.NONE_ID ) ) {
        IStringMap<ISourceVideo> esv = unitSourceVideos.episodeSourceVideos( e.id() );
        if( !esv.isEmpty() ) {
          camId = esv.values().first().cameraId();
        }
      }
    }
    if( camId.equals( IStridable.NONE_ID ) ) {
      return IFrame.NONE;
    }
    int medianSecNo = aSvin.interval().start() + aSvin.interval().duration() / 2;
    return new Frame( e.id(), camId, FPS * medianSecNo, false );
  }

  private IFrame chooseBestFrameWithCam( Svin aChunk, String aCamId ) {
    // Svin criteriaSvin = Svin.replaceCamId( aChunk, aCamId );
    // FIXME what to do ?
    // IList<IFrame> frames =
    // cofsFrames.listEpisodeFrames( new FrameSelectionCriteria( criteriaSvin, EAnimationKind.ANIMATED, false ) );
    ICofsFrames cofsFrames = tsContext().get( ICofsFrames.class );
    IFramesSet frames = cofsFrames.listEpisodeFrames( aChunk.episodeId() );
    int medianSecNo = aChunk.interval().start() + aChunk.interval().duration() / 2;
    int deltaFromMedian = Integer.MAX_VALUE;
    IFrame foundFrame = null;
    for( IFrame f : frames.items() ) {
      int delta = Math.abs( f.secNo() - medianSecNo );
      if( delta < deltaFromMedian ) {
        deltaFromMedian = delta;
        foundFrame = f;
      }
    }
    return foundFrame;
  }

}
