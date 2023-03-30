package com.hazard157.psx.common.filesys.impl;

import static com.hazard157.psx.common.filesys.IPfsConfig.*;
import static com.hazard157.psx.common.filesys.impl.PfsUtils.*;

import java.io.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IPfsFramesCache} implementation.
 *
 * @author hazard157
 */
public class PfsFramesCache
    implements IPfsFramesCache {

  /**
   * Map "Episode ID" - "Sorted list of all frames".
   */
  private final IStringMapEdit<IList<IFrame>> epMap = new StringMap<>();

  /**
   * Map of maps "Episode ID" - "Camera ID" - "Sorted list of camera all frames".
   */
  private final IStringMapEdit<IStringMapEdit<IList<IFrame>>> camsMap = new StringMap<>();

  /**
   * Constructor.
   */
  public PfsFramesCache() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation

  private static void addCamDirsFromEpisodeSubdir( File aFramesSubdir, IListBasicEdit<File> aList ) {
    // найдем все камера-подобные (по названию) поддиректории maybeCamSubdirs
    File[] allSubdirs = aFramesSubdir.listFiles( TsFileFilter.FF_DIRS );
    if( allSubdirs == null || allSubdirs.length == 0 ) {
      return;
    }
    for( File f : allSubdirs ) {
      if( StridUtils.isValidIdPath( f.getName() ) ) {
        aList.add( f );
      }
    }
  }
  //

  private final static IList<File> listMaybeFrameDirs( String aEpisodeId ) {
    IListEdit<File> maybeCamSubdirs = new ElemArrayList<>();
    File frDir;
    frDir = pfsFindSourceEpisodeSubdir( aEpisodeId, EPSUBDIR_FRAMES_ANIM );
    if( frDir != null ) {
      addCamDirsFromEpisodeSubdir( frDir, maybeCamSubdirs );
    }
    /**
     * Если нет директории из не-облачной части, то включим директори из облачной части, чтобы хотя бы секундные кадры
     * были в результате.
     */
    File frDirSecs = pfsFindSourceEpisodeSubdir( aEpisodeId, EPSUBDIR_FRAMES_SECS );
    File frDirAllStill = pfsFindNonSecFramesEpisodeDir( aEpisodeId );
    if( frDirAllStill != null ) {
      frDir = frDirAllStill;
    }
    else {
      frDir = frDirSecs;
    }
    if( frDir != null ) {
      addCamDirsFromEpisodeSubdir( frDir, maybeCamSubdirs );
    }
    return maybeCamSubdirs;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private boolean ensureEpisodeFrames( String aEpisodeId ) {
    // START --- проврки наличия обязательных кадров: всех ANIM и секундно выровненных STILL
    // найдем директорию эпизода
    File epDir = pfsFindSourceEpisodeDir( aEpisodeId );
    // при необходимусти удалить из кеша вхождение отсутствующего эпизода
    if( epDir == null ) {
      epMap.removeByKey( aEpisodeId );
      camsMap.removeByKey( aEpisodeId );
      return false;
    }
    // проверим, что кеш уже создан
    if( epMap.hasKey( aEpisodeId ) ) {
      return true;
    }
    // --- END
    // соберем все кадры (ANIM - из облачной части, а STILL - из не-облачной)
    IList<File> maybeCamSubdirs = listMaybeFrameDirs( aEpisodeId );
    IStringMapEdit<IListBasicEdit<IFrame>> epFramesByCamMap = new StringMap<>(); // карта кадров по камерам
    for( File probableCamSubdir : maybeCamSubdirs ) {
      String camId = probableCamSubdir.getName();
      IListBasicEdit<IFrame> camFrames = epFramesByCamMap.findByKey( camId );
      if( camFrames == null ) {
        camFrames = new SortedElemLinkedBundleList<>();
        epFramesByCamMap.put( camId, camFrames );
      }
      File[] allImages = probableCamSubdir.listFiles( IMediaFileConstants.IMAGE_FILES_FILTER );
      for( File imgFile : allImages ) {
        IFrame f = PfsUtils.makeSourceFrameFromFileName( imgFile, camId, aEpisodeId );
        if( f != null ) {
          camFrames.add( f );
        }
      }
    }
    IListBasicEdit<IFrame> episodeAllFrames = new SortedElemLinkedBundleList<>();
    for( String camId : epFramesByCamMap.keys() ) {
      episodeAllFrames.addAll( epFramesByCamMap.getByKey( camId ) );
    }
    epMap.put( aEpisodeId, episodeAllFrames );
    camsMap.put( aEpisodeId, (IStringMapEdit)epFramesByCamMap );
    return true;
  }

  private boolean ensureCameraFrames( String aEpisodeId, String aCamId ) {
    if( ensureEpisodeFrames( aEpisodeId ) ) {
      return camsMap.getByKey( aEpisodeId ).hasKey( aCamId );
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // IPfsFramesCache
  //

  @Override
  public IList<IFrame> listEpisodeFrames( String aEpisodeId ) {
    if( !ensureEpisodeFrames( aEpisodeId ) ) {
      return IList.EMPTY;
    }
    return epMap.getByKey( aEpisodeId );
  }

  @Override
  public IList<IFrame> listCamFrames( String aEpisodeId, String aCamId ) {
    if( !ensureCameraFrames( aEpisodeId, aCamId ) ) {
      return IList.EMPTY;
    }
    return camsMap.getByKey( aEpisodeId ).getByKey( aCamId );
  }

  @Override
  public void updateFrameFile( IFrame aFrame ) {
    // update cache if frame file exists and not in cache., remove if not exists
    boolean isFrameFile = (findFrameFile( aFrame ) != null);
    IListBasicEdit<IFrame> epFrames = (IListBasicEdit<IFrame>)epMap.findByKey( aFrame.episodeId() );
    if( epFrames != null ) {
      epFrames.remove( aFrame );
      if( isFrameFile ) {
        epFrames.add( aFrame );
      }
    }
    IStringMapEdit<IList<IFrame>> epCamsMap = camsMap.findByKey( aFrame.episodeId() );
    if( epCamsMap != null ) {
      IListBasicEdit<IFrame> camFrames = (IListBasicEdit<IFrame>)epCamsMap.findByKey( aFrame.episodeId() );
      if( camFrames != null ) {
        camFrames.remove( aFrame );
        if( isFrameFile ) {
          camFrames.add( aFrame );
        }
      }
    }
  }

}
