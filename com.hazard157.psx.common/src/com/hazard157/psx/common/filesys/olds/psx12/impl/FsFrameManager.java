package com.hazard157.psx.common.filesys.olds.psx12.impl;

import static com.hazard157.psx.common.filesys.olds.psx12.impl.IPsxResources.*;
import static com.hazard157.psx.common.filesys.olds.psx12.impl.PsxFileSystemUtils.*;
import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.filesys.olds.psx12.frames.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.sc.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.common.utils.ftstep.*;

/**
 * Реализация {@link IFsFrameManager}.
 * <p>
 * Данная реализация предполагает, что изображения кадров эпизодов в общем случае находятся <b>вне</b> общего корня
 * ресурсов приложения PRISEX. Скорее всего, где-то рядом с местом кеширования службы {@link ITsImageManager}.
 *
 * @author hazard157
 */
class FsFrameManager
    implements IFsFrameManager {

  private static final TsFileFilter FF_CAM_DIRS_FILTER = new TsFileFilter( EFsObjKind.DIR, IStringList.EMPTY ) {

    @Override
    public boolean accept( File aPathName ) {
      if( super.accept( aPathName ) ) {
        return StridUtils.isValidIdPath( aPathName.getName() );
      }
      return false;
    }
  };

  private final IStringMapEdit<IStringMap<IList<IFrame>>> cache = new StringMap<>();

  private final PsxFileSystem   fileSystem;
  private final ITsImageManager imageManager;

  /**
   * The constructor.
   *
   * @param aFileSystem {@link PsxFileSystem} - owner service
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FsFrameManager( PsxFileSystem aFileSystem ) {
    fileSystem = TsNullArgumentRtException.checkNull( aFileSystem );
    imageManager = fileSystem.winContext().get( ITsImageManager.class );
    TsInternalErrorRtException.checkNull( imageManager );
    if( !NONSEC_FRAMES_ROOT.exists() ) {
      LoggerUtils.errorLogger().error( FMT_ERR_INV_FRAMES_DIR, NONSEC_FRAMES_ROOT.getAbsolutePath() );
    }
    if( !EPISODES_RESOURCES_ROOT.exists() ) {
      LoggerUtils.errorLogger().error( FMT_ERR_INV_EPISODES_RESOURCES_DIR, EPISODES_RESOURCES_ROOT.getAbsolutePath() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static void internalAddFrameFilesFrom( LocalDate aEpisodeDate, EFrameFileLocation aLocation,
      IStringMapEdit<IListBasicEdit<IFrame>> aMap ) {
    String episodeId = EpisodeUtils.localDate2EpisodeId( aEpisodeDate );
    File epDir = aLocation.getDir( aEpisodeDate );
    IList<File> camDirs = TsFileUtils.listChilds( epDir, FF_CAM_DIRS_FILTER );
    for( File d : camDirs ) {
      String camId = d.getName();
      IListBasicEdit<IFrame> ll = aMap.findByKey( camId );
      IList<File> frameFiles = TsFileUtils.listChildsSorted( d, aLocation.getImageFileFilter() );
      if( ll == null ) {
        ll = new ElemArrayList<>( frameFiles.size() );
        aMap.put( camId, ll );
      }
      for( File file : frameFiles ) {
        IFrame f = PsxFileSystemUtils.makeSourceFrameFromFileName( file, camId, episodeId );
        ll.add( f );
      }
    }
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private static IStringMap<IList<IFrame>> internalReadEpisodeFrames( LocalDate aEpisodeDate ) {
    IStringMapEdit<IListBasicEdit<IFrame>> map = new StringMap<>();
    internalAddFrameFilesFrom( aEpisodeDate, EFrameFileLocation.NOT_ALIGNED, map );
    internalAddFrameFilesFrom( aEpisodeDate, EFrameFileLocation.SEC_ALIGNED, map );
    internalAddFrameFilesFrom( aEpisodeDate, EFrameFileLocation.ANIM, map );
    return (IStringMap)map;
  }

  IStringMap<IList<IFrame>> getCachedEpisodeFrames( LocalDate aEpisodeDate ) {
    String epDate = aEpisodeDate.toString();
    IStringMap<IList<IFrame>> map = cache.findByKey( epDate );
    if( map == null ) {
      map = internalReadEpisodeFrames( aEpisodeDate );
      cache.put( epDate, map );
    }
    return map;
  }

  void internalSelectFrames( ISingleCriterion aCriterion, IListBasicEdit<IFrame> aList, boolean aForcedFrame ) {
    LocalDate epDate = EpisodeUtils.episodeId2LocalDate( aCriterion.episodeId() );
    int count = 0;
    for( IList<IFrame> ll : getCachedEpisodeFrames( epDate ) ) {
      for( IFrame f : ll ) {
        if( aCriterion.accept( f ) ) {
          aList.add( f );
          ++count;
        }
      }
    }
    // при aForcedFrame = true, если ни одного кадра не добавлено, то добавим любой из интервала
    if( count == 0 && aForcedFrame ) {
      ISingleCriterion sc = SingleCriterion.createSecAligned( aCriterion.episodeId(), aCriterion.cameraId(),
          aCriterion.interval(), EAnimationKind.SINGLE, ESecondsStep.SEC_01 );
      for( IList<IFrame> ll : getCachedEpisodeFrames( epDate ) ) {
        for( IFrame f : ll ) {
          if( sc.accept( f ) ) {
            aList.add( f );
            break;
          }
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IPsxFrameManager
  //

  @Override
  public File findFrameFile( IFrame aFrame ) {
    File f = makeFrameFile( aFrame );
    if( f.exists() ) {
      return f;
    }
    return null;
  }

  @Override
  public TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFrame, aThumbSize );
    File frameFile = findFrameFile( aFrame );
    if( frameFile == null ) {
      return null;
    }
    return imageManager.findThumb( frameFile, aThumbSize );
  }

  @Override
  public File makeFrameFile( IFrame aFrame ) {
    EFrameFileLocation ffl = EFrameFileLocation.locationOf( aFrame );
    return ffl.getFile( aFrame );
  }

  @Override
  public IList<IFrame> selectFrames( ISingleCriterion aCriterion, boolean aForcedFrame ) {
    IListBasicEdit<IFrame> result =
        new SortedElemLinkedBundleList<>( getListInitialCapacity( estimateOrder( 30_000 ) ), true );
    internalSelectFrames( aCriterion, result, aForcedFrame );
    return result;
  }

  @Override
  public IList<IFrame> selectFrames( IList<ISingleCriterion> aCriteria, boolean aForcedFrame ) {
    TsNullArgumentRtException.checkNull( aCriteria );
    IListBasicEdit<IFrame> result =
        new SortedElemLinkedBundleList<>( getListInitialCapacity( estimateOrder( 30_000 ) ), true );
    for( ISingleCriterion c : aCriteria ) {
      internalSelectFrames( c, result, aForcedFrame );
    }
    return result;
  }

}
