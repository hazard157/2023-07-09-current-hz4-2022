package com.hazard157.psx.proj3.gaze;

import java.time.*;

import org.toxsoft.core.tslib.bricks.strid.*;

import com.hazard157.lib.core.quants.rating.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * Incident of type {@link EPsxIncidentKind#GAZE}.
 *
 * @author hazard157
 */
public interface IGaze
    extends IPsxIncident, IStridableParameterized, IRatingable {

  /**
   * Returns the incident date.
   *
   * @return {@link LocalDateTime} - incident date
   */
  LocalDate incidentDate();

}
