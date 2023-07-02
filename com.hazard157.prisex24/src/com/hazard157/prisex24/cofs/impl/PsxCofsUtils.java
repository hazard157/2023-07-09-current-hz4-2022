package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.prisex24.cofs.impl.IPsxCofsInternalConstants.*;
import static com.hazard157.prisex24.cofs.l10n.IPsxCofsSharedResources.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * COFS utility methods.
 *
 * @author hazard157
 */
class PsxCofsUtils {

  public static final TsFileFilter FF_IDPATH_DIRS =
      new TsFileFilter( EFsObjKind.DIR, IStringList.EMPTY, false, false ) {

        @Override
        protected boolean doAccept( File aPathName, boolean aIsFile ) {
          return StridUtils.isValidIdPath( aPathName.getName() );
        }

      };

  /**
   * Ensures that fresh summary GIF file exists.
   * <p>
   * Out-dated or non-existing GIF will be recreated using {@link #createSummaryGif(OptedFile, File)}.
   *
   * @param aVideoFile {@link OptedFile} - the video file
   * @param aGifFile {@link File} - GIF file to be created
   * @return {@link File} - the argument <code>aGifFile</code> or <code>null</code> if can't be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static File ensureSummaryGif( OptedFile aVideoFile, File aGifFile ) {
    TsNullArgumentRtException.checkNulls( aVideoFile, aGifFile );
    // out-dated or non-existing GIF will be recreated
    if( !aGifFile.exists() || aGifFile.lastModified() < aVideoFile.file().lastModified() ) {
      ValidationResult vr = PsxCofsUtils.createSummaryGif( aVideoFile, aGifFile );
      if( vr.isError() ) {
        return null;
      }
    }
    if( !aGifFile.exists() ) {
      return null;
    }
    return aGifFile;
  }

  /**
   * Creates the summary GIF animation for the specified video file.
   * <p>
   * TODO if OptedFile.params() has ClipThumb definition - use it
   *
   * @param aVideoFile {@link OptedFile} - the video file
   * @param aGifFile {@link File} - GIF file to be created
   * @return {@link ValidationResult} - how the operation was performed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult createSummaryGif( OptedFile aVideoFile, File aGifFile ) {
    TsNullArgumentRtException.checkNulls( aVideoFile, aGifFile );

    /**
     * TODO aVideoFile.params() may contain additional information about GIF file creation, such as #ClipThumb, here
     * must be the implementation of such GIF creation
     */

