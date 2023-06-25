package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Сцена, как реализация {@link IScene}.
 *
 * @author hazard157
 */
class Scene
    extends AbstractScene {

  /**
   * Конструктор.
   *
   * @param aParent {@link AbstractScene} - родительская сцена
   * @param aIn {@link Secint} - интервал эпизода в виде [0,x)
   * @param aInfo {@link SceneInfo} - описание сцены
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException интервал выходит за пределы родителя
   */
  public Scene( AbstractScene aParent, Secint aIn, SceneInfo aInfo ) {
    super( TsNullArgumentRtException.checkNull( aParent ), aIn, aInfo );
    TsIllegalArgumentRtException.checkFalse( aParent.interval().contains( aIn ) );
  }

  @Override
  protected Story getRoot() {
    return doGetParent().getRoot();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    return info().frame();
  }

}
