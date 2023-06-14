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
    extends IStridableParameterized, IRatingable {

  /**
   * Returns the incident kind.
   *
   * @return {@link EPsxIncidentKind} - the incident kind
   */
  EPsxIncidentKind incidentKind();

  /**
   * Returns the incident date.
   *
   * @return {@link LocalDateTime} - incident date
   */
  LocalDate incidentDate();

  /**
   * Returns the place where gaze happens.
   *
   * @return String - the place
   */
  String place();

}
