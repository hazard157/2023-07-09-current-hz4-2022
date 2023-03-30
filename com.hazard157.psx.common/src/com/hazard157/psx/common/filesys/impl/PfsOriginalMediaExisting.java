package com.hazard157.psx.common.filesys.impl;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.filesys.IPfsHardConstants.*;
import static com.hazard157.psx.common.filesys.impl.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.incub.optedfile.*;
import com.hazard157.psx.common.filesys.*;

/**
 * {@link IPfsOriginalMedia} implementation.
 *
 * @author goga
 */
public class PfsOriginalMediaExisting
    implements IPfsOriginalMedia {

  private static final String FILE_NAME_TEMPLATE = "2020-12-31";               //$NON-NLS-1$
  private static final int    FILE_NAME_LEN      = FILE_NAME_TEMPLATE.length();

  private final File rootDir;

  /**
   * Constructor.
   *
   * @param aOriginalsRoot {@link File} - original media root directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException unaccessible directory
   */
  public PfsOriginalMediaExisting( File aOriginalsRoot ) {
    rootDir = TsFileUtils.checkDirReadable( aOriginalsRoot );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private File makeDateDir( LocalDate aDate ) {
    TsNullArgumentRtException.checkNull( aDate );
    String fn = aDate.toString();
    return new File( rootDir, fn );
  }

  private static OptedFile crateOriginalMediaOptedFile( File aFile, LocalDate aDate ) {
    OptedFile f = new OptedFile( aFile );
    LocalDate d = OPDEF_ORIGINAL_MEDIA_DATE.getValue( f.params() ).asValobj();
    if( !d.equals( aDate ) ) {
      OPDEF_ORIGINAL_MEDIA_DATE.setValue( f.params(), avValobj( aDate ) );
    }
    return f;
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
    return true;
  }

  @Override
  public IList<LocalDate> listDates() {
    File[] ff = rootDir.listFiles( TsFileFilter.FF_DIRS );
    if( ff == null || ff.length == 0 ) {
      return IList.EMPTY;
    }
    IListEdit<LocalDate> ll = new ElemArrayList<>( ff.length );
    for( File f : ff ) {
      String fn = f.getName();
      if( fn.length() == FILE_NAME_LEN ) {
        try {
          LocalDate d = LocalDate.parse( fn );
          if( d.isAfter( MIN_PSX_DATE ) && d.isBefore( MAX_PSX_DATE ) ) {
            ll.add( d );
          }
          else {
            LoggerUtils.errorLogger().warning( FMT_WARN_INV_ORIG_DATA_DIR, f.getAbsolutePath() );
          }
        }
        catch( @SuppressWarnings( "unused" ) Exception ex ) {
          // warn about non-valid subdirectory
          LoggerUtils.errorLogger().warning( FMT_WARN_INV_ORIG_DATA_DIR, f.getAbsolutePath() );
        }
      }
    }
    return ll;
  }

  @Override
  public IList<OptedFile> listFiles( LocalDate aDate ) {
    File dateDir = makeDateDir( aDate );
    if( !TsFileUtils.isDirReadable( dateDir ) ) {
      throw new TsItemNotFoundRtException( FMT_ERR_INV_ORIDG_DATE, aDate );
    }
    File[] ff = dateDir.listFiles( TsFileFilter.FF_FILES );
    if( ff == null || ff.length == 0 ) {
      return IList.EMPTY;
    }
    IListEdit<OptedFile> ll = new ElemArrayList<>( ff.length );
    for( File f : ff ) {
      ll.add( crateOriginalMediaOptedFile( f, aDate ) );
    }
    return ll;
  }

  @Override
  public IList<OptedFile> listAllFiles() {
    IList<LocalDate> dlist = listDates();
    IListEdit<OptedFile> ll = new ElemLinkedBundleList<>();
    for( LocalDate d : dlist ) {
      ll.addAll( listFiles( d ) );
    }
    return ll;
  }

}
