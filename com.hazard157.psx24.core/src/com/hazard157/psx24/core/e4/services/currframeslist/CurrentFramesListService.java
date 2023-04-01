package com.hazard157.psx24.core.e4.services.currframeslist;

import java.util.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Реализаци {@link ICurrentFramesListService}.
 *
 * @author hazard157
 */
public class CurrentFramesListService
    extends CurrentEntityService<IList<IFrame>>
    implements ICurrentFramesListService {

  private final TsSelectionChangeEventHelper<IFrame> selectionChangeEventHelper;
  private IFrame                                     selectedFrame = null;

  /**
   * Конструктор.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   * @throws TsNullArgumentRtException аргумент = null
   */
  public CurrentFramesListService( IEclipseContext aWinContext ) {
    super( aWinContext );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICurrentImagesListService
  //

  @Override
  public void setCurrentAsSvin( Svin aSvin, boolean aShowSomething ) {
    if( aSvin == null ) {
      setCurrent( null );
      return;
    }
    IPsxFileSystem fileSystem = appContext().get( IPsxFileSystem.class );
    IList<IFrame> frames =
        fileSystem.listEpisodeFrames( new FrameSelectionCriteria( aSvin, EAnimationKind.ANIMATED, true ) );
    // GOGA no IFsExplorer
    // if( frames.isEmpty() ) {
    // int frameSec = aSvin.interval().start() + aSvin.interval().duration() / 2;
    // IFsExplorer fe = appContext().get( IFsExplorer.class );
    // IStringMap<File> camDirs = fe.listSourceCameraDirs( aSvin.episodeId() );
    // if( !camDirs.isEmpty() ) {
    // Frame middleFrame = new Frame( aSvin.episodeId(), camDirs.values().first().getName(), FPS * frameSec, false );
    // frames = new SingleItemList<>( middleFrame );
    // }
    // }
    setCurrent( frames );
  }

  @Override
  public void setCurrentAsSvins( IList<Svin> aSvins, boolean aShowSomething ) {
    if( aSvins == null || aSvins.isEmpty() ) {
      setCurrent( null );
      return;
    }
    IPsxFileSystem fileSystem = appContext().get( IPsxFileSystem.class );
    IListBasicEdit<Svin> sortedSvins = new SortedElemLinkedBundleList<>();
    sortedSvins.addAll( aSvins );
    IListEdit<IFrame> result = new ElemLinkedBundleList<>();
    for( Svin s : sortedSvins ) {
      // IList<IFrame> frames =
      // fileSystem.listEpisodeFrames( new FrameSelectionCriteria( s, EAnimationKind.ANIMATED, true ) );
      // if( frames.isEmpty() ) {
      // int frameSec = s.interval().start() + s.interval().duration() / 2;
      // IFsExplorer fe = appContext().get( IFsExplorer.class );
      // IStringMap<File> camDirs = fe.listSourceCameraDirs( s.episodeId() );
      // if( !camDirs.isEmpty() ) {
      // Frame middleFrame = new Frame( s.episodeId(), camDirs.values().first().getName(), FPS * frameSec, false );
      // result.add( middleFrame );
      // }
      // }
      // else {
      // result.addAll( frames );
      // }
      result.addAll( fileSystem.listSvinIllustrationFrames( s ) );
    }
    setCurrent( result );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionProvider
  //

  @Override
  public IFrame selectedItem() {
    return selectedFrame;
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    if( !Objects.equals( aItem, selectedFrame ) ) {
      selectedFrame = aItem;
      selectionChangeEventHelper.fireTsSelectionEvent( selectedFrame );
    }
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IFrame> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IFrame> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

}
