package com.hazard157.psx.common.filesys.olds.psx12;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.e4.services.mps.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * PSX application media player.
 * <p>
 * Uses {@link IMediaPlayerService} for media play.
 *
 * @author hazard157
 */
public interface IFsMediaPlayer {

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

}
