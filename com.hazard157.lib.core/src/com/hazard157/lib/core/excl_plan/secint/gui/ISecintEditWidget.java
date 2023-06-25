package com.hazard157.lib.core.excl_plan.secint.gui;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Widget for {@link Secint} values editing.
 *
 * @author hazard157
 */
public interface ISecintEditWidget
    extends IGenericChangeEventCapable, ILazyControl<Control> {

  /**
   * Determines if hours part is used in "HH:MM:SS" represenataion.
   * <p>
   * Depending on the representation format ("MM:SS" or "HH:MM:SS"), the maximum values of the beginning and end of the
   * interval are limited (respectively, by the value {@link HmsUtils#MAX_MMSS_VALUE} or
   * {@link HmsUtils#MAX_HHMMSS_VALUE}). *
   *
   * @return boolean - true for "MM:SS" format, false for "HH:MM:SS" format
   */
  boolean isOnlyMmSs();

  /**
   * Sets the representation format "MM:SS" or "HH:MM:SS" format
   *
   * @param aIsOnlyMmSs boolean - true for "MM:SS" format, false for "HH:MM:SS" format
   */
  void setOnlyMmSs( boolean aIsOnlyMmSs );

  /**
   * Determines if panel content editing is allowed right now.
   *
   * @return boolean - edit mode flag
   */
  boolean isEditable();

  /**
   * Toggles panel content edit mode.
   *
   * @param aEditable boolean - edit mode flag
   */
  void setEditable( boolean aEditable );

  /**
   * Returns allowed range of the interval.
   * <p>
   * The widget guarantees that neither programmatically nor using the GUI can set an interval that goes beyond these
   * limits.
   *
   * @return {@link Secint} - the range of the interval {@link #getValue()}
   */
  Secint getLimits();

  /**
   * Set new editing range.
   *
   * @param aMinStart int - the minimum allowed value of the editable {@link Secint#start()}
   * @param aMaxEnd int - the maximum allowed value of the edited {@link Secint#end()}
   * @throws TsValidationFailedRtException failed {@link Secint#checkCanCreate(int, int)}
   */
  void setLimits( int aMinStart, int aMaxEnd );

  /**
   * Returns the current edited value.
   *
   * @return {@link Secint} - the current value, is alwayes in range {@link #getLimits()}
   */
  Secint getValue();

  /**
   * Sets the edited value.
   *
   * @param aValue {@link Secint} - the new value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException the value is out of range {@link #getLimits()}
   */
  void setValue( Secint aValue );

  /**
   * Sets the edited value.
   *
   * @param aStart int - initial (inclusive) second of the interval (must be >= 0)
   * @param aEnd int - end (inclusive) second of interval (must be >= aStart)
   * @throws TsValidationFailedRtException failed {@link Secint#checkCanCreate(int, int)}
   * @throws TsValidationFailedRtException the value is out of range {@link #getLimits()}
   */
  void setValue( int aStart, int aEnd );

}
