package com.hazard157.psx.proj3.trailers;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Компонента файла проекта - менеджер трейлеров.
 *
 * @author hazard157
 */
public interface IUnitTrailers
    extends IProjDataUnit {

  /**
   * Возвращает менеджер собственно трейлеров.
   *
   * @return {@link ISinentManager}&lt;{@link TrailerInfo},{@link TrailerInfo}&gt; - менеджер трейлеров
   */
  ISinentManager<Trailer, TrailerInfo> tsm();

  /**
   * Возвращает трейлеры заданного эпизода.
   * <p>
   * Если такого эпизода нет, возвращает пустую карту.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @return IStringMap&lt;{@link Trailer}&gt; - список трейлеров запрошенного эпизода
   * @throws TsNullArgumentRtException аргумент = null
   */
  IStridablesList<Trailer> listTrailersByEpisode( String aEpisodeId );

  /**
   * Возвращает списокописаний осмысленных видов трейлеров,
   *
   * @return {@link INotifierStridablesListEdit}&lt;{@link SensibleTrailerInfo}&gt; - редактируемый список
   */
  INotifierStridablesListEdit<SensibleTrailerInfo> sensibleTrailerInfos();

}
