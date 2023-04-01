package com.hazard157.psx.common.filesys.olds.psx24;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.filesys.olds.psx24.IPsxResources.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;
import static org.toxsoft.core.tslib.utils.files.TsFileUtils.*;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Статические методы (они же правила) именования и работы с файловыми ресурсами приложения PSX.
 *
 * @author hazard157
 */
public class PsxFileSystemUtils {

  private static final String FRAME_FILE_EXT_STILL = "jpg"; //$NON-NLS-1$
  private static final String FRAME_FILE_EXT_ANIM  = "gif"; //$NON-NLS-1$

  private static final String HZ_CREATE_VIDEO_ILL_GIF_SH = "hz-create-video-ill-gif.sh"; //$NON-NLS-1$

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
   * Формирует имя файла (с раширением но без пути) изображения кадра.
   *
   * @param aFrame {@link IFrame} - кадр
   * @return Strng - имя файла
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException кадр не валидный
   */
  public static final String makeFrameFileName( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    TsIllegalArgumentRtException.checkFalse( aFrame.isDefined() );
    String ext = aFrame.isAnimated() ? FRAME_FILE_EXT_ANIM : FRAME_FILE_EXT_STILL;
    String fileName = bareSourceFrameFileName( aFrame.frameNo() );
    return fileName + '.' + ext;
  }

  /**
   * Определяет, соответствует ли имя файла принятому формату "ЧЧ-ММ-СС_КК"".
   *
   * @param aBareName - имя файла без расширения
   * @return boolean - признак соответствия имени аргумента формату "ЧЧ-ММ-СС_КК"
   */
  final static boolean isEpisodeFrameFileName( String aBareName ) {
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
   * Creates illustrative GIF for any video file.
   * <p>
   * Shows progress dialog while creating file.
   *
   * @param aVideoFile {@link File} - the video file
   * @param aGifFile {@link File} - GIF to be created
   * @param aShell {@link Shell} - the shell for progress and information dialogs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error working with files
   */
  // TODO move to hz.aaa lib
  public static void createVideoIllGif( File aVideoFile, File aGifFile, Shell aShell ) {
    TsFileUtils.checkFileReadable( aVideoFile );
    TsFileUtils.checkFileAppendable( aGifFile );
    TsNullArgumentRtException.checkNull( aShell );
    IRunnableWithProgress thumbCreator = aMonitor -> {
      String s = String.format( FMT_CREATING_FILM_THUMB, aVideoFile.getName() );
      aMonitor.beginTask( s, IProgressMonitor.UNKNOWN );
      TsMiscUtils.runTool( HZ_CREATE_VIDEO_ILL_GIF_SH, //
          aVideoFile.getAbsolutePath(), //
          aGifFile.getAbsolutePath() );
    };
    ProgressMonitorDialog d = new ProgressMonitorDialog( aShell );
    try {
      d.run( true, false, thumbCreator );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( aShell, ex );
    }
  }

  /**
   * Запрет на создание экземпляров.
   */
  private PsxFileSystemUtils() {
    // nop
  }

}
