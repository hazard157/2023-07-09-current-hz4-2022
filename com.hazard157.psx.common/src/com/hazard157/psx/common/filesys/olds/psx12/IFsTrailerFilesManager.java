package com.hazard157.psx.common.filesys.olds.psx12;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Trailer files management.
 *
 * @author hazard157
 */
public interface IFsTrailerFilesManager {

  /**
   * Returns trailer file if it exists.
   *
   * @param aTrailerFullId String - trailer full ID (consists of episode and trailer local IDs)
   * @return {@link File} - existing trailer file or {@link NullPointerException}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File findTrailerFile( String aTrailerFullId );

}
