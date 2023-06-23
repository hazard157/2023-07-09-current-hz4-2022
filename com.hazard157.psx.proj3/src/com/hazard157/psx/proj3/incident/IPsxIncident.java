package com.hazard157.psx.proj3.incident;

import org.toxsoft.core.tslib.bricks.strid.*;

import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.place.*;

/**
 * Base class of all PSX incidents.
 *
 * @author hazard157
 */
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

}
