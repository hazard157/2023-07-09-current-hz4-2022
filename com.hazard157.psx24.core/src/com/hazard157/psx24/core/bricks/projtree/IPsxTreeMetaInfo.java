package com.hazard157.psx24.core.bricks.projtree;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Опции хранения PSX-сецифичной мета-информации в узлах проекта.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface IPsxTreeMetaInfo {

  IDataDef PTN_OPDEF_IS_DIMMED = DataDef.create( "isDimmed", BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_FALSE );

  // IDataDef PTN_OPDEF_EPISODE_ID = DataDef.create( "episodeId", STRING );
  //
  // IDataDef PTN_OPDEF_CAMERA_ID = DataDef.create( "cameraId", STRING );
  //
  // IDataDef PTN_OPDEF_INTERVAL = DataDef.create( "interval", VALOBJ, TSID_KEEPER_ID, SecintKeeper.KEEPER_ID );
  //
  // IDataDef PTN_OPDEF_FRAME = DataDef.create( "frame", VALOBJ, TSID_KEEPER_ID, FrameKeeper.KEEPER_ID );
  //
  // IDataDef PTN_OPDEF_SVIN = DataDef.create( "svin", VALOBJ, TSID_KEEPER_ID, SvinKeeper.KEEPER_ID );
  //
  // IDataDef PTN_OPDEF_FILM_ID = DataDef.create( "filmId", STRING );
  //
  // IDataDef PTN_OPDEF_TRAILER_ID = DataDef.create( "trailerId", STRING );
  //
  // IDataDef PTN_OPDEF_AUDIO_FILE = DataDef.create( "audioFile", VALOBJ, //
  // TSID_KEEPER_ID, FileKeeper.KEEPER_ID );

}
