package com.hazard157.psx.proj3.sourcevids;

import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Базовый интерфейс исходного видео эпизода.
 * <p>
 * Реализует общие для любой реализации методы, что для psx3, что для psx5 или еще будущих реализации.
 *
 * @author hazard157
 */
public interface ISourceVideo
    extends ISinentity<SourceVideoInfo>, IEpisodeIdable, ICameraIdable, IFrameable {

  /**
   * Возвращает длительность исходного видео.
   *
   * @return int - длительность видео в секундах
   */
  int duration();

  /**
   * Возвращает название место расположения камеры.
   *
   * @return String - место расположения камеры
   */
  String location();

}
