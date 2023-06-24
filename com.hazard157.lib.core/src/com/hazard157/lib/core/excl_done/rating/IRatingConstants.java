package com.hazard157.lib.core.excl_done.rating;

import static com.hazard157.lib.core.excl_done.rating.IHzResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;

/**
 * Package constants.
 *
 * @author hazard157
 */
public interface IRatingConstants {

  /**
   * ID of the data definition {@link #DDEF_RATING}.
   */
  String DDID_RATING = "Rating"; //$NON-NLS-1$

  /**
   * ID of the model {@link RatingM5Model}.
   */
  String MID_RATING = DDID_RATING;

  /**
   * ID of the M5-attribute field {@link ERating}.
   */
  String FID_RATING = DDID_RATING;

  /**
   * Data definition for {@link ERating} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_RATING = DataDef.create( DDID_RATING, VALOBJ, //
      TSID_NAME, STR_N_M5M_RATING, //
      TSID_DESCRIPTION, STR_D_M5M_RATING, //
      TSID_KEEPER_ID, ERating.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ERating.UNKNOWN ) //
  );

}
