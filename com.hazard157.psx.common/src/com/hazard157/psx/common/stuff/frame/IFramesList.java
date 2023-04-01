package com.hazard157.psx.common.stuff.frame;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Список кадров.
 *
 * @author hazard157
 */

public interface IFramesList
    extends IList<IFrame> {

  /**
   * Находит ближайши к указанной секунде кадр из списка.
   *
   * @param aSec int - секунда кадра
   * @param aPreferredCamId String - идентификатор предпочитительной камеры или {@link IStridable#NONE_ID}
   * @return {@link IFrame} - ближайший кадр или null если список пустой
   */
  IFrame findNearest( int aSec, String aPreferredCamId );

}
