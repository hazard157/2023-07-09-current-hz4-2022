package com.hazard157.psx24.explorer.e4.uiparts;

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
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.currep.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.explorer.e4.services.*;
import com.hazard157.psx24.explorer.gui.resview.*;
import com.hazard157.psx24.explorer.pq.*;

/**
 * Вью отображения результатов выборки.
 *
 * @author hazard157
 */
public class UipartPqResultsViewer
    extends MwsAbstractPart {

  @SuppressWarnings( "unchecked" )
  private final ICurrentEntityChangeListener<PqResultSet> resultChangedListener = aCurrent -> {
    if( aCurrent != null ) {
      this.viewer.setPqResults( aCurrent );
      if( this.unitEpisodes == null ) {
        return;
      }
      IListBasicEdit<IFrame> toShow = new ElemLinkedBundleList<>();
      for( Svin chunk : aCurrent.listAllSvins() ) {
        IFrame f = chooseBestFrameForSvin( chunk );
        if( f != IFrame.NONE ) {
          toShow.add( f );
        }
      }
      this.currentFramesListService.setCurrent( toShow );
      IList<Svin> selSvins = this.viewer.getSelectionSvins();
      if( selSvins.isEmpty() ) { // если нет выбранных, покажем ВСН Svin-ы
        this.psxSelectedSvinsService.setSvins( this.viewer.getAllSvins() );
      }
      else {
        this.psxSelectedSvinsService.setSvins( selSvins );
      }
    }
    else {
      this.viewer.setPqResults( PqResultSet.EMPTY );
      this.psxSelectedSvinsService.setSvins( IList.EMPTY );
    }
  };

  private final ITsSelectionChangeListener<Svin> selectionChangeListener = ( aSource, aSelectedItem ) -> {
    this.currentFramesListService.setCurrentAsSvins( this.viewer.getSelectionSvins(), true );
    this.psxSelectedSvinsService.setSvins( this.viewer.getSelectionSvins() );
  };

  @Inject
  ICurrentFramesListService currentFramesListService;

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

  @Inject
  IPsxFileSystem fileSystem;

  ResultsPanelAsSimpleList viewer;

  @Override
  protected void doInit( Composite aParent ) {
    currentPqResultService.addCurrentEntityChangeListener( resultChangedListener );
    currentFramesListService.addTsSelectionListener(
        ( aSource, aSelectedItem ) -> whenSelectionChangedInCurrenyFramesListViewer( aSelectedItem ) );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    viewer = new ResultsPanelAsSimpleList( aParent, ctx );
    viewer.addTsSelectionListener( selectionChangeListener );
    // покажем все эпизоды
    PqResultSet rs = PqResultSet.createFull( unitEpisodes );
    viewer.setPqResults( rs );
  }

  void whenSelectionChangedInCurrenyFramesListViewer( IFrame aFrame ) {
    // обрабатываем, только если мы сами видимы
    if( !isPartVisible() || aFrame == null ) {
      return;
    }
    currentEpisodeService.setCurrent( unitEpisodes.items().findByKey( aFrame.episodeId() ) );
    PqResultSet rs = viewer.getPqResults();
    IList<Svin> svins = rs.epinsMap().findByKey( aFrame.episodeId() );
    if( svins == null ) {
      return;
    }
    // сначала поищем точное попадание в интервал
    for( Svin s : svins ) {
      if( s.interval().contains( aFrame.secNo() ) ) {
        viewer.setSelectedItem( s );
        return;
      }
    }
  }

  IFrame chooseBestFrameForSvin( Svin aSvin ) {
    IEpisode e = unitEpisodes.items().findByKey( aSvin.episodeId() );
    if( e == null ) {
      return IFrame.NONE;
    }
    // если иллюстрация сцены хорошо попадает в интервал, возьмем его
    IScene scene = e.story().findBestSceneFor( aSvin.interval(), true );
    if( SecintUtils.containsEx( aSvin.interval(), scene.frame().secNo(), 2, 2 ) ) {
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
    Svin criteriaSvin = Svin.replaceCamId( aChunk, aCamId );
    IList<IFrame> frames =
        fileSystem.listEpisodeFrames( new FrameSelectionCriteria( criteriaSvin, EAnimationKind.ANIMATED, false ) );
    int medianSecNo = aChunk.interval().start() + aChunk.interval().duration() / 2;
    int deltaFromMedian = Integer.MAX_VALUE;
    IFrame foundFrame = null;
    for( IFrame f : frames ) {
      int delta = Math.abs( f.secNo() - medianSecNo );
      if( delta < deltaFromMedian ) {
        deltaFromMedian = delta;
        foundFrame = f;
      }
    }
    return foundFrame;
  }

}
