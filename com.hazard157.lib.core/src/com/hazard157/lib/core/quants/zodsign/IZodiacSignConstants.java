package com.hazard157.lib.core.quants.zodsign;

import static com.hazard157.lib.core.quants.zodsign.IHzResources.*;
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
public interface IZodiacSignConstants {

  /**
   * ID of the data definition {@link #DDEF_ZODIAC_SIGN}.
   */
  String DDID_ZODIAC_SIGN = "ZodiacSign"; //$NON-NLS-1$

  /**
   * ID of the model {@link ZodiacSignM5Model}.
   */
  String MID_ZODIAC_SIGN = DDID_ZODIAC_SIGN;

  /**
   * ID of the M5-attribute field {@link EZodiacSign}.
   */
  String FID_ZODIAC_SIGN = DDID_ZODIAC_SIGN;

  /**
   * Data definition for {@link EZodiacSign} atomic {@link EAtomicType#VALOBJ} type.
   */
  IDataDef DDEF_ZODIAC_SIGN = DataDef.create( DDID_ZODIAC_SIGN, VALOBJ, //
      TSID_NAME, STR_N_M5M_ZODIAC_SIGN, //
      TSID_DESCRIPTION, STR_D_M5M_ZODIAC_SIGN, //
      TSID_KEEPER_ID, EZodiacSign.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EZodiacSign.UNKNOWN ) //
  );

}
