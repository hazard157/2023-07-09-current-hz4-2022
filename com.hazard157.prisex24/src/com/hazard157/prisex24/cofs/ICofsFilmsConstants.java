package com.hazard157.prisex24.cofs;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Constants to work with COFS.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ICofsFilmsConstants {

  // ------------------------------------------------------------------------------------
  // Output media

  String OPID_FILMS_QP_INCLUDE_LEGACY     = "IncludeLegacy";     //$NON-NLS-1$
  String OPID_FILMS_QP_INCLUDE_DEVELOPING = "IncludeDeveloping"; //$NON-NLS-1$

  IDataDef OPDEF_FILMS_QP_INCLUDE_LEGACY = DataDef.create( OPID_FILMS_QP_INCLUDE_LEGACY, BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  IDataDef OPDEF_FILMS_QP_INCLUDE_DEVELOPING = DataDef.create( OPID_FILMS_QP_INCLUDE_DEVELOPING, BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  IStridablesList<IDataDef> ALL_FILMS_QP_OPS = new StridablesList<>( //
      OPDEF_FILMS_QP_INCLUDE_LEGACY, //
      OPDEF_FILMS_QP_INCLUDE_DEVELOPING //
  );

}
