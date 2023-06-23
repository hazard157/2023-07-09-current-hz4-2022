package com.hazard157.psx.common.stuff.place;

import static com.hazard157.psx.common.stuff.place.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPlaceableConstants {

  String OPID_PLACE = "place";   //$NON-NLS-1$
  String FID_PLACE  = OPID_PLACE;

  IDataDef DDEF_PLACE = DataDef.create( OPID_PLACE, STRING, //
      TSID_NAME, STR_PLACE, //
      TSID_DESCRIPTION, STR_PLACE_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

}
