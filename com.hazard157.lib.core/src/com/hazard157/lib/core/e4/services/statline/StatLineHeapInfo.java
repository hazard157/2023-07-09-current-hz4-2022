package com.hazard157.lib.core.e4.services.statline;

import static com.hazard157.lib.core.e4.services.statline.IHzResources.*;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Status line tool control displays Java heap info.
 *
 * @author hazard157
 */
public class StatLineHeapInfo {

  @Inject
  Display display;

  Label label;

  @PostConstruct
  void init( Composite aParent ) {
    label = new Label( aParent, SWT.BORDER );
    label.setToolTipText( TOOLTIP_MEM_USAGE );
    refreshLabel();
  }

  @SuppressWarnings( "boxing" )
  void refreshLabel() {
    long freeMiB = Runtime.getRuntime().freeMemory() / 1048576L;
    long totalMiB = Runtime.getRuntime().totalMemory() / 1048576L;
    long maxMiB = Runtime.getRuntime().maxMemory() / 1048576L;
    String s = String.format( FMT_MEM_USAGE, freeMiB, totalMiB, maxMiB );
    label.setText( s );
    display.timerExec( 1000, this::refreshLabel );
  }

}