    if( !GIF_CREATE_SH.exists() ) {
      return ValidationResult.warn( FMT_WARN_NO_SH_FILE, GIF_CREATE_SH.getAbsolutePath() );
    }
    IRunnableWithProgress thumbCreator = aMonitor -> {
      String s = String.format( FMT_CREATING_FILM_THUMB, aVideoFile.file().getName() );
      aMonitor.beginTask( s, IProgressMonitor.UNKNOWN );
      TsMiscUtils.runAndWait( 600, GIF_CREATE_SH.getAbsolutePath(), aVideoFile.file().getAbsolutePath(),
          aGifFile.getAbsolutePath() );
    };
    ProgressMonitorDialog d = new ProgressMonitorDialog( null );
    try {
      d.run( true, false, thumbCreator );
      return ValidationResult.SUCCESS;
    }
    catch( Exception ex ) {
      return ValidationResult.error( ex );
    }
  }

  /**
   * Makes bare file name (without extension) of the frame file.
   *
   * @param aFrameNo int - the frame number
   * @return String - frame file bare name
   * @throws TsIllegalArgumentRtException aFrameNo < 1
   */
  public static String bareSourceFrameFileName( int aFrameNo ) {
    TsIllegalArgumentRtException.checkTrue( aFrameNo < 0 );
    Integer frameNo = Integer.valueOf( aFrameNo % FPS );
    int totalSecs = aFrameNo / FPS;
    Integer secs = Integer.valueOf( totalSecs % 60 );
    totalSecs /= 60;
    Integer mins = Integer.valueOf( totalSecs % 60 );
    totalSecs /= 60;
    Integer hours = Integer.valueOf( totalSecs % 60 );
    // fName = "HH-MM-SS_FF"
    return String.format( "%02d-%02d-%02d_%02d", hours, mins, secs, frameNo ); //$NON-NLS-1$
  }

  /**
   * Determines if bare file name of frame conforms to the format HH-MM-SS_FF.
   *
   * @param aBareName - file name without extension
   * @return boolean - <code>true</code> if argument has the format HH-MM-SS_FF
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isEpisodeFrameFileName( String aBareName ) {
    TsNullArgumentRtException.checkNull( aBareName );
    if( aBareName.length() == 11 ) { // "ЧЧ-ММ-СС_КК"
      if( aBareName.charAt( 2 ) != '-' || aBareName.charAt( 5 ) != '-'
          || (aBareName.charAt( 8 ) != '-' && aBareName.charAt( 8 ) != '_') ) {
        return false;
      }
      if( !isAsciiDigit( aBareName.charAt( 0 ) ) || !isAsciiDigit( aBareName.charAt( 1 ) )
          || !isAsciiDigit( aBareName.charAt( 3 ) ) || !isAsciiDigit( aBareName.charAt( 4 ) )
          || !isAsciiDigit( aBareName.charAt( 6 ) ) || !isAsciiDigit( aBareName.charAt( 7 ) )
          || !isAsciiDigit( aBareName.charAt( 9 ) ) || !isAsciiDigit( aBareName.charAt( 10 ) ) ) {
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * Creates {@link IFrame} based only on file name, extension, episode and camera IDs.
   * <p>
   * Note: method assumes that file name, extension, camera ID and episode ID have valid values.
   *
   * @param aFile {@link File} - the image file with extension
   * @param aCamId String - the camera ID
   * @param aEpisodeId String - the episode ID
   * @return {@link IFrame} - the frame or <code>null</code>
   */
  public static IFrame makeSourceFrameFromFileName( File aFile, String aCamId, String aEpisodeId ) {
    String bareName = extractBareFileName( aFile.getName() );
    if( !isEpisodeFrameFileName( bareName ) ) {
      return null;
    }
    int hh, mm, ss, ff, frameNo;
    hh = Integer.parseInt( bareName.substring( 0, 2 ) );
    mm = Integer.parseInt( bareName.substring( 3, 5 ) );
    ss = Integer.parseInt( bareName.substring( 6, 8 ) );
    ff = Integer.parseInt( bareName.substring( 9, 11 ) );
    frameNo = (hh * 3600 + mm * 60 + ss) * FPS + ff;
    String ext = extractExtension( aFile.getName() ).toLowerCase();
    int index = IMAGE_FILE_EXT_LIST.indexOf( ext );
    return new Frame( aEpisodeId, aCamId, frameNo, IS_ANIM_IMAGE_EXT[index] );
  }

  /**
   * Searches for probably camera sub-directories in the specified directory/
   * <p>
   * Sub-directory is considered to be related to the camera if directory name is an IDpath which is considered as a
   * camera ID.
   *
   * @param aDir {@link File} - the directory to search in
   * @return {@link IStringMap}&lt;{@link File}&gt; - the sorted map "camera ID" - "path to camera sub-directory"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final IStringMap<File> listProbablyCamsSubdirs( File aDir ) {
    if( TsFileUtils.isDirReadable( aDir ) ) {
      IList<File> llSubs = TsFileUtils.listChilds( aDir, FF_IDPATH_DIRS );
      if( !llSubs.isEmpty() ) {
        IStringMapEdit<File> map = new SortedStringMap<>();
        for( File f : llSubs ) {
          map.put( f.getName(), f );
        }
        return map;
      }
    }
    return IStringMap.EMPTY;
  }

  /**
   * /** No subclassing.
   */
  private PsxCofsUtils() {
    // nop
  }

}
