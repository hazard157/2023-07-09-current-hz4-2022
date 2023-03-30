package com.hazard157.lib.core.incub.optedfile;

import java.io.File;
import java.io.FileFilter;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.SortedElemLinkedBundleListEx;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.files.TsFileFilter;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;

import com.hazard157.lib.core.incub.ConvoyFileManager;

/**
 * File or directory which have {@link IOptionSet} parameters convoy information.
 * <p>
 * Each editing of parameters {@link #params()} is immediately wriiten to the convoy file. If {@link #params()} can not
 * be written (eg. file is on read-only drive) the warning is logged but no exception is thrown.
 *
 * @author goga
 * @see ConvoyFileManager
 */
public class OptedFile
    implements IParameterizedEdit, Comparable<OptedFile> {

  private static final String            CONVOY_FILE_EXT = "convoy";                                //$NON-NLS-1$
  private static final ConvoyFileManager cfm             = new ConvoyFileManager( CONVOY_FILE_EXT );

  private final ITsCollectionChangeListener paramsChangeListener = ( aSource, aOp, aItem ) -> writeParams();

  private final File    file;
  private final boolean isDir;

  private final INotifierOptionSetEdit params = new NotifierOptionSetEditWrapper( new OptionSet() );

  /**
   * Creates the instance for the existing file.
   *
   * @param aFile {@link File} - path to the denoted file or directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException unaccessible file or directory
   */
  public OptedFile( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    if( aFile.isFile() ) {
      TsFileUtils.checkFileReadable( aFile );
      isDir = false;
    }
    else {
      if( aFile.isDirectory() ) {
        TsFileUtils.checkDirReadable( aFile );
        isDir = true;
      }
      else {
        throw new TsIoRtException( aFile.getAbsolutePath() );
      }
    }
    file = aFile;
    refresh();
    params.addCollectionChangeListener( paramsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void writeParams() {
    if( isDir ) {
      cfm.writeDirConvoy( file, params, OptionSetKeeper.KEEPER_INDENTED );
    }
    else {
      cfm.writeFileConvoy( file, params, OptionSetKeeper.KEEPER_INDENTED );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Therurns the file.
   *
   * @return {@link File} - the file
   */
  public File file() {
    return file;
  }

  /**
   * Determines if {@link #file()} points to directory.
   *
   * @return boolean - <code>true</code> if {@link #file()} is directory, <code>false</code> if it is common file
   */
  public boolean isDirectory() {
    return isDir;
  }

  /**
   * Re-reads {@link #params()} from the convoy file.
   */
  public void refresh() {
    IOptionSet p;
    if( isDir ) {
      p = cfm.readDirConvoy( file, OptionSetKeeper.KEEPER, IOptionSet.NULL );
    }
    else {
      p = cfm.readFileConvoy( file, OptionSetKeeper.KEEPER, IOptionSet.NULL );
    }
    params.pauseFiring();
    params.setAll( p );
    params.resumeFiring( false );
  }

  // ------------------------------------------------------------------------------------
  // Static API
  //

  /**
   * Returns the filtered content of the directory as list of {@link OptedFile}.
   * <p>
   * The result is sorted in ascending alphabetical order with directories first.
   *
   * @param aDir {@link File} - the directory
   * @param aFileFilter {@link FileFilter} - file selection filter
   * @return {@link IList}&lt;{@link OptedFile}&gt; - sorted list of {@link OptedFile}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IList<OptedFile> list( File aDir, FileFilter aFileFilter ) {
    IList<File> all = TsFileUtils.listChilds( aDir, aFileFilter );
    if( all.isEmpty() ) {
      return IList.EMPTY;
    }
    IListBasicEdit<File> ff = new SortedElemLinkedBundleListEx<>( TsFileUtils.FILEDIR_CMP_ASC );
    ff.addAll( all );
    IListEdit<OptedFile> result = new ElemArrayList<>( ff.size() );
    for( File f : ff ) {
      OptedFile of = new OptedFile( f );
      result.add( of );
    }
    return result;
  }

  /**
   * Returns the content of the directory as list of {@link OptedFile}.
   * <p>
   * This method return exactly the result of {@link #list(File, FileFilter) list(aDir,TsFileFilter.FF_ALL} call.
   * <p>
   * The result is sorted in ascending alphabetical order with directories first.
   *
   * @param aDir {@link File} - the directory
   * @return {@link IList}&lt;{@link OptedFile}&gt; - sorted list of {@link OptedFile}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IList<OptedFile> list( File aDir ) {
    return list( aDir, TsFileFilter.FF_ALL );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return file.getPath() + " params[" + params.size() + ']'; //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof OptedFile that ) {
      return this.file.equals( that.file ) && that.params.equals( that.params );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + file.hashCode();
    result = TsLibUtils.PRIME * result + params.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( OptedFile aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return this.file.compareTo( aThat.file );
  }

}
