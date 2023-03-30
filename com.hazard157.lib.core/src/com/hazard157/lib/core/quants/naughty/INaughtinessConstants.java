package com.hazard157.lib.core.quants.naughty;

import static com.hazard157.lib.core.quants.naughty.IHzResources.*;
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
public interface INaughtinessConstants {

  /**
   * ID of the data definition {@link #DDEF_NAUGHTINESS}.
   */
  String DDID_NAUGHTINESS = "Naughtiness"; //$NON-NLS-1$

  /**
   * ID of the model {@link NaughtinessM5Model}.
   */
  String MID_NAUGHTINESS = DDID_NAUGHTINESS;

  /**
   * ID of the M5-attribute field {@link ENaughtiness}.
   */
  String FID_NAUGHTINESS = DDID_NAUGHTINESS;

  /**
   * Data definition for {@link ENaughtiness} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_NAUGHTINESS = DataDef.create( DDID_NAUGHTINESS, VALOBJ, //
      TSID_NAME, STR_N_M5M_NAUGHTINESS, //
      TSID_DESCRIPTION, STR_D_M5M_NAUGHTINESS, //
      TSID_KEEPER_ID, ENaughtiness.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ENaughtiness.UNKNOWN ) //
  );

}
