package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.cofs.ICofsFilmsConstants.*;

import java.io.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.opfil.*;
import com.hazard157.prisex24.cofs.*;

/**
 * {@link ICofsFilms} implementation.
 *
 * @author hazard157
 */
public class CofsFilms
    implements ICofsFilms {

  // TODO check constants to placed and/or taken from other interfaces

  private static final File   FILMS_ROOT           = new File( "/home/hmade/cofs/media/films/" );           //$NON-NLS-1$
  private static final File   FILMS_SUMMARIES_ROOT = new File( "/home/hmade/cofs/cache/films-summaries/" ); //$NON-NLS-1$
  private static final File   FILMS_KDENLIVES_ROOT = new File( "/home/hmade/cofs/devel/films/" );           //$NON-NLS-1$
  private static final String SUBDIR_LEGACY        = "legacy";                                              //$NON-NLS-1$

  /**
   * Constructor.
   */
  public CofsFilms() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static File filmGifFile( File aFilmFile ) {
    String fileName = TsFileUtils.extractFileName( aFilmFile.getName() );
    return new File( FILMS_SUMMARIES_ROOT, fileName + GIF_DOT_EXT );
  }

  // ------------------------------------------------------------------------------------
  // ICofsFilms
  //

  @Override
  public IList<IOptedFile> listFilms( IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    IListBasicEdit<IOptedFile> result = new SortedElemLinkedBundleList<>();
    if( OPDEF_FILMS_QP_INCLUDE_LEGACY.getValue( aParams ).asBool() ) {
      File legacyDir = new File( FILMS_ROOT, SUBDIR_LEGACY );
      result.addAll( OptedFileUtils.list( legacyDir, IMediaFileConstants.FF_VIDEOS ) );
    }
    result.addAll( OptedFileUtils.list( FILMS_ROOT, IMediaFileConstants.FF_VIDEOS ) );
    return result;
  }

  @Override
  public File getSummaryGif( IOptedFile aFilmFile ) {
    TsNullArgumentRtException.checkNull( aFilmFile );
    File gifFile = filmGifFile( aFilmFile.file() );
    return PsxCofsUtils.ensureSummaryGif( aFilmFile, gifFile );
  }

  @Override
  public IList<File> listKdenliveProjectFiles( File aFilmFile ) {
    TsNullArgumentRtException.checkNull( aFilmFile );
    String bareName = TsFileUtils.extractBareFileName( aFilmFile.getName() );
    File kDir = FILMS_KDENLIVES_ROOT;
    // there may be several projects with the same names in file system subtree
    IList<File> allKdenliveFiles = TsFileUtils1.collectFilesInSubtree( kDir, FF_KDENLIVE_PROJS );
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
