package com.hazard157.psx24.core.e4.services.playmenu;

import org.eclipse.jface.action.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.core.*;

/**
 * Support for {@link IPsxAppActions#AI_PLAY_MENU} implementation.
 * <p>
 * The instance must be in the windows level context.
 *
 * @author hazard157
 */
public interface IPlayMenuSupport {

  /**
   * Returns menu creator to be set by {@link IAction#setMenuCreator(IMenuCreator)}.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aParamsProvider {@link IPlayMenuParamsProvider} - play parameters provider
   * @return {@link IMenuCreator} - menu creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IMenuCreator getPlayMenuCreator( ITsGuiContext aContext, IPlayMenuParamsProvider aParamsProvider );

}
