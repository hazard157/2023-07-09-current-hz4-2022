package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.prisex24.cofs.impl.IPsxCofsInternalConstants.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.opfil.*;
import com.hazard157.prisex24.cofs.*;

/**
 * {@link ICofsMingles} implementation.
 *
 * @author hazard157
 */
public class CofsMingles
    implements ICofsMingles {

  /**
   * Constructor.
   */
  public CofsMingles() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // ICofsMingles
  //

  @Override
  public IList<IOptedFile> listMediaFiles( LocalDate aDate, EIncidentMediaKind aMediaKind ) {
    TsNullArgumentRtException.checkNulls( aDate, aMediaKind );
    File mediaDir;
    switch( aMediaKind ) {
      case MASTER: {
        mediaDir = new File( MASTER_MEDIA_ROOT, aDate.toString() );
        break;
      }
      case SOURCE: {
        File mingleDir = new File( COFS_MINGLES_ROOT, aDate.toString() );
        mediaDir = new File( mingleDir, SUBDIR_MINGLE_SOURCE );
        break;
      }
      case OUTPUT: {
        File mingleDir = new File( COFS_MINGLES_ROOT, aDate.toString() );
        mediaDir = new File( mingleDir, SUBDIR_MINGLE_OUTPUT );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aMediaKind.id() );
    }
    IListBasicEdit<IOptedFile> ll = new SortedElemLinkedBundleList<>();
    if( TsFileUtils.isDirReadable( mediaDir ) ) {
      IList<File> ff = TsFileUtils1.collectFilesInSubtree( mediaDir, TsFileFilter.FF_FILES );
      for( File f : ff ) {
        ll.add( new OptedFile( f ) );
      }
    }
    return ll;
  }

}
