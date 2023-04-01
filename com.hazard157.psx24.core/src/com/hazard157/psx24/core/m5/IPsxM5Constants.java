package com.hazard157.psx24.core.m5;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * Константы M5-моделирования сущностей PSX.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxM5Constants {

  String MID_TAG     = PSX_ID + ".Tag";     //$NON-NLS-1$
  String MID_EPISODE = PSX_ID + ".Episode"; //$NON-NLS-1$
  String MID_FRAME   = PSX_ID + ".Frame";   //$NON-NLS-1$
  String MID_SCENE   = PSX_ID + ".Scene";   //$NON-NLS-1$
  String MID_SVIN    = PSX_ID + ".Svin";    //$NON-NLS-1$

  String FID_FRAME_NO        = "FrameNo";          //$NON-NLS-1$
  String FID_IS_ANIMATED     = "IsAnimated";       //$NON-NLS-1$
  String FID_CAMERA_ID       = "CameraId";         //$NON-NLS-1$
  String FID_FRAME_IMAGE     = "FrameImage";       //$NON-NLS-1$
  String FID_INTERVAL        = "Interval";         //$NON-NLS-1$
  String FID_DUR_PERC_STR    = "DurPercStr";       //$NON-NLS-1$
  String FID_WHEN            = "When";             //$NON-NLS-1$
  String FID_ACTION_INTERVAL = "ActInterval";      //$NON-NLS-1$
  String FID_PLACE           = "Place";            //$NON-NLS-1$
  String FID_DEF_TRAILER_ID  = "defaultTrailerId"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Data types

  int          MIN_M5_YEAR         = 1900;
  long         MIN_M5_YEAR_TIME    = -2_208_987_017_000L;            // 1900
  IAtomicValue MIN_M5_YEAR_AV      = avInt( MIN_M5_YEAR );
  IAtomicValue MIN_M5_YEAR_TIME_AV = avTimestamp( MIN_M5_YEAR_TIME );
  int          MAX_M5_YEAR         = 3000;
  long         MAX_M5_YEAR_TIME    = 32_503_680_000_000L;            // 3000
  IAtomicValue MAX_M5_YEAR_AV      = avInt( MAX_M5_YEAR );
  IAtomicValue MAX_M5_YEAR_TIME_AV = avTimestamp( MAX_M5_YEAR_TIME );
  String       FMT_DATE_DMY        = "%1$td.%1$tm.%1$tY";            // TIMESTAMP в виде "DD.MM.YYYY". //$NON-NLS-1$
  IAtomicValue FMT_DATE_DMY_AV     = avStr( FMT_DATE_DMY );
  String       FMT_DATE_YMD        = "%1$tY-%1$tm-%1$td";            // TIMESTAMP в виде "YYYY-MM-DD". //$NON-NLS-1$
  IAtomicValue FMT_DATE_YMD_AV     = avStr( FMT_DATE_YMD );

  /**
   * Метка времени, по умолчанию отображаемое как дата ДД.ММ.ГГГГ.
   */
  IDataType DT_DATE_DMY = new DataType( EAtomicType.TIMESTAMP, OptionSetUtils.createOpSet( //
      TSID_FORMAT_STRING, FMT_DATE_DMY_AV, //
      TSID_MIN_INCLUSIVE, MIN_M5_YEAR_TIME_AV, //
      TSID_MAX_EXCLUSIVE, MAX_M5_YEAR_TIME_AV, //
      TSID_DEFAULT_VALUE, AV_TIME_0 //
  ) );

  /**
   * Метка времени, по умолчанию отображаемое как дата ГГГГ-ММ-ДД.
   */
  IDataType DT_DATE_YMD = new DataType( EAtomicType.TIMESTAMP, OptionSetUtils.createOpSet( //
      TSID_FORMAT_STRING, FMT_DATE_YMD_AV, //
      TSID_MIN_INCLUSIVE, MIN_M5_YEAR_TIME_AV, //
      TSID_MAX_EXCLUSIVE, MAX_M5_YEAR_TIME_AV, //
      TSID_DEFAULT_VALUE, AV_TIME_0 //
  ) );

  /**
   * Год в качестве целого сила в пределах {@link #MIN_M5_YEAR} .. {@link #MAX_M5_YEAR}.
   */
  IDataType DT_YEAR = new DataType( EAtomicType.INTEGER, OptionSetUtils.createOpSet( //
      TSID_MIN_INCLUSIVE, MIN_M5_YEAR_AV, //
      TSID_MAX_EXCLUSIVE, MAX_M5_YEAR_AV, //
      TSID_DEFAULT_VALUE, avInt( 2020 ) //
  ) );

}
