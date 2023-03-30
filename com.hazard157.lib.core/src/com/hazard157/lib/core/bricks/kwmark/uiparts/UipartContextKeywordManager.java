package com.hazard157.lib.core.bricks.kwmark.uiparts;

import com.hazard157.lib.core.bricks.kwmark.manager.*;

/**
 * Uipart to edit {@link IKeywordManager} found in windows context.
 *
 * @author goga
 */
public class UipartContextKeywordManager
    extends UipartAbstractKeywordManager {

  @Override
  protected IKeywordManager doGetKeywordManager() {
    return tsContext().get( IKeywordManager.class );
  }

}
