package com.hazard157.psx.common.filesys.olds.psx12;

import java.io.*;

import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.filesys.olds.psx12.films.*;
import com.hazard157.psx.common.filesys.olds.psx12.frames.*;

/**
 * Access to PRISEX files.
 *
 * @author hazard157
 */
public interface IPsxFileSystem {

  /**
   * Returns PSX file resources root directory.
   *
   * @return {@link File} - PSX root directory
   */
  File psxRootDir();

  /**
   * Returns means to access to the original media files.
   *
   * @return {@link IPfsOriginalMedia} - original media files
   */
  IPfsOriginalMedia originalMedia();

  /**
   * Returns the frame files manager.
   *
   * @return {@link IFsFrameManager} - the frame files manager
   */
  IFsFrameManager frameManager();

  /**
   * Returns PSX media resources player.
   *
   * @return {@link IFsMediaPlayer} - PSX media player
   */
  IFsMediaPlayer mediaPlayer();

  /**
   * Returns the source video files manager.
   *
   * @return {@link IFsSourceVideoFilesManager} - the source video files manager
   */
  IFsSourceVideoFilesManager sourceVideoFilesManager();

  /**
   * Returns the trailer files manager.
   *
   * @return {@link IFsTrailerFilesManager} - the trailer files manager
   */
  IFsTrailerFilesManager trailerFilesManager();

  /**
   * Returns the film files manager.
   *
   * @return {@link IFsFilmFilesManager} - the film files manager
   */
  IFsFilmFilesManager filmFilesManager();

  /**
   * Returns the root directory of the miscellanoues resources.
   * <p>
   * Each subsystem may freely create and use subdirectory in this directory.
   *
   * @return {@link File} - the resources root directory
   */
  File getResourcesRootDir();

}
