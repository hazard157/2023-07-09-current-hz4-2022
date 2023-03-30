package com.hazard157.lib.core.incub.kof.impl;

import java.io.File;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.incub.kof.IKofFileSystem;
import com.hazard157.lib.core.incub.optedfile.OptedFile;

/**
 * An {@link IKofFileSystem} implementation including all non-hidden directories and files by extensions list.
 *
 * @author hazard157
 * @param <T> - type of T subclass
 */
public class KofFilesByExtDirsAll<T extends OptedFile>
    extends KofAbstractFileSystem<T> {

  private final TsFileFilter fileFilter;

  /**
   * Constructor.
   *
   * @param aExtensions String - included extensions (without dot) or an empty list to include all extensions
   * @param aCreator {@link IKofItemCreator}&lt;T&gt; - instances creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KofFilesByExtDirsAll( IStringList aExtensions, IKofItemCreator<T> aCreator ) {
    super( aCreator );
    TsNullArgumentRtException.checkNull( aExtensions );
    fileFilter = new TsFileFilter( EFsObjKind.BOTH, aExtensions );
  }

  // ------------------------------------------------------------------------------------
  // KofAbstractFileSystem
  //

  @Override
  protected IList<T> doListChilds( File aDir, EFsObjKind aKind ) {
    IList<File> all = TsFileUtils.listChilds( aDir, aKind );
    IListEdit<T> ff = new ElemArrayList<>( all.size() );
    for( File f : all ) {
      if( fileFilter.accept( f ) ) {
        T of = createOpted( f );
        ff.add( of );
      }
    }
    return ff;
  }

  @Override
  protected boolean doIsAccepted( File aFsObj ) {
    return fileFilter.accept( aFsObj );
  }

}
