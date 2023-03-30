package com.hazard157.lib.core.bricks.kwmark.marker;

import java.io.File;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Strategy - how to mark file object with keywords.
 *
 * @author hazard157
 */
public interface IFileKeywordMarker {

  /**
   * Lists the filesystem objects in specified directory.
   * <p>
   * This method returns objects "viewed" by this marker. Marker may not allow to read some kind of files (eg. non-media
   * files).
   * <p>
   * Even more marker may encode keywords in file name so this method returns names without encodes keywords.
   *
   * @param aDir {@link File} - the directory
   * @return {@link IList}&lt;{@link File}&gt; - files and/or directories "viewed" by this marker
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException can not read the directory content
   */
  IList<File> listObjects( File aDir );

  /**
   * Returns the keywords of filesystem object.
   * <p>
   * Depending on implementation any of the returned keywords may NOT be an IDpath.
   *
   * @param aFile {@link File} - the file or directory as returned in {@link #listObjects(File)}
   * @return {@link IStringList} - the keywords list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException can not access aFile
   */
  IStringList getKeywords( File aFile );

  /**
   * Mark a file or directory with specified keywords.
   *
   * @param aFile {@link File} - the file or directory as returned in {@link #listObjects(File)}
   * @param aKeywords {@link IStringList} - the keywords list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException can not access aFile
   * @throws TsIllegalArgumentRtException any element of aKeywords is not an IDpath
   */
  void setKeywords( File aFile, IStringList aKeywords );

}
