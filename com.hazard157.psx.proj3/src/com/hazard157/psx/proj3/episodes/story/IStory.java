package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.*;

/**
 * Сюжет эпизода.
 * <p>
 * Содержит информацию о происходящем, безотносительно какими камерами и как снимался эпизод.
 *
 * @author hazard157
 */
public interface IStory
    extends IScene, IEpisodeIdable, IGenericChangeEventCapable, IKeepableEntity {

  /**
   * Возвращает эпизод, сюжет которого содержится в этом объекте.
   *
   * @return {@link Episode} - эпизод
   */
  // Episode episode();

  /**
   * Задает новый интервал сцены.
   *
   * @param aIn {@link Secint} - новый интервал
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException новый интервал меньше чем занятые интервалы дочерных сцен
   */
  void setSecint( Secint aIn );

  /**
   * Очищает сюжет.
   */
  void clear();

}
