package com.hazard157.lib.core.excl_done.acsupart;

import org.toxsoft.core.tsgui.bricks.actions.ITsActionDef;

/**
 * The handler of action wich has an ID {@link ITsActionDef#id()}.
 *
 * @author hazard157
 */
public interface ITsActionProcessor {

  /**
   * Prefroms an action if it belongs to this processor.
   *
   * @param aActionId String - an action ID
   * @return boolean - determines if specified action belongs to this processor<br>
   *         <b>true</b> - yes, action is handled by this process, no need to try other processors;<br>
   *         <b>false</b> - this processor will not handle action, try other processors in chain.
   */
  boolean processAction( String aActionId );

}
