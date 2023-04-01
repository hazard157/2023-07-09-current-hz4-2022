package com.hazard157.psx.common.filesys.olds.psx12.impl;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.filesys.impl.*;
import com.hazard157.psx.common.filesys.olds.psx12.*;
import com.hazard157.psx.common.filesys.olds.psx12.films.*;
import com.hazard157.psx.common.filesys.olds.psx12.frames.*;

/**
 * Implementation of the {@link IPsxFileSystem}.
 *
 * @author hazard157
 */
public class PsxFileSystem
    implements IPsxFileSystem {

  private static final String ROOT_DIR = "/home/hmade/"; //$NON-NLS-1$

  private static final String ORIGINAL_MEDIA_ROOT = ROOT_DIR + "mastering/original/"; //$NON-NLS-1$
  private static final String RESOURCES_ROOT      = ROOT_DIR + "resources/";          //$NON-NLS-1$

  private final IPfsOriginalMedia          originalMedia;
  private final IFsFrameManager            frameManager;
  private final IFsMediaPlayer             mediaPlayer;
  private final IFsFilmFilesManager        filmFilesManager;
  private final IFsSourceVideoFilesManager sourceVideoFilesManager;
  private final IFsTrailerFilesManager     trailerFilesManager;

  private final File rootDir;

  private final File resurcesRootDir;

  private final IEclipseContext winContext;

  /**
   * The constructor.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PsxFileSystem( IEclipseContext aWinContext ) {
    rootDir = new File( ROOT_DIR );
    winContext = TsNullArgumentRtException.checkNull( aWinContext );
    originalMedia = new PfsOriginalMediaExisting( new File( ORIGINAL_MEDIA_ROOT ) );
    frameManager = new FsFrameManager( this );
    mediaPlayer = new FsMediaPlayer( this );
    filmFilesManager = new FsFilmFilesManager( this );
    sourceVideoFilesManager = new FsSourceVideoFilesManager( this );
    trailerFilesManager = new FsTrailerFilesManager( this );
    resurcesRootDir = new File( RESOURCES_ROOT );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  IEclipseContext winContext() {
    return winContext;
  }

  // ------------------------------------------------------------------------------------
  // IPrisexFileSystem
  //

  @Override
  public File psxRootDir() {
    return rootDir;
  }

  @Override
  public IPfsOriginalMedia originalMedia() {
    return originalMedia;
  }

  @Override
  public IFsFrameManager frameManager() {
    return frameManager;
  }

  @Override
  public IFsMediaPlayer mediaPlayer() {
    return mediaPlayer;
  }

  @Override
  public IFsSourceVideoFilesManager sourceVideoFilesManager() {
    return sourceVideoFilesManager;
  }

  @Override
  public IFsTrailerFilesManager trailerFilesManager() {
    return trailerFilesManager;
  }

  @Override
  public IFsFilmFilesManager filmFilesManager() {
    return filmFilesManager;
  }

  @Override
  public File getResourcesRootDir() {
    return resurcesRootDir;
  }

}
