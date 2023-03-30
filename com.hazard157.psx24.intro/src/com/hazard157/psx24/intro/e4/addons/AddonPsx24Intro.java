package com.hazard157.psx24.intro.e4.addons;

import static com.hazard157.psx24.intro.IPsxIntroGuiConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.psx24.intro.*;
import com.hazard157.psx24.intro.utils.*;

/**
 * Адон приложения.
 *
 * @author goga
 */
public class AddonPsx24Intro
    extends MwsAbstractAddon {

  /**
   * Конструктор.
   */
  public AddonPsx24Intro() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IPsxIntroGuiConstants.init( aWinContext );
    ITsE4Helper helper = aWinContext.get( ITsE4Helper.class );
    Display display = aWinContext.get( Display.class );
    // жестко зададим начальный вью - экран приветствия
    display.asyncExec( () -> helper.switchToPerspective( PERSPID_PRISEX_INTRO, PARTID_PRISEX_INTRO_EPISODE_THUMBS ) );
    // покажем, если нужно приветственный GIF
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle prefBundle = aprefs.getBundle( PSX_INTRO_APREF_BUNDLE_ID );
    if( IPsxIntroGuiConstants.APPRM_IS_STARTUP_GIF_SHOWN.getValue( prefBundle.prefs() ).asBool() ) {
      display.asyncExec( () -> {
        StartupGifDisplay sgd = new StartupGifDisplay( aWinContext );
        sgd.show();
      } );
    }
  }

}
