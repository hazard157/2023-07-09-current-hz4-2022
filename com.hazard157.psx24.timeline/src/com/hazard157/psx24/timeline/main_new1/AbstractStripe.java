package com.hazard157.psx24.timeline.main_new1;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

public class AbstractStripe
    extends Canvas {

  public AbstractStripe( Composite aParent ) {
    super( aParent, SWT.NONE );
    addPaintListener( this::doPaint );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  final void doPaint( PaintEvent aEvent ) {
    // TODO AbstractStripe.doPaint()
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  protected void doPaintFlow( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {

  }

  protected void doPaintTitle( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {

  }

}
