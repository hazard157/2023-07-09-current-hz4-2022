package com.hazard157.prisex30.pre.proj30;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

import com.hazard157.prisex30.pre.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPrisexProject30Constants {

  // ------------------------------------------------------------------------------------
  // icons
  //

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  String ICONID_XXX                = "xxx";     //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPrisexProject30Constants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
