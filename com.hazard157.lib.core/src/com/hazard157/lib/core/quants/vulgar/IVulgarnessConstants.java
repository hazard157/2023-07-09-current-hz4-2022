package com.hazard157.lib.core.quants.vulgar;

import static com.hazard157.lib.core.quants.vulgar.IHzResources.*;
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
public interface IVulgarnessConstants {

  /**
   * ID of the data definition {@link #DDEF_VULGARNESS}.
   */
  String DDID_VULGARNESS = "Vulgarness"; //$NON-NLS-1$

  /**
   * ID of the model {@link VulgarnessM5Model}.
   */
  String MID_VULGARNESS = DDID_VULGARNESS;

  /**
   * ID of the M5-attribute field {@link EVulgarness}.
   */
  String FID_VULGARNESS = DDID_VULGARNESS;

  /**
   * Data definition for {@link EVulgarness} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_VULGARNESS = DataDef.create( DDID_VULGARNESS, VALOBJ, //
      TSID_NAME, STR_N_M5M_VULGARNESS, //
      TSID_DESCRIPTION, STR_D_M5M_VULGARNESS, //
      TSID_KEEPER_ID, EVulgarness.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EVulgarness.UNKNOWN ) //
  );

}
