package com.hazard157.psx.proj3.bricks.beq2.impl;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.bricks.beq2.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Методы работы с подсистемой BEQ.
 *
 * @author hazard157
 */
public class BeqUtils {

  /**
   * Создает набор, содержащий все интервалы всех эпизодов.
   *
   * @param aEpMan {@link IUnitEpisodes} - менеджер эпизодов
   * @return {@link IBeqResult} - "корневой" набор, откуда пойдет первый запрос
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static IBeqResult createFull( IUnitEpisodes aEpMan ) {
    IListEdit<Svin> list = new ElemLinkedBundleList<>();
    for( IEpisode e : aEpMan.items() ) {
      list.add( new Svin( e.id(), new Secint( 0, e.info().duration() - 1 ) ) );
    }
    return new BeqResult( list );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private BeqUtils() {
    // nop
  }

}
