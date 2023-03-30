package com.hazard157.psx24.core.e4.services.prisex;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Common service for PRISEX application.
 * <p>
 * This includes new and experimental features that will either stay here or create new services, including for reuse in
 * other applications.
 *
 * @author hazard157
 */
public interface IPrisexService {

  /**
   * Воспроизводит видео материал эпизода по указанным параметрам.
   * <p>
   * Параметры {@link Svin} точно задает эпизод {@link Svin#episodeId()}. Камера {@link Svin#cameraId()} определяет,
   * какое исходное видео будет вопроизведено. Если камера не задана, то используется камера иллюстрирующего эпизод
   * кадра, а если и она не задана - первыое исходное видео. Интервал воспрозведения {@link Svin#interval()} или весь
   * эпизод, если интервал не задан.
   * <p>
   * Если невозможно вопроизвести запрошенное видео, метод выдает диалог и возвращается без исключений.
   *
   * @param aSvin {@link Svin} - параемтры воспроизведения
   * @throws TsNullArgumentRtException аргумент = null
   */
  void playEpisodeVideo( Svin aSvin );

  /**
   * Воспроизводит трейлер.
   *
   * @param aTrailerId String - идентификатор трейлера
   * @throws TsNullArgumentRtException аргумент = null
   */
  void playTrailer( String aTrailerId );

  // /**
  // * Воспроизводит фильм.
  // *
  // * @param aFilmId String - идентификатор фильмa
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // void playFilm( String aFilmId );

  /**
   * Воспроизводит аудио-файл.
   *
   * @param aFilePath String - путь к файлу
   * @throws TsNullArgumentRtException аргумент = null
   */
  void playAudioFile( String aFilePath );

  /**
   * Корпирует файл изображения кадра (если таковой существует).
   * <p>
   * Файл назначения получает имя ЭПИЗОД-КАДР-КАМЕРА (типа "e1995_05_06-00-00-03_00-cvhs.no01").
   * <p>
   * Существующий файл назначения переписываеться.
   *
   * @param aFrame {@link IFrame} - копируемый кадр
   * @param aDestDir {@link File} - директория назначения
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIoRtException ошибка файловой операции
   */
  void copyFrameImage( IFrame aFrame, File aDestDir );

  /**
   * Загружает миниатюру анимированного изображения иллюстрации к трейлеру.
   * <p>
   * При отсутствии илююстрации или если задан aRectreate, создает gif-файл иллюстрации.
   *
   * @param aTrailerId Sttring - идентификатор трейлера
   * @param aThumbSize {@link EThumbSize} - запршенный размер миниатюры
   * @param aRecreate boolean - признак пересоздания иллюстрации
   * @return {@link TsImage} - миниатюра или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет такого трейлера
   */
  TsImage loadTrailerIllimThumb( String aTrailerId, EThumbSize aThumbSize, boolean aRecreate );

}
