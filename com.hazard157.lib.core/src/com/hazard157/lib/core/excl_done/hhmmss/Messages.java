package com.hazard157.lib.core.excl_done.hhmmss;

import org.eclipse.osgi.util.*;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "com.hazard157.lib.core.legacy.widgets.hhmmss"; //$NON-NLS-1$

  public static String ITgResources_FMT_ERR_SECS_TOO_HIGH;
  public static String ITgResources_FMT_ERR_SECS_TOO_LOW;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
