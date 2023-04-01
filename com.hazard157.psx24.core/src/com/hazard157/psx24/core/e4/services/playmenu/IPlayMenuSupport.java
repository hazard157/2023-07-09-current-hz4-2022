package com.hazard157.psx24.core.e4.services.playmenu;

import org.eclipse.jface.action.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.core.*;

/**
 * Поддержка реализации {@link IPsxAppActions#AI_PLAY_MENU}.
 * <p>
 * Экземпляр должен быть в контексте приложения.
 *
 * @author hazard157
 */
public interface IPlayMenuSupport {

  /**
   * Возвращает создатель меню для установки методом {@link IAction#setMenuCreator(IMenuCreator)}.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aParamsProvider {@link IPlayMenuParamsProvider} - предоставляет начальные параметры просмотра
   * @return {@link IMenuCreator} - создатель меню
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  IMenuCreator getPlayMenuCreator( ITsGuiContext aContext, IPlayMenuParamsProvider aParamsProvider );

}
