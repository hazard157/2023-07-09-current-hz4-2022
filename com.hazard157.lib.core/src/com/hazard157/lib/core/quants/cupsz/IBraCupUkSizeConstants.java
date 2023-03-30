package com.hazard157.lib.core.quants.cupsz;

import static com.hazard157.lib.core.quants.cupsz.IHzResources.*;
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
public interface IBraCupUkSizeConstants {

  /**
   * ID of the data definition {@link #DDEF_BRA_CUP_UK_SIZE}.
   */
  String DDID_BRA_CUP_UK_SIZE = "BraCupUkSize"; //$NON-NLS-1$

  /**
   * ID of the model {@link BraCupUkSizeM5Model}.
   */
  String MID_BRA_CUP_UK_SIZE = DDID_BRA_CUP_UK_SIZE;

  /**
   * ID of the M5-attribute field {@link EBraCupUkSize}.
   */
  String FID_BRA_CUP_UK_SIZE = DDID_BRA_CUP_UK_SIZE;

  /**
   * Data definition for {@link EBraCupUkSize} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_BRA_CUP_UK_SIZE = DataDef.create( DDID_BRA_CUP_UK_SIZE, VALOBJ, //
      TSID_NAME, STR_N_M5M_BRA_CUP_UK_SIZE, //
      TSID_DESCRIPTION, STR_D_M5M_BRA_CUP_UK_SIZE, //
      TSID_KEEPER_ID, EBraCupUkSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EBraCupUkSize.UNKNOWN ) //
  );

}
