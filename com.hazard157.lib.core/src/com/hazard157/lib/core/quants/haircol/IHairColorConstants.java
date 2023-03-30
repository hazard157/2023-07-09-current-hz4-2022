package com.hazard157.lib.core.quants.haircol;

import static com.hazard157.lib.core.quants.haircol.IHzResources.*;
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
public interface IHairColorConstants {

  /**
   * ID of the data definition {@link #DDEF_HAIR_COLOR}.
   */
  String DDID_HAIR_COLOR = "HairColor"; //$NON-NLS-1$

  /**
   * ID of the model {@link HairColorM5Model}.
   */
  String MID_HAIR_COLOR = DDID_HAIR_COLOR;

  /**
   * ID of the M5-attribute field {@link EHairColor}.
   */
  String FID_HAIR_COLOR = DDID_HAIR_COLOR;

  /**
   * Data definition for {@link EHairColor} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_HAIR_COLOR = DataDef.create( DDID_HAIR_COLOR, VALOBJ, //
      TSID_NAME, STR_N_M5M_HAIR_COLOR, //
      TSID_DESCRIPTION, STR_D_M5M_HAIR_COLOR, //
      TSID_KEEPER_ID, EHairColor.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EHairColor.UNKNOWN ) //
  );

}
