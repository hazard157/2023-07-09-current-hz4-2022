package com.hazard157.lib.core.quants.stars5;

import static com.hazard157.lib.core.quants.stars5.IHzResources.*;
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
public interface IStarsFiveConstants {

  /**
   * ID of the data definition {@link #DDEF_STARS_FIVE}.
   */
  String DDID_STARS_FIVE = "StarsFive"; //$NON-NLS-1$

  /**
   * ID of the model {@link StarsFiveM5Model}.
   */
  String MID_STARS_FIVE = DDID_STARS_FIVE;

  /**
   * ID of the M5-attribute field {@link EStarsFive}.
   */
  String FID_STARS_FIVE = DDID_STARS_FIVE;

  /**
   * Data definition for {@link EStarsFive} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_STARS_FIVE = DataDef.create( DDID_STARS_FIVE, VALOBJ, //
      TSID_NAME, STR_N_M5M_STARS_FIVE, //
      TSID_DESCRIPTION, STR_D_M5M_STARS_FIVE, //
      TSID_KEEPER_ID, EStarsFive.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EStarsFive.UNKNOWN ) //
  );

}
