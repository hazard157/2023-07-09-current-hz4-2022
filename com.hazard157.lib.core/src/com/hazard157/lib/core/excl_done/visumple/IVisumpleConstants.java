package com.hazard157.lib.core.excl_done.visumple;

import static com.hazard157.lib.core.excl_done.visumple.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
public interface IVisumpleConstants {

  /**
   * ID of the field {@link IVisumplable#visumples()}.
   */
  String FID_VISUMPLES = "Visumples"; //$NON-NLS-1$

  /**
   * Option to store {@link IVisumplable#visumples()} in {@link IParameterized#params()}.
   * <p>
   * It is applicable when implementaion of {@link IVisumplable} is based on {@link IParameterized}.
   */
  IDataDef OPDEF_VISUMPLES = DataDef.create( FID_VISUMPLES, VALOBJ, //
      TSID_NAME, STR_N_FIELD_VISUMPLES, //
      TSID_DESCRIPTION, STR_D_FIELD_VISUMPLES, //
      TSID_KEEPER_ID, VisumplesListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, AvUtils.avValobj( new VisumplesList() ) //
  );

  /**
   * ID of {@link IVisumpleConstants#OP_NOTES}.
   */
  String FID_NOTES = "notes"; //$NON-NLS-1$

  /**
   * ID of {@link IVisumpleConstants#OP_TAGS}.
   */
  String FID_TAGS = "tags"; //$NON-NLS-1$

  /**
   * Option to store {@link Visumple#notes()} in {@link Visumple#params()}.
   */
  IDataDef OP_NOTES = DataDef.create( FID_NOTES, STRING, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Option to store {@link Visumple#tags()} in {@link Visumple#params()}.
   */
  IDataDef OP_TAGS = DataDef.create( FID_TAGS, VALOBJ, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ) //
  );

}
