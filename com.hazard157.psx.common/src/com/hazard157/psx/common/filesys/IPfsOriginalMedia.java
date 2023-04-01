package com.hazard157.psx.common.filesys;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.incub.optedfile.*;

/**
 * Original multimedia files manager.
 * <p>
 * Original media directory imay be inaccessible hence this interface methods does nothing.
 *
 * @author hazard157
 */
public interface IPfsOriginalMedia {

  /**
   * Returns the valid original media root directory.
   *
   * @return {@link File} - the original media root directory
   */
  File getRootDir();

  /**
   * Determines if original media directory {@link #getRootDir()} exists and is accessible.
   *
   * @return boolean - <code>true</code> if orginal media presents in system
   */
  boolean isOriginalMediaPresent();

  /**
   * List the dates subdirectories in original files directory.
   *
   * @return {@link IList}&lt;{@link LocalDate}&gt; - list of the directories as dates
   */
  IList<LocalDate> listDates();

  /**
   * Lists all files of specified date.
   *
   * @param aDate {@link LocalDate} - the date
   * @return {@link IList}&lt;{@link OptedFile}&gt; - all files in date directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException no such date directory
   */
  IList<OptedFile> listFiles( LocalDate aDate );

  /**
   * Lists all media files in all data subdirectories.
   *
   * @return {@link IList}&lt;{@link OptedFile}&gt; - all files in all data subdirs directory
   */
  IList<OptedFile> listAllFiles();

}
