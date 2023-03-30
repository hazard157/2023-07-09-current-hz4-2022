package com.hazard157.psx.proj3.ng.incident;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Incident utility methods.
 *
 * @author goga
 */

public class PsxIncidentUtils {

  /**
   * Creates {@link LocalDateTime} from epoch millisecods for zone "+03:00".
   *
   * @param aTimestamp - epoch millisecods
   * @return {@link LocalDateTime} - moment of time
   * @throws TsIllegalArgumentRtException - argument is out of PSX allowed interval
   */
  public static final LocalDateTime sysMillis2IncidentWhen( long aTimestamp ) {
    TsIllegalArgumentRtException.checkTrue( aTimestamp < MIN_TIMESTAMP );
    TsIllegalArgumentRtException.checkTrue( aTimestamp > MAX_TIMESTAMP );
    Instant instant = Instant.ofEpochMilli( aTimestamp );
    return LocalDateTime.ofInstant( instant, ZoneId.of( "+03:00" ) ); //$NON-NLS-1$
  }

  /**
   * No subclassing.
   */
  private PsxIncidentUtils() {
    // nop
  }

}
