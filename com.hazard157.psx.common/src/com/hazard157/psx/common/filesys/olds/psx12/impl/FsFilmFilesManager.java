package com.hazard157.psx.common.filesys.olds.psx12.impl;

import static com.hazard157.psx.common.filesys.olds.psx12.films.IFsFilmFilesManagerConstants.*;
import static com.hazard157.psx.common.filesys.olds.psx12.impl.IPsxResources.*;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.e4.core.contexts.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.incub.optedfile.*;
import com.hazard157.psx.common.filesys.olds.psx12.*;
import com.hazard157.psx.common.filesys.olds.psx12.films.*;

/**
 * {@link IFsMediaPlayer} implementation.
 *
 * @author hazard157
 */
class FsFilmFilesManager
    implements IFsFilmFilesManager {

  private static final String FILMS_IN_PSX_ROOT = "media/films"; //$NON-NLS-1$

  private static final String SUBDIR_THUMBS   = "thumbs"; //$NON-NLS-1$
  private static final String SUBDIR_BIN      = "bin";    //$NON-NLS-1$
  private static final String SUBDIR_LEGACY   = "legacy"; //$NON-NLS-1$
  private static final String SUBDIR_KDENLIVE = "devel";  //$NON-NLS-1$

  private static final String GIF_CREATE_SH    = "make-film-thumb.sh"; //$NON-NLS-1$
  private static final String GIF_DOT_EXT      = ".gif";               //$NON-NLS-1$
  private static final String KDENLIVE_DOT_EXT = ".kdenlive";          //$NON-NLS-1$

  private final File filmsRootDir;

  private final PsxFileSystem   fileSystem;
  private final IEclipseContext winContext;

  FsFilmFilesManager( PsxFileSystem aFileSystem ) {
    fileSystem = aFileSystem;
    winContext = fileSystem.winContext();
    filmsRootDir = new File( fileSystem.psxRootDir(), FILMS_IN_PSX_ROOT );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private File filmGifFile( File aFilmFile ) {
    String fileName = TsFileUtils.extractFileName( aFilmFile.getName() );
    File thumbsDir = new File( filmsRootDir, SUBDIR_THUMBS );
    return new File( thumbsDir, fileName + GIF_DOT_EXT );
  }

  private void createFilmGif( File aFilmFile ) {
    File binDir = new File( filmsRootDir, SUBDIR_BIN );
    File shFile = new File( binDir, GIF_CREATE_SH );
    if( !shFile.exists() ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_NO_SH_FILE, shFile.getAbsolutePath() );
      return;
    }
    IRunnableWithProgress thumbCreator = aMonitor -> {
      String s = String.format( FMT_CREATING_FILM_THUMB, aFilmFile.getName() );
      aMonitor.beginTask( s, IProgressMonitor.UNKNOWN );
      TsMiscUtils.runTool( shFile.getAbsolutePath(), aFilmFile.getAbsolutePath() );
    };
    Shell shell = winContext.get( Shell.class );
    ProgressMonitorDialog d = new ProgressMonitorDialog( shell );
    try {
      d.run( true, false, thumbCreator );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( shell, ex );
    }
  }

  private File ensureFilmGif( File aFilmFile ) {
    File gifFile = filmGifFile( aFilmFile );
    // если нет, или устарел, пересохдает GIF-файл
    if( !gifFile.exists() || gifFile.lastModified() < aFilmFile.lastModified() ) {
      createFilmGif( aFilmFile );
    }
    if( !gifFile.exists() ) {
      return null;
    }
    return gifFile;
  }

  // ------------------------------------------------------------------------------------
  // IFsFilmFilesManager
  //

  @Override
  public File getRootDir() {
    return filmsRootDir;
  }

  @Override
  public IList<OptedFile> listFilms( IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    IListBasicEdit<OptedFile> result = new SortedElemLinkedBundleList<>();
    if( QUERY_PARAM_IS_LEGACY_FILEMS_INCLUDED.getValue( aParams ).asBool() ) {
      File legacyDir = new File( filmsRootDir, SUBDIR_LEGACY );
      result.addAll( OptedFile.list( legacyDir, IMediaFileConstants.VIDEO_FILES_FILTER ) );
    }
    result.addAll( OptedFile.list( filmsRootDir, IMediaFileConstants.VIDEO_FILES_FILTER ) );
    return result;
  }

  @Override
  public TsImage loadFilmThumb( File aFilmFile, EThumbSize aThumbSize ) {
    if( aFilmFile == null ) {
      return null;
    }
    File gifFile = ensureFilmGif( aFilmFile );
    if( gifFile == null ) {
      return null;
    }
    ITsImageManager imageManager = winContext.get( ITsImageManager.class );
    return imageManager.findThumb( gifFile, aThumbSize );
  }

  @Override
  public File kdenliveProjectFile( File aFilmFile ) {
    TsNullArgumentRtException.checkNull( aFilmFile );
    String bareName = TsFileUtils.extractBareFileName( aFilmFile.getName() );
    File kDir = new File( filmsRootDir, SUBDIR_KDENLIVE );
    return new File( kDir, bareName + KDENLIVE_DOT_EXT );
  }

}
