package com.hazard157.psx.common.filesys.olds.psx12;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Source video files management.
 *
 * @author goga
 */
public interface IFsSourceVideoFilesManager {

  /**
   * Возвращает файл исходного видео, если таковой существует.
   *
   * @param aSourceVideoId String - идентификатор исходного видео
   * @return {@link File} - файл исходного видео или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  File findSourceVideoFile( String aSourceVideoId );

}
