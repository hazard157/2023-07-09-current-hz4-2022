package com.hazard157.prisex30.pre.psx;

import java.time.*;

import org.toxsoft.core.tslib.bricks.strid.*;

import com.hazard157.prisex30.pre.psx.props.*;

public interface IPsxIncident
    extends IStridable, IPlaceable {

  /**
   * Returns the incident kind.
   * <p>
   * Depending on kind this interface may be cast to any of it's subclass.
   *
   * @return {@link EPsxIncidentKind} - incident kind
   */
  EPsxIncidentKind incidentKind();

  /**
   * Returns the incident starting time.
   *
   * @return {@link LocalDateTime} - incident starting time
   */
  LocalDateTime when();

  /**
   * Returns the incident date.
   *
   * @return {@link LocalDateTime} - incident date
   */
  default LocalDate incidentDate() {
    return when().toLocalDate();
  }

}
