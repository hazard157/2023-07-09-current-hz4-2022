package com.hazard157.lib.core.incub.kof2.impl;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.incub.kof.*;
import com.hazard157.lib.core.incub.optedfile.*;

/**
 * Basic abstract {@link IKofFileSystem} implementation.
 *
 * @author hazard157
 */
public abstract class KofAbstractFileSystem
    implements IKofFileSystem {

  private final IMapEdit<File, OptedFile> cache = new ElemMap<>();

  /**
   * Root directories initialized in {@link #setRootDirs(IList)}.
   * <p>
   * List contains readable directories and non-crossing subtrees. All elementas are absolute paths as of
   * {@link File#getAbsoluteFile()}.
   */
  private IListEdit<File> rootDirectories = new ElemArrayList<>();

  /**
   * {@link OptedFile} corresponding to {@link #rootDirectories}.
   */
  private IListEdit<OptedFile> rootOptedDirs = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public KofAbstractFileSystem() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Makes and returns list of directories with nn-crossing subtrees from argument.
   * <p>
   * Non-readable directories and directories that are in subtree of other element of argument are simply ignored.
   * <p>
   * Order of elements in returned list corresponds to the order of elements in argument list.
   *
   * @param aRootDirs {@link IList}&lt;{@link File}&gt; - list of paths considered as root directories
   * @return {@link IList}&lt;{@link File}&gt; - correct list of dirs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  private static IList<File> internalMakeRoots( IList<File> aRootDirs ) {
    TsNullArgumentRtException.checkNull( aRootDirs );
    // ignore non-dirs or unaccessible elements
    IListEdit<File> ll1 = new ElemArrayList<>();
    for( File f : aRootDirs ) {
      if( TsFileUtils.isDirReadable( f ) ) {
        ll1.add( f );
      }
    }
    // ignore nested subdirs
    IListEdit<File> ll2 = new ElemArrayList<>();
    for( File f : ll1 ) {
      boolean isSubdir = false;
      for( File p : ll1 ) {
        if( f != p && TsFileUtils.isChild( p, f ) ) {
          isSubdir = true;
          break;
        }
      }
      if( !isSubdir ) {
        ll2.add( f.getAbsoluteFile() );
      }
    }
    return ll2;
  }

  private void internalClearEverything() {
    rootDirectories.clear();
    rootOptedDirs.clear();
    cache.clear();
  }

  // ------------------------------------------------------------------------------------
  // IKofFileSystem
  //

  @Override
  public IList<OptedFile> listRoots() {
    return rootOptedDirs;
  }

  @Override
  public boolean isRoot( File aFsObj ) {
    TsNullArgumentRtException.checkNull( aFsObj );
    File absFile = aFsObj.getAbsoluteFile();
    return rootDirectories.hasElem( absFile );
  }

  @Override
  public OptedFile findRoot( File aFsObj ) {
    TsNullArgumentRtException.checkNull( aFsObj );
    // argument must exists
    if( !aFsObj.exists() ) {
      return null;
    }
    // argument must be either file or directory, no special fs object
    if( !EFsObjKind.BOTH.isAccepted( aFsObj ) ) {
      return null;
    }
    // check that argument is in one of the root directories subtrees
    for( OptedFile of : rootOptedDirs ) {
      if( TsFileUtils.isChild( of.file(), aFsObj ) ) {
        if( doIsAccepted( aFsObj ) ) {
          return of;
        }
        return null;
      }
    }
    return null;
  }

  @Override
  public OptedFile getParent( File aFsObj ) {
    TsItemNotFoundRtException.checkNull( findRoot( aFsObj ) );
    File absFile = aFsObj.getAbsoluteFile();
    if( rootDirectories.hasElem( absFile ) ) {
      return null;
    }
    return createOpted( absFile.getParentFile() );
  }

  @Override
  public IList<OptedFile> listChilds( File aDir, EFsObjKind aKind ) {
    TsFileUtils.checkDirReadable( aDir );
    TsItemNotFoundRtException.checkNull( findRoot( aDir ) );
    IList<OptedFile> ff = doListChilds( aDir, aKind );
    TsInternalErrorRtException.checkNull( ff );
    // check returned elemnts were created by the method createOpted(File)
    for( OptedFile of : ff ) {
      TsInternalErrorRtException.checkFalse( cache.hasElem( of ) );
    }
    return ff;
  }

  @Override
  public IList<OptedFile> listSiblings( File aFsObj, EFsObjKind aKind ) {
    TsItemNotFoundRtException.checkNull( findRoot( aFsObj ) );
    // for root directory return roots list
    File absFile = aFsObj.getAbsoluteFile();
    if( rootDirectories.hasElem( absFile ) ) {
      if( aKind.isDir() ) {
        return rootOptedDirs;
      }
      return IList.EMPTY;
    }
    // for non-root just return childs of parent
    File absParent = absFile.getParentFile().getAbsoluteFile();
    return listChilds( absParent, aKind );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Sets root directories to browse.
   * <p>
   * Order of directories in argument will be maintained in list {@link #listRoots()}.
   * <p>
   * Non-directory or unaccessible elements of list will be ignored.Also will be ignored elements which are subdirectory
   * of any other element.
   *
   * @param aRootDirs {@link IList}&lt;{@link File}&gt; - the root dires
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void setRootDirs( IList<File> aRootDirs ) {
    internalClearEverything();
    rootDirectories.addAll( internalMakeRoots( aRootDirs ) );
    // fill OptedFile roots list
    for( File f : rootDirectories ) {
      OptedFile of = new OptedFile( f );
      cache.put( f, of );
      rootOptedDirs.add( of );
    }
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the {@link OptedFile} from cache either existing or created and cached one.
   *
   * @param aFile {@link File} - the file from this file system
   * @return {@link OptedFile} - cached instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException the file is not from this file system
   */
  protected OptedFile createOpted( File aFile ) {
    TsItemNotFoundRtException.checkNull( findRoot( aFile ) );
    File absFile = aFile.getAbsoluteFile();
    OptedFile of = cache.findByKey( absFile );
    if( of == null ) {
      // maybe restrict cache size???
      of = new OptedFile( absFile );
      cache.put( absFile, of );
    }
    return of;
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass must list childs of specified directory.
   * <p>
   * Note: all elements of returned list must be created by method {@link #createOpted(File)} otherwise the exception
   * will be thrown.
   *
   * @param aDir {@link File} - the directory guaranteed of this file system
   * @param aKind {@link EFsObjKind} - requested kind of objects to return
   * @return {@link IListEdit}&lt;{@link OptedFile}&gt; - editable list of requested content of directory
   */
  protected abstract IList<OptedFile> doListChilds( File aDir, EFsObjKind aKind );

  /**
   * Subclass must check if argument is accepted as this filesystem object.
   * <p>
   * Argument is an existing path in the real filesystem in on other root dirs {@link #listRoots()} subtree.
   * <p>
   * For example, argument file may be checked for correct extension and argument directory accepted if not hidden.
   *
   * @param aFsObj {@link File} - file/dir to be checked
   * @return boolean - file/dir in real filesystem is accepted as this filesystem object
   */
  protected abstract boolean doIsAccepted( File aFsObj );

}
