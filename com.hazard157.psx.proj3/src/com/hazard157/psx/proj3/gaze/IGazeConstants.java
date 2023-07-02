package com.hazard157.psx.proj3.gaze;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.gaze.IPsxResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;

import com.hazard157.common.quants.rating.*;
import com.hazard157.common.valed.radioprop.*;
import com.hazard157.psx.proj3.incident.*;

@SuppressWarnings( "javadoc" )
public interface IGazeConstants {

  String OPID_NAME        = TSID_NAME;
  String OPID_DESCRIPTION = TSID_DESCRIPTION;
  String OPID_DATE        = PSX_ID + ".when";   //$NON-NLS-1$
  String OPID_RATING      = PSX_ID + ".rating"; //$NON-NLS-1$
  String OPID_PLACE       = PSX_ID + ".place";  //$NON-NLS-1$

  IDataDef OPDEF_NAME = DataDef.create( OPID_NAME, STRING, //
      TSID_NAME, STR_NAME, //
      TSID_DESCRIPTION, STR_NAME_D, //
      TSID_DEFAULT_VALUE, DEFAULT_NAME_AV //
  );

  IDataDef OPDEF_DESCRIPTION = DataDef.create( OPID_DESCRIPTION, STRING, //
      TSID_NAME, STR_DESCRIPTION, //
      TSID_DESCRIPTION, STR_DESCRIPTION_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IDataDef OPDEF_DATE = DataDef.create( OPID_DATE, VALOBJ, //
      TSID_NAME, STR_DATE, //
      TSID_DESCRIPTION, STR_DATE_D, //
      TSID_KEEPER_ID, LocalDateKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( IPrisexIncidentConstants.DEFAULT_NEW_INCIDENT_DATE ) //
  );

  IDataDef OPDEF_RATING = DataDef.create( OPID_RATING, VALOBJ, //
      TSID_NAME, STR_RATING, //
      TSID_DESCRIPTION, STR_RATING_D, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjRadioPropEnumStars.FACTORY_NAME, //
      TSID_KEEPER_ID, ERating.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ERating.UNKNOWN ) //
  );

  IDataDef OPDEF_PLACE = DataDef.create( OPID_PLACE, STRING, //
      TSID_NAME, STR_PLACE, //
      TSID_DESCRIPTION, STR_PLACE_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

}
