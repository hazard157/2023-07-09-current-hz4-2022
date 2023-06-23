package com.hazard157.lib.core.incub.kof2;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;

/**
 * Filesystem to browse.
 *
 * @author hazard157
 */
public interface IKofFileSystem {

  /**
   * Returns root directoris of browsing subtrees.
   *
   * @return {@link IList}&lt;{@link File}&gt; - the root dires
   */
  IList<OptedFile> listRoots();

  /**
   * Determines if argument is one of the root directories.
   *
   * @param aFsObj {@link File} - path to be checked
   * @return boolen - <code>true</code> if argument is one of the direcories in {@link #listRoots()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isRoot( File aFsObj );

  /**
   * Finds root directory of file to browse.
   * <p>
   * If argument is not file or directory iunder {@link #findRoot(File)} subtrees, returns <code>null</code>.
   *
   * @param aFsObj {@link File} - any filesystem object
   * @return {@link OptedFile} - one of the {@link #listRoots()} or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  OptedFile findRoot( File aFsObj );

  /**
   * Returns parent directory of file to browse.
   *
   * @param aFsObj {@link File} - any filesystem object
   * @return {@link OptedFile} - parent directory or <code>null</code> for elements of {@link #listRoots()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException argument is not under {@link #listRoots()} subtrees
   */
  OptedFile getParent( File aFsObj );

  /**
   * Returnes child files/dirs of specified direcotry.
   *
   * @param aDir {@link File} - the directory in browsing subtrees
   * @param aKind {@link EFsObjKind} - requested kind of objects to return
   * @return {@link IList}&lt;{@link OptedFile}&gt; - requested content of directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException <code>aDir</code> is not a readable direcory
   * @throws TsItemNotFoundRtException <code>aDir</code> is not under {@link #listRoots()} subtrees
   */
  IList<OptedFile> listChilds( File aDir, EFsObjKind aKind );

  /**
   * Lists siblings of argument.
   * <p>
   * Argument is included in returned list if its kind is applicable to <code>aKind</code>. If argument is on of the
   * root directories, the {@link #listRoots()} will be returned.
   *
   * @param aFsObj {@link File} - the filesystem object in browsing subtrees
   * @param aKind {@link EFsObjKind} - requested kind of objects to return
   * @return {@link IList}&lt;{@link OptedFile}&gt; - requested content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException <code>aFsObj</code> is not under {@link #listRoots()} subtrees
   */
  IList<OptedFile> listSiblings( File aFsObj, EFsObjKind aKind );

  // ------------------------------------------------------------------------------------
  // Convinience inline methods

  @SuppressWarnings( "javadoc" )
  default boolean isOwned( File aFsObj ) {
    return findRoot( aFsObj ) != null;
  }

}
