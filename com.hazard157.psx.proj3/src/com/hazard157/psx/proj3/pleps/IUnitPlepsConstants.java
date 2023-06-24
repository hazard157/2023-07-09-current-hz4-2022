package com.hazard157.psx.proj3.pleps;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.lib.core.quants.visumple.*;

/**
 * Константы {@link IUnitPleps}.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface IUnitPlepsConstants {

  IDataDef OP_STIR_NAME = DataDef.create( TSID_NAME, STRING, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IDataDef OP_STIR_DESCRIPTION = DataDef.create( TSID_DESCRIPTION, STRING, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IDataDef OP_STIR_DURATION = DataDef.create( "Duration", INTEGER, //
      TSID_DEFAULT_VALUE, AV_1, //
      TSID_MIN_INCLUSIVE, AV_0, //
      TSID_MAX_INCLUSIVE, HmsUtils.MAX_MMSS_VALUE_AV, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      // FIXME EValedControlParam.EDITOR_FACTORY_NAME, avStr( ValedAvIntHhmmss.FACTORY_NAME ), //
      // FIXME ValedAvIntHhmmss.IS_ONLY_MMSS, DV_TRUE, //
      TSID_DEFAULT_VALUE, avInt( 60 ) //
  );

  // TODO NO_STRI_THUMB
  // String FID_THUMB_FILE_PATH = "ThumbFilePath";
  //
  // IDataDef OP_STIR_THUMB_FILE_PATH = DataDef.create( FID_THUMB_FILE_PATH, STRING, //
  // TSID_IS_NULL_ALLOWED, Boolean.TRUE, //
  // // FIXME ValedAvImageFileNameViewer.VIEWER_SIZE, ThumbSizeKeeper.KEEPER.toAv( EThumbSize.SZ360 ), //
  // // FIXME EValedControlParam.EDITOR_FACTORY_NAME, avStr( ValedAvImageFileNameEditor.FACTORY_NAME ),
  // TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  // );

  IList<IDataDef> ALL_STIR_OPS = new ElemArrayList<>( //
      OP_STIR_NAME, //
      OP_STIR_DURATION, //
      OP_STIR_DESCRIPTION, //
      // TODO NO_STRI_THUMB OP_STIR_THUMB_FILE_PATH, //
      IVisumpleConstants.OPDEF_VISUMPLES //
  );

  String OPID_STIR_INSERTION_INDEX = "stirInsertionIndex";

  // эта опция опциональна, используется для передачи в менеджер ЖЦ предполагаемого места вставления нового IStir
  IDataDef OP_STIR_INSERTION_INDEX = DataDef.create( OPID_STIR_INSERTION_INDEX, INTEGER, //
      TSID_DEFAULT_VALUE, AV_N1 // add to the end of IPlep.stirs()
  );

  String OPID_TRACK_INSERTION_INDEX = "trackInsertionIndex";

  // эта опция опциональна, используется для передачи в менеджер ЖЦ предполагаемого места вставления нового ITrack
  IDataDef OP_TRACK_INSERTION_INDEX = DataDef.create( OPID_TRACK_INSERTION_INDEX, INTEGER, //
      TSID_DEFAULT_VALUE, AV_N1 // add to the end of IPlep.tracks()
  );

}
