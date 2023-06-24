package com.hazard157.lib.core.excl_done.mps;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Служба общего назначения для воспроизведения медия файлов.
 *
 * @author hazard157
 */
public interface IMediaPlayerService {

  /**
   * Воспроизводит видео-файл во внешнем проигривателе.
   * <p>
   * В случае ошибки работы с файлом или запуском воспроизведения, не выбрасывает исключение, а показывает диалог
   * ошибки.
   *
   * @param aVideoFile {@link File} - видео-файл
   * @param aFullscreen boolean - признак запуска на весь жкран
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  void playVideoFile( File aVideoFile, boolean aFullscreen );

  /**
   * Воспроизводит указанную часть видео-файла во внешнем проигривателе.
   *
   * @param aVideoFile {@link File} - видео-файл
   * @param aStartSec int - начало воспрооизводимой части (в секундах с начала видео)
   * @param aDurationSecs int - длительность воспрооизводимой части (в секундах)
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aStartSec < 0 или aDurationSecs < 0
   * @throws TsIoRtException заданный файл не существует или его невозможно прочитать
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  void playVideoFilePart( File aVideoFile, int aStartSec, int aDurationSecs );

  /**
   * Воспроизводит аудио-файл во внешнем проигривателе.
   * <p>
   * В случае ошибки работы с файлом или запуском воспроизведения, не выбрасывает исключение, а показывает диалог
   *
   * @param aAudioFile {@link File} - аудио-файл
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  void playAudioFile( File aAudioFile );

  /**
   * Воспроизводит указанную часть аудио-файла во внешнем проигривателе.
   *
   * @param aAudioFile {@link File} - аудио-файл
   * @param aStartSec int - начало воспрооизводимой части (в секундах с начала видео)
   * @param aDurationSecs int - длительность воспрооизводимой части (в секундах)
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aStartSec < 0 или aDurationSecs < 0
   * @throws TsIoRtException заданный файл не существует или его невозможно прочитать
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  void playAudioFilePart( File aAudioFile, int aStartSec, int aDurationSecs );

  /**
   * Opens the file int the user's preffered application.
   * <p>
   * If argument is <code>null</code> or does not exists, method displayes error dialog and returns.
   *
   * @param aFile {@link File} - the file to open
   */
  void runPrefApp( File aFile );

  // ------------------------------------------------------------------------------------
  // Методы для удобства
  //

  @SuppressWarnings( "javadoc" )
  default void playVideoFile( File aVideoFile ) {
    playVideoFile( aVideoFile, false );
  }

}
