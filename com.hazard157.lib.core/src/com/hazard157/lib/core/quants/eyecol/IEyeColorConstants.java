package com.hazard157.lib.core.quants.eyecol;

import static com.hazard157.lib.core.quants.eyecol.IHzResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
public interface IEyeColorConstants {

  /**
   * ID of the data definition {@link #DDEF_EYE_COLOR}.
   */
  String DDID_EYE_COLOR = "EyeColor"; //$NON-NLS-1$

  /**
   * ID of the model {@link EyeColorM5Model}.
   */
  String MID_EYE_COLOR = DDID_EYE_COLOR;

  /**
   * ID of the M5-attribute field {@link EEyeColor}.
   */
  String FID_EYE_COLOR = DDID_EYE_COLOR;

  /**
   * Data definition for {@link EEyeColor} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_EYE_COLOR = DataDef.create( DDID_EYE_COLOR, VALOBJ, //
      TSID_NAME, STR_N_M5M_EYE_COLOR, //
      TSID_DESCRIPTION, STR_D_M5M_EYE_COLOR, //
      TSID_KEEPER_ID, EEyeColor.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EEyeColor.UNKNOWN ) //
  );

}
