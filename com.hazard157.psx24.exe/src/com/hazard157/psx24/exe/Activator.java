package com.hazard157.psx24.exe;

import static com.hazard157.psx24.exe.IPsx24ExeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import org.eclipse.osgi.service.environment.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.mws.*;

import com.hazard157.psx.proj3.*;

/**
 * The plugin activator.
 *
 * @author hazard157
 */
public class Activator
    extends MwsActivator {

  /**
   * The plugin ID (for Java static imports).
   */
  public static final String PLUGIN_ID = "com.hazard157.psx24.exe"; //$NON-NLS-1$

  /**
   * Аргумент командной строки с указанием имени конфигурационного INI-файла.
   */
  public static final String CMDLINE_ARG_CFG_FILE_NAME = "config"; //$NON-NLS-1$

  /**
   * Имя конфигурационного ini-файла по умолчанию, расположенного в директории запуска.
   */
  public static final String DEFAULT_CFG_FILE_NAME = APP_ALIAS + ".cfg"; //$NON-NLS-1$

  private static Activator instance = null;

  /**
   * Constructor.
   */
  public Activator() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
  }

  @Override
  protected void doStart() {
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    mws.setAppInfo( APP_INFO );
    // configure TsProject plugin
    IUnitTxtprojMwsConstants.OPDEF_SHOW_CMD_IN_TOOLBAR.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_SHOW_CMD_IN_MENU.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_ALWAYS_USE_FILE_MENU.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_IMMEDIATE_LOAD_PROJ.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_PROJECT_FILE_FORMAT_INFO.setValue( mws.context().params(),
        avValobj( QuantPsx3Project.PROJECT_FILE_FORMAT_INFO ) );
    // настройки программы храним в INI-файле
    EnvironmentInfo envInfo = getOsgiService( EnvironmentInfo.class );
    ProgramArgs pa = new ProgramArgs( envInfo.getCommandLineArgs() );
    String cfgFileName = pa.getArgValue( CMDLINE_ARG_CFG_FILE_NAME, DEFAULT_CFG_FILE_NAME );
    File cfgFile = new File( cfgFileName );
    AbstractAppPreferencesStorage apStorage = new AppPreferencesConfigIniStorage( cfgFile );
    mws.context().put( AbstractAppPreferencesStorage.class, apStorage );
  }

  /**
   * Returns the reference to the activator singleton.
   *
   * @return {@link Activator} - the activator singleton
   */
  public static Activator getInstance() {
    return instance;
  }

}
