package com.hazard157.psx.common.filesys.impl;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.filesys.IPfsConfig.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;

/**
 * PSX file system utils.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PfsUtils {

  /**
   * Создает описание rflhf исключительно на основе имени и расширения файла и идентификатора камеры.
   * <p>
   * Если имя этого файла не удовлетворяет формату приложения, возвращает null. Расширение по способу вызова метода,
   * подразумевается, что всегда правильное.
   *
   * @param aFile {@link File} - файл
   * @param aCamId String - идентификатор камеры
   * @param aEpisodeId String - идентификатор эпизода
   * @return {@link IFrame} - описание кадра или <code>null</code>
   */
  public static IFrame makeSourceFrameFromFileName( File aFile, String aCamId, String aEpisodeId ) {
    String bareName = extractBareFileName( aFile.getName() );
    if( !isEpisodeFrameFileName( bareName ) ) {
      return null;
    }
    // проверяем формат имени файла
    int hh, mm, ss, ff, frameNo;
    hh = Integer.parseInt( bareName.substring( 0, 2 ) );
    mm = Integer.parseInt( bareName.substring( 3, 5 ) );
    ss = Integer.parseInt( bareName.substring( 6, 8 ) );
    ff = Integer.parseInt( bareName.substring( 9, 11 ) );
    frameNo = (hh * 3600 + mm * 60 + ss) * FPS + ff;
    String ext = extractExtension( aFile.getName() ).toLowerCase();
    int index = IMAGE_FILE_EXT_LIST.indexOf( ext ); // всегда >= 0
    return new Frame( aEpisodeId, aCamId, frameNo, IS_ANIM_IMAGE_EXT[index] );
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

  public static File pfsFindNonSecFramesEpisodeDir( String aEpisodeId ) {
    LocalDate epDate = EpisodeUtils.episodeId2LocalDate( aEpisodeId );
    return new File( EPISODE_NONSEC_FRAMES_ROOT_DIR, epDate.toString() );
  }

  public static File pfsFindSourceEpisodeDir( String aEpisodeId ) {
    if( EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( aEpisodeId ) ) {
      LocalDate epDate = EpisodeUtils.episodeId2LocalDate( aEpisodeId );
      File epDir = new File( EPISODES_ROOT_DIR, epDate.toString() );
      if( !epDir.exists() ) {
        return null;
      }
      return epDir;
    }
    return null;
  }

  public static File pfsFindSourceEpisodeSubdir( String aEpisodeId, String aSubdir ) {
    File epDir = pfsFindSourceEpisodeDir( aEpisodeId );
    if( epDir != null ) {
      File framesSubdir = new File( epDir, aSubdir );
      if( TsFileUtils.isDirReadable( framesSubdir ) ) {
        return framesSubdir;
      }
    }
    return null;
  }

  /**
   * Формирует только имя (без расширения) файла картинки кадра эпизода.
   * <p>
   * Если значок не задан (aFrameNo < 0), возвращает пустую строку.
   *
   * @param aFrameNo int - номер кадра значка
   * @return String - имя файла без расширения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static String bareSourceFrameFileName( int aFrameNo ) {
    if( aFrameNo < 0 ) {
      return TsLibUtils.EMPTY_STRING;
    }
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

  public static File findFrameDir( IFrame aFrame ) {
    if( aFrame.isAnimated() ) {
      return pfsFindSourceEpisodeSubdir( aFrame.episodeId(), EPSUBDIR_FRAMES_ANIM );
    }
    if( aFrame.isSecAligned() ) {
      return pfsFindSourceEpisodeSubdir( aFrame.episodeId(), EPSUBDIR_FRAMES_SECS );
    }
    return pfsFindNonSecFramesEpisodeDir( aFrame.episodeId() );
  }

  /**
   * Возвращает файл кадра эпизода, если таковой существует.
   *
   * @param aFrame {@link IFrame} - искомый кадр
   * @return {@link File} - файл иллюстрации или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static File findFrameFile( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    if( aFrame.frameNo() < 0 || !aFrame.isDefined() ) {
      return null;
    }
    File subDir = findFrameDir( aFrame );
    if( !subDir.exists() ) {
      return null;
    }
    File epCameraDir = new File( subDir, aFrame.cameraId() );
    if( !epCameraDir.exists() ) {
      return null;
    }
    String baseFileName = bareSourceFrameFileName( aFrame.frameNo() );
    for( int i = 0; i < IMAGE_FILE_EXTENSIONS.length; i++ ) {
      String ext = IMAGE_FILE_EXTENSIONS[i];
      if( (IS_ANIM_IMAGE_EXT[i]) == aFrame.isAnimated() ) {
        File f = new File( epCameraDir, baseFileName + CHAR_EXT_SEPARATOR + ext );
        if( f.exists() ) {
          return f;
        }
      }
    }
    return null;
  }

}
