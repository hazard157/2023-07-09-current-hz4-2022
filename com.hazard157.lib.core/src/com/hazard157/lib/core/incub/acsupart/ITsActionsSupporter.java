package com.hazard157.lib.core.incub.acsupart;

import org.toxsoft.core.tsgui.bricks.actions.ITsActionDef;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;

/**
 * Mixin interface of some (usually GUI) entity supporting {@link ITsActionDef} actions.
 *
 * @author hazard157
 */
public interface ITsActionsSupporter
    extends ITsActionProcessor {

  /**
   * Returns the supported action definitions.
   *
   * @return {@link IStringMap}&lt;{@link ITsActionDef}&gt; - map "action ID" - "action definition"
   */
  IStringMap<ITsActionDef> listActions();

  /**
   * Determines if action is enabled.
   * <p>
   * For unknown action must return <code>false</code>.
   *
   * @param aActionId String - an action ID
   * @return boolean - <code>true</code> if action is enabled (may be processed)
   */
  boolean isActionEnabled( String aActionId );

  @Override
  boolean processAction( String aActionId );

}
