package com.hazard157.psx.common.filesys;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.filesys.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;

import com.hazard157.common.incub.fs.*;

/**
 * Constants with persistent values.
 *
 * @author hazard157
 */
public interface IPfsHardConstants {

  /**
   * ID of option {@link #OPDEF_ORIGINAL_MEDIA_DATE}.
   */
  String OPID_ORIGINAL_MEDIA_DATE = PSX_ID + ".date"; //$NON-NLS-1$

  /**
   * Option for {@link OptedFile#params()}: starting date-time.
   */
  IDataDef OPDEF_ORIGINAL_MEDIA_DATE = DataDef.create( OPID_ORIGINAL_MEDIA_DATE, VALOBJ, //
      TSID_NAME, STR_N_ORIGINAL_MEDIA_DATE, //
      TSID_DESCRIPTION, STR_D_ORIGINAL_MEDIA_DATE, //
      TSID_KEEPER_ID, LocalDateKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( LocalDate.ofInstant( Instant.now(), ZoneId.systemDefault() ) ) //
  );

}
