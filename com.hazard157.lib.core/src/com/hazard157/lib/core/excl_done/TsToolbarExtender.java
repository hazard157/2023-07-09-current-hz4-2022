package com.hazard157.lib.core.excl_done;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Helper class to add actions to the existing toolbar.
 *
 * @author hazard157
 */
public class TsToolbarExtender {

  /**
   * Constructor for subclasses.
   */
  public TsToolbarExtender() {
    // nop
  }

  /**
   * Subclass may adjust created toolbar.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   *
   * @param aActDefs IList&lt;{@link ITsActionDef}&gt; - an editable list of actions for buttons creation
   */
  public void doSetupToolbarActions( IListEdit<ITsActionDef> aActDefs ) {
    // nop
  }

  /**
   * Here subclass must handle additional user actions added to the toolbar.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   *
   * @param aActionId String - the action ID
   */
  public void doProcessAction( String aActionId ) {
    // nop
  }

  /**
   * Here subclass must set state of the additional user action buttons added to the toolbar.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   */
  public void doUpdateActionsState() {
    // nop
  }

}
