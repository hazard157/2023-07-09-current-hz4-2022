package com.hazard157.psx.proj3export;

import org.osgi.framework.*;

/**
 * The plugin activator.
 *
 * @author hazard157
 */
public class Activator
    implements BundleActivator {

  private static BundleContext context;

  static BundleContext getContext() {
    return context;
  }

  @Override
  public void start( BundleContext bundleContext )
      throws Exception {
    Activator.context = bundleContext;
  }

  @Override
  public void stop( BundleContext bundleContext )
      throws Exception {
    Activator.context = null;
  }

}
