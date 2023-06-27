package com.hazard157.prisex24.e4.handlers;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;

import com.hazard157.prisex24.glib.dialogs.*;

/**
 * Command: invokes {@link DialogWorkWithFrames} dialog to work with rames.
 *
 * @author hazard157
 */
public class CmdWorkWithFrames {

  @Execute
  void exec( IEclipseContext aContext ) {
    DialogWorkWithFrames.openAll( new TsGuiContext( aContext ), null );
  }

}
