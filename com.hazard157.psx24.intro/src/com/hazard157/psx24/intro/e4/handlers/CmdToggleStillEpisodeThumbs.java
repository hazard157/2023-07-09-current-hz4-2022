package com.hazard157.psx24.intro.e4.handlers;

import static com.hazard157.psx24.intro.IPsxIntroGuiConstants.*;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.psx24.intro.*;

/**
 * Command toggles {@link IPsxIntroGuiConstants#APPRM_IS_FORCE_STILL_FRAME} preference value.
 *
 * @author hazard157
 */
public class CmdToggleStillEpisodeThumbs {

  @Execute
  void exec( IAppPreferences aAppPrefs, ITsE4Helper aE4Helper ) {
    IPrefBundle pb = aAppPrefs.getBundle( PSX_INTRO_APREF_BUNDLE_ID );
    boolean isStillForced = APPRM_IS_FORCE_STILL_FRAME.getValue( pb.prefs() ).asBool();
    pb.prefs().setBool( APPRM_IS_FORCE_STILL_FRAME, !isStillForced );
    aE4Helper.updateHandlersCanExecuteState();
  }

  // @CanExecute
  // boolean canExec( IAppPreferences aAppPrefs, ITsE4Helper aE4Helper, MPart aPart ) {
  // IPrefBundle pb = aAppPrefs.getBundle( PSX_INTRO_APREF_BUNDLE_ID );
  // boolean isStillForced = APPRM_IS_FORCE_STILL_FRAME.getValue( pb.prefs() ).asBool();
  // MHandledToolItem toolItem = aE4Helper.findElement( aPart.getToolbar(), TOOLBTNID_TOGGLE_FORCE_STILL,
  // MHandledToolItem.class, EModelService.IN_TRIM );
  // toolItem.setSelected( isStillForced );
  // return true;
  // }

}
