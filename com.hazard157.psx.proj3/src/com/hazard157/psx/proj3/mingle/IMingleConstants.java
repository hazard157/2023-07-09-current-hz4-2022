package com.hazard157.psx.proj3.mingle;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.mingle.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

import com.hazard157.psx.proj3.incident.*;

@SuppressWarnings( "javadoc" )
public interface IMingleConstants {

  String OPID_NAME        = TSID_NAME;
  String OPID_DESCRIPTION = TSID_DESCRIPTION;
  String OPID_DATE        = PSX_ID + ".when";  //$NON-NLS-1$
  String OPID_PLACE       = PSX_ID + ".place"; //$NON-NLS-1$

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

  IDataDef OPDEF_PLACE = DataDef.create( OPID_PLACE, STRING, //
      TSID_NAME, STR_PLACE, //
      TSID_DESCRIPTION, STR_PLACE_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IStridablesList<IDataDef> ALL_MINGLE_OPS = new StridablesList<>( //
      OPDEF_NAME, //
      OPDEF_DESCRIPTION, //
      OPDEF_DATE, //
      OPDEF_PLACE //
  );

}
