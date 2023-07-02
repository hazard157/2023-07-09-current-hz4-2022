package com.hazard157.prisex24.cofs;

import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.fs.*;

/**
 * COFS - access to the mingle source and output media files.
 *
 * @author hazard157
 */
public interface ICofsMingles {

  /**
   * Lists all media files of the specified kind and date of the mingle.
   * <p>
   * If no such mingle or corresponding directory exists then returns an empty list.
   *
   * @param aDate {@link LocalDate} - the mingle date
   * @param aMediaKind {@link EIncidentMediaKind} - the requested media kind
   * @return {@link IList}&lt;{@link OptedFile}&gt; - all files in date directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<OptedFile> listMediaFiles( LocalDate aDate, EIncidentMediaKind aMediaKind );

}
