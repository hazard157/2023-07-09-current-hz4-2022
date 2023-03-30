package com.hazard157.lib.core.quants.sfz;

import static com.hazard157.lib.core.quants.sfz.IHzResources.*;
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
public interface IShotFieldSizeConstants {

  /**
   * ID of the data definition {@link #DDEF_SHOT_FIELD_SIZE}.
   */
  String DDID_SHOT_FIELD_SIZE = "ShotFieldSize"; //$NON-NLS-1$

  /**
   * ID of the model {@link ShotFieldSizeM5Model}.
   */
  String MID_SHOT_FIELD_SIZE = DDID_SHOT_FIELD_SIZE;

  /**
   * ID of the M5-attribute field {@link EShotFieldSize}.
   */
  String FID_SHOT_FIELD_SIZE = DDID_SHOT_FIELD_SIZE;

  /**
   * Data definition for {@link EShotFieldSize} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_SHOT_FIELD_SIZE = DataDef.create( DDID_SHOT_FIELD_SIZE, VALOBJ, //
      TSID_NAME, STR_N_M5M_SHOT_FIELD_SIZE, //
      TSID_DESCRIPTION, STR_D_M5M_SHOT_FIELD_SIZE, //
      TSID_KEEPER_ID, EShotFieldSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EShotFieldSize.UNKNOWN ) //
  );

}
