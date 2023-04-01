package com.hazard157.psx.proj3.gaze.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IHzResources {

  String STR_GAZE_ID_MUST_BE           = GazeUtils.CHAR_GAZE_ID_PREFIX + "ГГГГ_ММ_ДД";
  String FMT_ERR_INV_GAZE_ID_TOO_SHORT = "Gaze ID '%s' is too short, must be like " + STR_GAZE_ID_MUST_BE;
  String FMT_ERR_INV_GAZE_ID_TOO_LONG  = "Gaze ID '%s' is too long, must be like " + STR_GAZE_ID_MUST_BE;
  String FMT_ERR_INV_GAZE_ID_INV_START = "Gaze ID '%s' must start with char '" + GazeUtils.CHAR_GAZE_ID_PREFIX;
  String FMT_ERR_INV_GAZE_ID_FORMAT    = "Invalid gaze ID '%s', must looks like " + STR_GAZE_ID_MUST_BE;
  String FMT_ERR_DATE_AND_ID_MISMATCH  = "Specified date %s and date from ID %s does not match";

}
