package com.hazard157.prisex30.pre;

import static com.hazard157.prisex30.pre.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Application common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPrisex30CoreConstants {

  // ------------------------------------------------------------------------------------
  // Application info

  String    APP_ID      = "com.hazard157.prisex30";                         //$NON-NLS-1$
  String    APP_ALIAS   = "prisex30";                                       //$NON-NLS-1$
  TsVersion APP_VERSION = new TsVersion( 30, 0, 2023, Month.SEPTEMBER, 10 );

  ITsApplicationInfo APP_INFO = new TsApplicationInfo( APP_ID, STR_APP_INFO, STR_APP_INFO_D, APP_ALIAS, APP_VERSION );

  // ------------------------------------------------------------------------------------
  // PRISEX30

  String PSX_ID      = "psx30";                  //$NON-NLS-1$
  String PSX_FULL_ID = "com.hazard157.prisex30"; //$NON-NLS-1$
  String PSX_M5_ID   = PSX_ID + ".m5";           //$NON-NLS-1$
  String PSX_ACT_ID  = PSX_ID + ".act";          //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_XXX    = "com.hazard157.prisex30.persp.xxx";    //$NON-NLS-1$
  String PARTID_XXX     = "com.hazard157.prisex30.part.xxx";     //$NON-NLS-1$
  String TOOLITEMID_XXX = "com.hazard157.prisex30.toolitem.xxx"; //$NON-NLS-1$
  String CMDID_XXX      = "com.hazard157.prisex30.cmd.xxx";      //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$
  String ICONID_PORNICON           = "pornicon"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_XXX = PSX_ACT_ID + ".xxx"; //$NON-NLS-1$

  ITsActionDef ACDEF_XXX = TsActionDef.ofPush2( ACTID_XXX, //
      STR_APP_INFO, STR_APP_INFO_D, ICONID_APP_ICON );

  // ------------------------------------------------------------------------------------
  // Application preferences

  String PBID_PSX30_COMMON = APP_ID;

  IDataDef APPREF_XXX = DataDef.create( PSX_ID + "xxx", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_APP_INFO, //
      TSID_DESCRIPTION, STR_APP_INFO_D //
  );

  IStridablesList<IDataDef> ALL_APREFS = new StridablesList<>( //
      APPREF_XXX //
  );

  // ------------------------------------------------------------------------------------
  // Misc settings

  /**
   * Minimal frame thumb size, restricted due to prepared image files size.
   */
  EThumbSize PSX_MIN_FRAME_THUMB_SIZE = EThumbSize.SZ64;

  /**
   * Maximal frame thumb size, restricted due to prepared image files size.
   */
  EThumbSize PSX_MAX_FRAME_THUMB_SIZE = EThumbSize.SZ512;

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IPrisex30CoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PBID_PSX30_COMMON, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_APP_INFO, //
        TSID_DESCRIPTION, STR_APP_INFO_D, //
        TSID_ICON_ID, ICONID_APP_ICON //
    ) );
    for( IDataDef dd : ALL_APREFS ) {
      pb.defineOption( dd );
    }
  }

}
