package com.hazard157.psx.proj3.ng.incident;

import static com.hazard157.psx.common.IPsxHardConstants.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link EPsxIncidentKind}
   */
  String STR_N_INC_EPISODE = "Episode";
  String STR_D_INC_EPISODE = "Sex action was performed and filmed in one go";
  String STR_N_INC_GAZE    = "Gaze";
  String STR_D_INC_GAZE    = "She nude was caught and shot on camera";

  /**
   * {@link IncidentWhenValidator}
   */
  String FMT_ERR_INV_INCIDENT_DATE =
      "Дата %s инцидента должна быть в пределах " + MIN_PSX_DATE + " ... " + MAX_PSX_DATE;

}
