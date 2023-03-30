package com.hazard157.psx.common.filesys.olds.psx12.films;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link IFsFilmFilesManager} constants.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public interface IFsFilmFilesManagerConstants {

  IDataDef QUERY_PARAM_IS_LEGACY_FILEMS_INCLUDED = DataDef.create( "IsLegacyFilmsIncluded", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

}
