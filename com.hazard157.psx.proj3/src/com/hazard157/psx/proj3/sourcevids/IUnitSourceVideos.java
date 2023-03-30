package com.hazard157.psx.proj3.sourcevids;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Менеджер исходных видео.
 *
 * @author hazard157
 */
public interface IUnitSourceVideos
    extends ISinentManager<ISourceVideo, SourceVideoInfo> {

  /**
   * Возвращает исходные видео заданного эпизода.
   * <p>
   * Если такого эпизода не существует, возвращает пустую карту.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @return IStringMap&lt;{@link ISourceVideo}&gt; - карта "ИД камеры" - "исходное видео"
   * @throws TsNullArgumentRtException аргумент = null
   */
  IStringMap<ISourceVideo> episodeSourceVideos( String aEpisodeId );

  /**
   * Возвращает исходные видео, снятые заданной камерой.
   * <p>
   * Если такой камеры не существует, возвращает пустую карту.
   *
   * @param aCameraId String - идентификатор камеры
   * @return {@link IStridablesList}&lt;{@link ISourceVideo}&gt; - список исходных видео
   * @throws TsNullArgumentRtException аргумент = null
   */
  IStridablesList<ISourceVideo> listCameraSourceVideos( String aCameraId );

}
