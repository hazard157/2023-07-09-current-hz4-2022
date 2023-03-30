package com.hazard157.psx.proj3.songs;

import static com.hazard157.psx.proj3.songs.IUnitSongsConstants.*;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Описание песны.
 *
 * @author hazard157
 */
public interface ISong
    extends IStridableParameterized {

  /**
   * Возвращает путь к файлу песни.
   *
   * @return String - путь к файлу песни
   */
  default String filePath() {
    return OP_FILE_PATH.getValue( params() ).asString();
  }

  /**
   * Возвращает длительность песни.
   *
   * @return int - длительность песни в секундах
   */
  default int duration() {
    return OP_DURATION.getValue( params() ).asInt();
  }

}
