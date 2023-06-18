package com.hazard157.prisex24.m5;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * PRISEX M5-modeling constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxM5Constants {

  String FID_WHEN     = "When";     //$NON-NLS-1$
  String FID_INTERVAL = "Interval"; //$NON-NLS-1$

  String MID_EPISODE         = PSX_ID + ".Episode"; //$NON-NLS-1$
  String FID_ACTION_INTERVAL = "ActInterval";       //$NON-NLS-1$
  String FID_PLACE           = "Place";             //$NON-NLS-1$
  String FID_DEF_TRAILER_ID  = "defaultTrailerId";  //$NON-NLS-1$

  String MID_FRAME       = PSX_ID + ".Frame"; //$NON-NLS-1$
  String FID_FRAME_NO    = "FrameNo";         //$NON-NLS-1$
  String FID_IS_ANIMATED = "IsAnimated";      //$NON-NLS-1$
  String FID_FRAME_IMAGE = "FrameImage";      //$NON-NLS-1$
  String FID_CAMERA_ID   = "CameraId";        //$NON-NLS-1$

  String MID_TODO             = PSX_ID + ".Todo"; //$NON-NLS-1$
  String FID_CREATION_TIME    = "CreationTime";   //$NON-NLS-1$
  String FID_PRIORITY         = "Priority";       //$NON-NLS-1$
  String FID_TEXT             = "Text";           //$NON-NLS-1$
  String FID_IS_DONE          = "IsDone";         //$NON-NLS-1$
  String FID_TODO_ID          = "TodoId";         //$NON-NLS-1$
  String FID_RELATED_TODO_IDS = "RelatedTodoIds"; //$NON-NLS-1$
  String FID_FULFIL_STAGES    = "FulfilStages";   //$NON-NLS-1$

  String MID_FULFIL_STAGE = PSX_ID + "FulfilStage"; //$NON-NLS-1$

  String MID_TAG = PSX_ID + ".Tag"; //$NON-NLS-1$

  String MID_SVIN = PSX_ID + ".Svin"; //$NON-NLS-1$

  String       FMT_DATE_DMY    = "%1$td.%1$tm.%1$tY";  // TIMESTAMP в виде "DD.MM.YYYY". //$NON-NLS-1$
  IAtomicValue FMT_DATE_DMY_AV = avStr( FMT_DATE_DMY );

  /**
   * Timestamp displayed as DD.MM.YYYY.
   */
  IDataType DT_DATE_DMY = new DataType( EAtomicType.TIMESTAMP, OptionSetUtils.createOpSet( //
      TSID_FORMAT_STRING, FMT_DATE_DMY_AV, //
      TSID_MIN_INCLUSIVE, avTimestamp( MIN_TIMESTAMP ), //
      TSID_MAX_EXCLUSIVE, avTimestamp( MAX_TIMESTAMP ), //
      TSID_DEFAULT_VALUE, AV_TIME_0 //
  ) );

}
