package com.hazard157.lib.core.excl_done.kof2.impl;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.lib.core.excl_done.kof.*;

/**
 * An {@link IKofFileSystem} implementation including all non-hidden directories and files by extensions list.
 *
 * @author hazard157
 */
public class KofFilesByExtDirsAll
    extends KofAbstractFileSystem {

  private final TsFileFilter fileFilter;

  /**
   * Constructor.
   *
   * @param aExtensions String - included extensions (without dot) or an empty list to include all extensions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KofFilesByExtDirsAll( IStringList aExtensions ) {
    TsNullArgumentRtException.checkNull( aExtensions );
    fileFilter = new TsFileFilter( EFsObjKind.BOTH, aExtensions );
  }

  // ------------------------------------------------------------------------------------
  // KofAbstractFileSystem
  //

  @Override
  protected IList<OptedFile> doListChilds( File aDir, EFsObjKind aKind ) {
    IList<File> all = TsFileUtils.listChilds( aDir, aKind );
    IListEdit<OptedFile> ff = new ElemArrayList<>( all.size() );
    for( File f : all ) {
      if( fileFilter.accept( f ) ) {
        OptedFile of = createOpted( f );
        ff.add( of );
      }
    }
    return ff;
  }

  @Override
  protected boolean doIsAccepted( File aFsObj ) {
    return fileFilter.accept( aFsObj );
  }

  @Override
  public IList listRootDirs() {
    // TODO Auto-generated method stub
    return null;
  }

}
