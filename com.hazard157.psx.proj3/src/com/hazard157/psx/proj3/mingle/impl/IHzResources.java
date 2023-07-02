package com.hazard157.psx.proj3.mingle.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IHzResources {

  String STR_MINGLE_ID_MUST_BE           = MingleUtils.CHAR_MINGLE_ID_PREFIX + "ГГГГ_ММ_ДД";
  String FMT_ERR_INV_MINGLE_ID_TOO_SHORT = "Mingle ID '%s' is too short, must be like " + STR_MINGLE_ID_MUST_BE;
  String FMT_ERR_INV_MINGLE_ID_TOO_LONG  = "Mingle ID '%s' is too long, must be like " + STR_MINGLE_ID_MUST_BE;
  String FMT_ERR_INV_MINGLE_ID_INV_START = "Mingle ID '%s' must start with char '" + MingleUtils.CHAR_MINGLE_ID_PREFIX;
  String FMT_ERR_INV_MINGLE_ID_FORMAT    = "Invalid mingle ID '%s', must looks like " + STR_MINGLE_ID_MUST_BE;
  String FMT_ERR_DATE_AND_ID_MISMATCH    = "Specified date %s and date from ID %s does not match";

}
