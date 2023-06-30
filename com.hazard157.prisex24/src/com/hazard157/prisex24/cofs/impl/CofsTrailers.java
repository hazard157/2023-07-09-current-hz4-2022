package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.cofs.impl.IPsxCofsInternalConstants.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * {@link ICofsTrailers} implementation.
 *
 * @author hazard157
 */
public class CofsTrailers
    implements ICofsTrailers {

  // TODO check constants to placed and/or taken from other interfaces
  private static final File TRAILERS_SUMMARIES_ROOT = new File( "/home/hmade/cofs/cache/trailers-summaries/" ); //$NON-NLS-1$

  /**
   * Constructor.
   */
  public CofsTrailers() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static String episodeIdFormTrailerFile( File aTrailerFile ) {
    TsNullArgumentRtException.checkNull( aTrailerFile );
    if( !TsFileUtils.isFileReadable( aTrailerFile ) ) {
      return null;
    }
    // extract episode ID from trailer file path
    String epDateStr = aTrailerFile.getParentFile().getName();
    LocalDate ld = EPsxIncidentKind.parseDate( epDateStr );
    if( ld == null ) {
      return null;
    }
    return EPsxIncidentKind.EPISODE.date2id( ld );
  }

  private static File episodeDir( String aEpisodeId ) {
    if( !EPsxIncidentKind.EPISODE.idStrValidator().isValid( aEpisodeId ) ) {
      return null;
    }
    LocalDate ld = EPsxIncidentKind.EPISODE.id2date( aEpisodeId );
    File epDir = new File( COFS_EPISODES_ROOT, ld.toString() );
    if( TsFileUtils.isDirReadable( epDir ) ) {
      return epDir;
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ICofsTrailers
  //

  @Override
  public IList<OptedFile> listEpisodeTrailerFiles( String aEpisodeId ) {
    File epDir = episodeDir( aEpisodeId );
    if( epDir == null ) {
      return IList.EMPTY;
    }
    File trDir = new File( epDir, SUBDIR_EP_TRAILERS );
    if( !TsFileUtils.isDirReadable( trDir ) ) {
      return IList.EMPTY;
    }
    // collect video files
    IListBasicEdit<OptedFile> result = new SortedElemLinkedBundleList<>();
    result.addAll( OptedFile.list( trDir, IMediaFileConstants.FF_VIDEOS ) );
    return result;
  }

  @Override
  public OptedFile findTrailerFile( String aEpisodeId, String aTrailerName ) {
    TsNullArgumentRtException.checkNulls( aEpisodeId, aTrailerName );
    for( OptedFile f : listEpisodeTrailerFiles( aEpisodeId ) ) {
      String bareName = TsFileUtils.extractBareFileName( f.file().getName() );
      if( bareName.equals( aTrailerName ) ) {
        return f;
      }
    }
    return null;
  }

  @Override
  public File getSummaryGif( OptedFile aTrailerFile ) {
    TsNullArgumentRtException.checkNull( aTrailerFile );
    // extract episode date from trailer file path
    String episodeId = episodeIdFormTrailerFile( aTrailerFile.file() );
    if( episodeId == null ) {
      return null;
    }
    LocalDate ld = EPsxIncidentKind.EPISODE.id2date( episodeId );
    // ensure summary GIF directory
    File gifsDir = new File( TRAILERS_SUMMARIES_ROOT, ld.toString() );
    String fileName = TsFileUtils.extractFileName( aTrailerFile.file().getName() );
    File gifFile = new File( gifsDir, fileName + GIF_DOT_EXT );
    // ensure GIF
    return PsxCofsUtils.ensureSummaryGif( aTrailerFile, gifFile );
  }

  @Override
  public IList<File> listKdenliveProjectFiles( File aTrailerFile ) {
    // find episode devel directory
    String episodeId = episodeIdFormTrailerFile( aTrailerFile );
    if( episodeId == null ) {
      return IList.EMPTY;
    }
    File epDir = episodeDir( episodeId );
    if( epDir == null ) {
      return IList.EMPTY;
    }
    File develDir = new File( epDir, SUBDIR_EP_DEVEL );
    if( !TsFileUtils.isDirReadable( develDir ) ) {
      return IList.EMPTY;
    }
    // there may be several projects with the same names in file system subtree
    String bareName = TsFileUtils.extractBareFileName( aTrailerFile.getName() );
    IList<File> allKdenliveFiles = TsFileUtils1.collectFilesInSubtree( develDir, FF_KDENLIVE_PROJS );
    IListBasicEdit<File> kk = new SortedElemLinkedBundleList<>();
    String projName = bareName + KDENLIVE_DOT_EXT;
    for( File f : allKdenliveFiles ) {
      if( f.getName().endsWith( projName ) ) {
        kk.add( f );
      }
    }
    return kk;
  }

}
