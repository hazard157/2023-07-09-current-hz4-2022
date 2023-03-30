package com.hazard157.psx.common.filesys.olds.psx12.impl;

import static com.hazard157.psx.common.filesys.olds.psx12.impl.PsxFileSystemUtils.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.filesys.olds.psx12.*;
import com.hazard157.psx.common.utils.*;

/**
 * {@link IFsSourceVideoFilesManager} implementation.
 *
 * @author goga
 */
class FsSourceVideoFilesManager
    implements IFsSourceVideoFilesManager {

  @SuppressWarnings( "unused" )
  private final PsxFileSystem fileSystem;

  /**
   * The constructor.
   *
   * @param aFileSystem {@link PsxFileSystem} - owner service
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  FsSourceVideoFilesManager( PsxFileSystem aFileSystem ) {
    fileSystem = TsNullArgumentRtException.checkNull( aFileSystem );
  }

  // ------------------------------------------------------------------------------------
  // IPsxSourceVideoFilesManager
  //

  @Override
  public File findSourceVideoFile( String aSourceVideoId ) {
    TsNullArgumentRtException.checkNull( aSourceVideoId );
    String episodeId = SourceVideoUtils.extractEpisodeId( aSourceVideoId );
    LocalDate episodeDate = EpisodeUtils.episodeId2LocalDate( episodeId );
    File epDir = new File( EPISODES_RESOURCES_ROOT, episodeDate.toString() );
    File subDir = new File( epDir, EPSUBDIR_SRCVIDEOS );
    if( subDir.exists() ) {
      String cameraId = SourceVideoUtils.extractCamId( aSourceVideoId );
      for( String ext : VIDEO_FILE_EXTENSIONS ) {
        File srcVideoFile = new File( subDir, cameraId + CHAR_EXT_SEPARATOR + ext );
        if( srcVideoFile.exists() ) {
          return srcVideoFile;
        }
      }
    }
    return null;
  }

}
