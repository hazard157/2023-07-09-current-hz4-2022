package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.prisex24.cofs.impl.IPsxCofsInternalConstants.*;
import static com.hazard157.prisex24.cofs.impl.PsxCofsUtils.*;
import static com.hazard157.prisex24.cofs.l10n.IPsxCofsSharedResources.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.prisex24.cofs.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * {@link ICofsFrames} implementation.
 *
 * @author hazard157
 */
class CofsFrames
    implements ICofsFrames {

  // FIXME move to settings? argument of this service?
  private static final File NONSECS_ROOT = new File( "/home/.psx/episodes/frames-nonsec/" ); //$NON-NLS-1$

  /**
   * Episodes resources root directory or <code>null</code> is specified one is not accessible.
   */
  private final File epsRoot;

  /**
   * Cached episodes frames: the map "episode ID" - "episode frames".
   * <p>
   * Cache is updated in {@link #ensureEpisodeFrames(LocalDate)}.
   */
  private final IStringMapEdit<IFramesSet> cachedEpFrames = new SortedStringMap<>();

  /**
   * Constructor.
   *
   * @param aEpisodeResourcesRoot {@link File} - root directory of the episodes resources in the COFS root
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CofsFrames( File aEpisodeResourcesRoot ) {
    if( TsFileUtils.isDirReadable( aEpisodeResourcesRoot ) ) {
      epsRoot = aEpisodeResourcesRoot;
    }
    else {
      epsRoot = null;
      LoggerUtils.errorLogger().warning( FMT_WARN_NO_FRAMES_ROOT_DIR, aEpisodeResourcesRoot.getAbsolutePath() );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void ensureEpisodeFrames( LocalDate aEpisodeDate ) {
    String epId = EPsxIncidentKind.EPISODE.date2id( aEpisodeDate );
    if( cachedEpFrames.hasKey( epId ) ) {
      return;
    }
    String epDirName = aEpisodeDate.toString(); // YYYY-MM-DD
    File epDir = new File( epsRoot, epDirName );
    if( !TsFileUtils.isDirReadable( epDir ) ) {
      return;
    }
    // collect still & anim camera frames dirs
    File stillFramesRoot = new File( epDir, SUBDIR_EP_STILL_IMAGES );
    File animFramesRoot = new File( epDir, SUBDIR_EP_ANIM_IMAGES );
    IStringMap<File> mapCamDirsStill = listProbablyCamsSubdirs( stillFramesRoot );
    IStringMap<File> mapCamDirsAnim = listProbablyCamsSubdirs( animFramesRoot );
    if( mapCamDirsStill.isEmpty() && mapCamDirsAnim.isEmpty() ) {
      return;
    }
    // collect frames from camera directories
    IStringMapEdit<IListEdit<IFrame>> mapCamsFrames = new SortedStringMap<>();
    // still images
    for( String camId : mapCamDirsStill.keys() ) {
      File camDir = mapCamDirsStill.getByKey( camId );
      IListEdit<IFrame> llFrames = new ElemLinkedBundleList<>();
      IList<File> llFiles = TsFileUtils.listChilds( camDir, FF_FRAME_FILES );
      for( File file : llFiles ) {
        IFrame frame = PsxCofsUtils.makeSourceFrameFromFileName( file, camId, epId );
        llFrames.add( frame );
      }
      mapCamsFrames.put( camId, llFrames );
    }
    // anim images
    for( String camId : mapCamDirsAnim.keys() ) {
      File camDir = mapCamDirsAnim.getByKey( camId );
      IListEdit<IFrame> llFrames = mapCamsFrames.findByKey( camId );
      if( llFrames == null ) { // if cams list for anim and still images does not matches
        llFrames = new ElemLinkedBundleList<>();
        mapCamsFrames.put( camId, llFrames );
      }
      IList<File> llFiles = TsFileUtils.listChilds( camDir, FF_FRAME_FILES );
      for( File file : llFiles ) {
        IFrame frame = PsxCofsUtils.makeSourceFrameFromFileName( file, camId, epId );
        llFrames.add( frame );
      }
    }
    // make cache collections
    IListBasicEdit<IFrame> llCacheAllFrames = new SortedElemLinkedBundleList<>();
    IStringMapEdit<IList<IFrame>> mapCacheCamsFrames = new SortedStringMap<>();
    for( String camId : mapCamsFrames.keys() ) {
      IListBasicEdit<IFrame> llSortedCamFrames = new SortedElemLinkedBundleList<>();
      llSortedCamFrames.addAll( mapCamsFrames.getByKey( camId ) );
      llCacheAllFrames.addAll( llSortedCamFrames );
      mapCacheCamsFrames.put( camId, llSortedCamFrames );
    }
    cachedEpFrames.put( epId, new FramesSet( llCacheAllFrames ) );
  }

  private File internalFindSecAlignedStillFrame( IFrame aFrame ) {
    LocalDate ld = EPsxIncidentKind.EPISODE.id2date( aFrame.episodeId() );
    File epDir = new File( epsRoot, ld.toString() );
    File epFramesDir = new File( epDir, SUBDIR_EP_STILL_IMAGES );
    File epCamDir = new File( epFramesDir, aFrame.cameraId() );
    if( !TsFileUtils.isDirReadable( epCamDir ) ) {
      return null;
    }
    String bareName = PsxCofsUtils.bareSourceFrameFileName( aFrame.frameNo() );
    File frameFile = new File( epCamDir, bareName + '.' + STILL_IMAGE_FILE_EXT );
    if( frameFile.exists() ) {
      return frameFile;
    }
    return null;
  }

  private File internalFindGifFrame( IFrame aFrame ) {
    LocalDate ld = EPsxIncidentKind.EPISODE.id2date( aFrame.episodeId() );
    File epDir = new File( epsRoot, ld.toString() );
    File epFramesDir = new File( epDir, SUBDIR_EP_ANIM_IMAGES );
    File epCamDir = new File( epFramesDir, aFrame.cameraId() );
    if( !TsFileUtils.isDirReadable( epCamDir ) ) {
      return null;
    }
    String bareName = PsxCofsUtils.bareSourceFrameFileName( aFrame.frameNo() );
    File frameFile = new File( epCamDir, bareName + '.' + ANIM_IMAGE_FILE_EXT );
    if( frameFile.exists() ) {
      return frameFile;
    }
    return null;
  }

  private static File internalFindNonSecFrame( IFrame aFrame ) {
    LocalDate ld = EPsxIncidentKind.EPISODE.id2date( aFrame.episodeId() );
    File epFramesDir = new File( NONSECS_ROOT, ld.toString() );
    File epCamDir = new File( epFramesDir, aFrame.cameraId() );
    if( !TsFileUtils.isDirReadable( epCamDir ) ) {
      return null;
    }
    String bareName = PsxCofsUtils.bareSourceFrameFileName( aFrame.frameNo() );
    File frameFile = new File( epCamDir, bareName + '.' + STILL_IMAGE_FILE_EXT );
    if( frameFile.exists() ) {
      return frameFile;
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ICofsFrames
  //

  @Override
  public File findFrameFile( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    if( epsRoot == null ) {
      return null;
    }
    if( aFrame.frameNo() < 0 || !aFrame.isDefined() ) {
      return null;
    }
    if( aFrame.isAnimated() ) {
      return internalFindGifFrame( aFrame );
    }
    if( aFrame.isSecAligned() ) {
      return internalFindSecAlignedStillFrame( aFrame );
    }
    return internalFindNonSecFrame( aFrame );
  }

  @Override
  public IFramesSet listEpisodeFrames( String aEpisodeId ) {
    LocalDate epDate = EPsxIncidentKind.EPISODE.id2date( aEpisodeId );
    if( epsRoot == null ) {
      return IFramesSet.EMPTY;
    }
    ensureEpisodeFrames( epDate );
    IFramesSet frSet = cachedEpFrames.findByKey( aEpisodeId );
    if( frSet != null ) {
      return frSet;
    }
    return IFramesSet.EMPTY;
  }

}
