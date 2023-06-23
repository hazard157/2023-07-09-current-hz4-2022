package com.hazard157.psx.common.filesys.impl;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.psx.common.filesys.*;

/**
 * {@link IPfsOriginalMedia} implementation.
 *
 * @author hazard157
 */
public class PfsOriginalMediaAbsent
    implements IPfsOriginalMedia {

  private final File rootDir;

  /**
   * Constructor.
   *
   * @param aOriginalsRoot {@link File} - original media root directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PfsOriginalMediaAbsent( File aOriginalsRoot ) {
    rootDir = TsNullArgumentRtException.checkNull( aOriginalsRoot );
  }

  // ------------------------------------------------------------------------------------
  // IPsxOriginalMedia
  //

  @Override
  public File getRootDir() {
    return rootDir;
  }

  @Override
  public boolean isOriginalMediaPresent() {
    return false;
  }

  @Override
  public IList<LocalDate> listDates() {
    return IList.EMPTY;
  }

  @Override
  public IList<OptedFile> listFiles( LocalDate aDate ) {
    return IList.EMPTY;
  }

  @Override
  public IList<OptedFile> listAllFiles() {
    return IList.EMPTY;
  }

}
