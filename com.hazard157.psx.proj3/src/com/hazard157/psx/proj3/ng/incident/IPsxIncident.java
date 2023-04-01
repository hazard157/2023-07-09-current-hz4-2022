package com.hazard157.psx.proj3.ng.incident;

import java.time.*;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Base interface for all PSX incidents.
 *
 * @author hazard157
 */
public interface IPsxIncident
    extends IStridable {

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

}
