package com.hazard157.lib.core.excl_done.kwmark.uiparts;

import com.hazard157.lib.core.excl_done.kwmark.manager.*;

/**
 * Uipart to edit {@link IKeywordManager} found in windows context.
 *
 * @author hazard157
 */
public class UipartContextKeywordManager
    extends UipartAbstractKeywordManager {

  @Override
  protected IKeywordManager doGetKeywordManager() {
    return tsContext().get( IKeywordManager.class );
  }

}
