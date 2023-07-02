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
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.common.utils.mop.*;
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

  private final ICofsFrames   cofsFrames;
  private final ICofsFilms    cofsFilms;
  private final ICofsTrailers cofsTrailers;
  private final ICofsGazes    cofsGazes;
  private final ICofsMingles  cofsMingles;

  /**
   * Constructor.
   */
  public PsxCofs() {
    cofsFrames = new CofsFrames( COFS_EPISODES_ROOT );
    cofsFilms = new CofsFilms();
    cofsTrailers = new CofsTrailers();
    cofsGazes = new CofsGazes();
    cofsMingles = new CofsMingles();
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
  public ICofsFilms cofsFilms() {
    return cofsFilms;
  }

  @Override
  public ICofsTrailers cofsTrailers() {
    return cofsTrailers;
  }

  @Override
  public ICofsGazes cofsGazes() {
    return cofsGazes;
  }

  @Override
  public ICofsMingles cofsMingles() {
    return cofsMingles;
  }

  @Override
  public File ensureSummaryImage( OptedFile aMediaFile ) {
    TsNullArgumentRtException.checkNull( aMediaFile );
    EMediaFileKind fileKind = EMediaFileKind.determineFileKind( aMediaFile.file() );
    switch( fileKind ) {
      case ANIMATED:
      case STILL: {
        if( TsFileUtils.isFileReadable( aMediaFile.file() ) ) {
          return aMediaFile.file();
        }
        break;
      }
      case VIDEO: {
        String mediaFileAbsDir = aMediaFile.file().getParentFile().getAbsolutePath();
        String gifAbsDir = TsFileUtils.removeEndingSeparator( COFS_CACHE_SUMMARIES_ROOT.getAbsolutePath() )
            + File.separator + TsFileUtils.removeEndingSeparator( mediaFileAbsDir ) + File.separator;
        File gifFile = new File( gifAbsDir, aMediaFile.file().getName() + GIF_DOT_EXT );
        return PsxCofsUtils.ensureSummaryGif( aMediaFile, gifFile );
      }
      case AUDIO: {
        return SPEC_IMAGE_MFK_AUDIO_FILE;
      }
      case OTHER: {
        return SPEC_IMAGE_MFK_OTHER_FILE;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( fileKind.id() );
    }
    return null;
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
