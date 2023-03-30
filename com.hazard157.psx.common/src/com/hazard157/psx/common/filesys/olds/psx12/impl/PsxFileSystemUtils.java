package com.hazard157.psx.common.filesys.olds.psx12.impl;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Статические методы (они же правила) именования и работы с файловыми ресурсами приложения PSX.
 *
 * @author goga
 */
public class PsxFileSystemUtils {

  static final String STILL_FRAME_FILE_EXT = "jpg"; //$NON-NLS-1$
  static final String ANIM_FRAME_FILE_EXT  = "gif"; //$NON-NLS-1$

  static final String EPSUBDIR_FRAMES_ANIM = "frames-anim";  //$NON-NLS-1$
  static final String EPSUBDIR_FRAMES_SECS = "frames-still"; //$NON-NLS-1$
  static final String EPSUBDIR_SRCVIDEOS   = "srcvideos";    //$NON-NLS-1$
  static final String EPSUBDIR_TRAILERS    = "trailers";     //$NON-NLS-1$

  static final File EPISODES_RESOURCES_ROOT = new File( "/home/hmade/episodes/" );             //$NON-NLS-1$
  static final File NONSEC_FRAMES_ROOT      = new File( "/home/.psx/episodes/frames-nonsec" ); //$NON-NLS-1$

  /**
   * Создает описание кадра исключительно на основе имени и расширения файла и идентификатора камеры.
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
    // String bareName = extractBareFileName( aFile.getName() );
    String fileName = aFile.getName();
    if( !isEpisodeFrameFileName2( fileName ) ) {
      return null;
    }
    // проверяем формат имени файла
    int hh, mm, ss, ff, frameNo;
    hh = Integer.parseInt( fileName.substring( 0, 2 ) );
    mm = Integer.parseInt( fileName.substring( 3, 5 ) );
    ss = Integer.parseInt( fileName.substring( 6, 8 ) );
    ff = Integer.parseInt( fileName.substring( 9, 11 ) );
    frameNo = (hh * 3600 + mm * 60 + ss) * FPS + ff;
    String ext = extractExtension( aFile.getName() ).toLowerCase();
    int index = IMAGE_FILE_EXT_LIST.indexOf( ext ); // всегда >= 0
    return new Frame( aEpisodeId, aCamId, frameNo, IS_ANIM_IMAGE_EXT[index] );
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

  /**
   * Определяет, соответствует ли имя файла принятому формату "ЧЧ-ММ-СС_КК"".
   *
   * @param aFileName - имя файла без расширения
   * @return boolean - признак соответствия имени аргумента формату "ЧЧ-ММ-СС_КК"
   */
  // final static boolean isEpisodeFrameFileName( String aBareName ) {
  // if( aBareName.length() == 11 ) { // "ЧЧ-ММ-СС_КК"
  // if( aBareName.charAt( 2 ) != '-' || aBareName.charAt( 5 ) != '-'
  // || (aBareName.charAt( 8 ) != '-' && aBareName.charAt( 8 ) != '_') ) {
  // return false;
  // }
  // if( !isAsciiDigit( aBareName.charAt( 0 ) ) || !isAsciiDigit( aBareName.charAt( 1 ) )
  // || !isAsciiDigit( aBareName.charAt( 3 ) ) || !isAsciiDigit( aBareName.charAt( 4 ) )
  // || !isAsciiDigit( aBareName.charAt( 6 ) ) || !isAsciiDigit( aBareName.charAt( 7 ) )
  // || !isAsciiDigit( aBareName.charAt( 9 ) ) || !isAsciiDigit( aBareName.charAt( 10 ) ) ) {
  // return false;
  // }
  // return true;
  // }
  // return false;
  // }

  // DEBUG анализ без необходимости удалить расширение
  final static boolean isEpisodeFrameFileName2( String aFileName ) {
    if( aFileName.length() >= 11 ) { // "ЧЧ-ММ-СС_КК"
      if( aFileName.charAt( 2 ) != '-' || aFileName.charAt( 5 ) != '-'
          || (aFileName.charAt( 8 ) != '-' && aFileName.charAt( 8 ) != '_') ) {
        return false;
      }
      if( !isAsciiDigit( aFileName.charAt( 0 ) ) || !isAsciiDigit( aFileName.charAt( 1 ) )
          || !isAsciiDigit( aFileName.charAt( 3 ) ) || !isAsciiDigit( aFileName.charAt( 4 ) )
          || !isAsciiDigit( aFileName.charAt( 6 ) ) || !isAsciiDigit( aFileName.charAt( 7 ) )
          || !isAsciiDigit( aFileName.charAt( 9 ) ) || !isAsciiDigit( aFileName.charAt( 10 ) ) ) {
        return false;
      }
      if( aFileName.length() > 11 && aFileName.charAt( 11 ) != TsFileUtils.CHAR_EXT_SEPARATOR ) {
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * Запрет на создание экземпляров.
   */
  private PsxFileSystemUtils() {
    // nop
  }

}
