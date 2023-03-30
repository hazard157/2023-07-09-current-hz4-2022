package com.hazard157.psx.proj3.episodes;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Менеджер эпизодов.
 *
 * @author hazard157
 */
public interface IUnitEpisodes
    extends ISinentManager<IEpisode, EpisodeInfo> {

  /**
   * Возвращает список иллюстрирующих эпизод (или его часть) кадров.
   * <p>
   * Метод ищет во всех сущностьях эпизода (сам эпизод, сюжет, иллюстрйии и т.п.) ссылки на кадры и возвращает их
   * список. Если в аргументе задана камера {@link Svin#cameraId()}, то возвращает только кадры, снятые этой камерой.
   * Если задан интервал {@link Svin#interval()}, то только кадры из этого интервала.
   *
   * @param aSvin {@link Svin} - идентификация эпизода (или его части)
   * @return IList&lt;{@link IFrame}&gt; - список кадров
   * @throws TsNullArgumentRtException аргумент = null
   */
  IList<IFrame> listUsedFrames( Svin aSvin );

}
