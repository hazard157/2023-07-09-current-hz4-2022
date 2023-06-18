package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.cofs.impl.IPsxCofsInternalConstants.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * {@link IPsxCofs} implementation.
 *
 * @author hazard157
 */
public class PsxCofs
    implements IPsxCofs {

  private final ICofsFrames cofsFrames;

  /**
   * Constructor.
   */
  public PsxCofs() {
    cofsFrames = new CofsFrames( COFS_EPISODES_ROOT );
  }

  // ------------------------------------------------------------------------------------
  // IPsxCofs
  //

  @Override
  public File cofsRoot() {
    return COFS_ROOT;
  }

  @Override
  public ICofsFrames cofsFrames() {
    return cofsFrames;
  }

  @Override
  public IList<File> listEpisodeKdenliveProjects( String aEpisodeId ) {
    LocalDate epDate = EPsxIncidentKind.EPISODE.id2date( aEpisodeId );
    File epDir = new File( COFS_EPISODES_ROOT, epDate.toString() );
    if( !TsFileUtils.isDirReadable( epDir ) ) {
      return IList.EMPTY;
    }
    IList<File> allProjFiles = TsFileUtils1.collectFilesInSubtree( epDir, new SingleStringList( KDENLIVE_EXT ) );
    if( allProjFiles.isEmpty() ) {
      return IList.EMPTY;
    }
    IListEdit<File> nonBackupProjFiles = new ElemArrayList<>();
    for( File f : allProjFiles ) {
      if( !f.getName().contains( BACKUP_STRING_IN_KDENLIVE_FILE_NAME ) ) {
        nonBackupProjFiles.add( f );
      }
    }
    return nonBackupProjFiles;
  }

  @Override
  public File findSourceVideoFile( String aSourceVideoId ) {
    String episodeId = SourceVideoUtils.extractEpisodeId( aSourceVideoId );
    LocalDate epDate = EPsxIncidentKind.EPISODE.id2date( episodeId );
    File epDir = new File( COFS_EPISODES_ROOT, epDate.toString() );
    File svDir = new File( epDir, SUBDIR_EP_SOURCE_VIDEOS );
    if( TsFileUtils.isDirReadable( svDir ) ) {
      String cameraId = SourceVideoUtils.extractCamId( aSourceVideoId );
      for( String ext : VIDEO_FILE_EXTENSIONS ) {
        File srcVideoFile = new File( svDir, cameraId + CHAR_EXT_SEPARATOR + ext );
        if( srcVideoFile.exists() ) {
          return srcVideoFile;
        }
      }
    }
    return null;
  }

}
