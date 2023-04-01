package com.hazard157.psx24.core.e4.services.currframeslist;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Служба реализации понятия "текущеий список кадров для просмотра в вью".
 *
 * @author hazard157
 */
public interface ICurrentFramesListService
    extends ICurrentEntityService<IList<IFrame>>, ITsSelectionProvider<IFrame> {

  /**
   * Задает новый список кадров в виде кадров в указанном интервале эпизода.
   * <p>
   * Показывает только анимированные кадры, если нет ни одного анимированного - хотя бы один единичный кадр.
   * <p>
   * Служба сама внутри преобразует в список {@link IFrame}-ов.
   *
   * @param aSvin {@link Svin} - интервал эпизода, возможно с указанием камеры, может быть <code>null</code>
   * @param aShowSomething boolean - показать хоть что-нибудь - если нет GIF, то хоть просто кадр
   */
  void setCurrentAsSvin( Svin aSvin, boolean aShowSomething );

  /**
   * Задает новый список кадров в виде кадров в указанных интервалах эпизодов.
   * <p>
   * Служба сама внутри преобразует в список {@link IFrame}-ов.
   *
   * @param aSvins {@link IList}&lt;{@link Svin}&gt; - отображаемые интервалы
   * @param aShowSomething boolean - показать хоть что-нибудь - если нет GIF, то хоть просто кадр
   */
  void setCurrentAsSvins( IList<Svin> aSvins, boolean aShowSomething );

  /**
   * Задает новый список кадров в виде анимированных кадров в указанном интервале эпизода.
   * <p>
   * Равнозначно вызову {@link #setCurrentAsSvin(Svin, boolean) setCurrentAsSvin(Svin, <b>false</b>)}
   *
   * @param aSvin {@link Svin} - интервал эпизода, возможно с указанием камеры, может быть <code>null</code>
   */
  default void setCurrentAsSvin( Svin aSvin ) {
    setCurrentAsSvin( aSvin, false );
  }

}
