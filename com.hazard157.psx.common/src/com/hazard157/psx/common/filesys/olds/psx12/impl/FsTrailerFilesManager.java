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
 * {@link IFsTrailerFilesManager} implementation.
 *
 * @author hazard157
 */
class FsTrailerFilesManager
    implements IFsTrailerFilesManager {

  @SuppressWarnings( "unused" )
  private final PsxFileSystem fileSystem;

  /**
   * The constructor.
   *
   * @param aFileSystem {@link PsxFileSystem} - owner service
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  FsTrailerFilesManager( PsxFileSystem aFileSystem ) {
    fileSystem = TsNullArgumentRtException.checkNull( aFileSystem );
  }

  // ------------------------------------------------------------------------------------
  // IPsxTrailerFilesManager
  //

  @Override
  public File findTrailerFile( String aTrailerId ) {
    TsNullArgumentRtException.checkNull( aTrailerId );
    String episodeId = TrailerUtils.extractEpisodeId( aTrailerId );
    LocalDate episodeDate = EpisodeUtils.episodeId2LocalDate( episodeId );
    File epDir = new File( EPISODES_RESOURCES_ROOT, episodeDate.toString() );
    File subDir = new File( epDir, EPSUBDIR_TRAILERS );
    if( subDir.exists() ) {
      String localId = TrailerUtils.extractLocalId( aTrailerId );
      for( String ext : VIDEO_FILE_EXTENSIONS ) {
        File trailerFile = new File( subDir, localId + CHAR_EXT_SEPARATOR + ext );
        if( trailerFile.exists() ) {
          return trailerFile;
        }
      }
    }
    return null;
  }

}
