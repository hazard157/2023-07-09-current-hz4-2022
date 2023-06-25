package com.hazard157.psx.proj3.pleps;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.proj3.songs.*;

/**
 * Трек - часть песни {@link ISong}, исползуемый для построения саундтрека планируемого эпизода.
 *
 * @author hazard157
 */
public interface ITrack {

  /**
   * Возвращает идентификатор песни.
   *
   * @return String - идентификатор песни
   */
  String songId();

  /**
   * Возвращает интервал внутри песни, используемый как трек.
   *
   * @return Secint - интервал внутри песни
   */
  Secint interval();

  /**
   * Возвращает родительский планируемый эпизод.
   *
   * @return {@link IPlep} - родительский планируемый эпизод
   */
  IPlep plep();

  /**
   * Возвращает длитеность трека.
   *
   * @return int - длитеность трека в секундах
   */
  default int duration() {
    return interval().duration();
  }

  /**
   * Calculates interval of this TRACK in the PLEP.
   *
   * @return {@link Secint} - TRACK interval
   */
  Secint getIntervalInPlep();

}
