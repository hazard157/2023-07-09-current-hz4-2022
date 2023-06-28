package com.hazard157.prisex24.e4.handlers;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.prisex24.*;

/**
 * Command toggles {@link IPrisex24CoreConstants#APPREF_WELCOME_IS_FORCE_STILL} preference value.
 *
 * @author hazard157
 */
public class CmdToggleStillEpisodeThumbs {

  @Execute
  void exec( IAppPreferences aAppPrefs, ITsE4Helper aE4Helper ) {
    IPrefBundle pb = aAppPrefs.getBundle( PBID_WELCOME );
    boolean isStillForced = APPREF_WELCOME_IS_FORCE_STILL.getValue( pb.prefs() ).asBool();
    pb.prefs().setBool( APPREF_WELCOME_IS_FORCE_STILL, !isStillForced );
    aE4Helper.updateHandlersCanExecuteState();
  }

}
