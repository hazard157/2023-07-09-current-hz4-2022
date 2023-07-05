package com.hazard157.prisex24.cofs;

import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.opfil.*;

/**
 * COFS - access to the gaze source and output media files.
 *
 * @author hazard157
 */
public interface ICofsGazes {

  /**
   * Lists all media files of the specified kind and date of the gaze.
   * <p>
   * If no such gaze or corresponding directory exists then returns an empty list.
   *
   * @param aDate {@link LocalDate} - the gaze date
   * @param aMediaKind {@link EIncidentMediaKind} - the requested media kind
   * @return {@link IList}&lt;{@link IOptedFile}&gt; - all files in date directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IOptedFile> listMediaFiles( LocalDate aDate, EIncidentMediaKind aMediaKind );

}
