package com.hazard157.lib.core.excl_plan.secint.gui;

import static com.hazard157.lib.core.excl_plan.secint.gui.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.validators.defav.*;
import org.toxsoft.core.tslib.bricks.validator.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.lib.core.excl_plan.secint.valed.*;

/**
 * Константы M3-моделирования библиотеки MP.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls" } )
public interface ISecintM5Constants {

  /**
   * Префикс идентификаторов M5 моделирования.
   */
  String PSX_ID_PREFIX = "com.hazard157.psx.";

  // ------------------------------------------------------------------------------------
  // Position in video

  // /**
  // * Идентификатор типа {@link #DT_VIDEO_POSITION} для регистрации в {@link IDvInfoRegistry}.
  // */
  // String DTID_VIDEO_POSITION = "com.hazard157.VideoPosition"; //$NON-NLS-1$

  /**
   * Тип данного - продолжительность видео в секундах.
   */
  IDataType DT_VIDEO_POSITION = DataType.create( EAtomicType.INTEGER, //
      TSID_DEFAULT_VALUE, AV_0, //
      TSID_MIN_INCLUSIVE, AV_0, //
      TSID_MAX_EXCLUSIVE, HmsUtils.MAX_MMSS_VALUE_AV, //
      TSID_IS_NULL_ALLOWED, Boolean.FALSE, //
      TSID_NAME, avStr( STR_N_DT_VIDEO_POSITION ), //
      TSID_DESCRIPTION, avStr( STR_D_DT_VIDEO_POSITION ), //
      IValedControlConstants.OPID_EDITOR_FACTORY_NAME, ValedAvIntHhmmss.FACTORY_NAME, //
      ValedAvIntHhmmss.IS_ONLY_MMSS, AV_TRUE //
  );

  /**
   * Валидатор атомарного значения типа {@value #DT_VIDEO_POSITION}.
   */
  ITsValidator<IAtomicValue> VIDEO_POSITION_VALIDATOR_AV =
      new DefaultAvValidator( EAtomicType.INTEGER, DT_VIDEO_POSITION.params() );

  // ------------------------------------------------------------------------------------
  // Duration of video

  // /**
  // * Идентификатор типа {@link #DT_VIDEO_DURATION} для регистрации в {@link IDvInfoRegistry}.
  // */
  // String DTID_VIDEO_DURATION = "com.hazard157.VideoDuration"; //$NON-NLS-1$

  /**
   * Тип данного - продолжительность видео в секундах.
   */
  IDataType DT_VIDEO_DURATION = DataType.create( EAtomicType.INTEGER, //
      TSID_DEFAULT_VALUE, AV_1, //
      TSID_MIN_INCLUSIVE, AV_0, //
      TSID_MAX_INCLUSIVE, HmsUtils.MAX_MMSS_VALUE_AV, //
      TSID_IS_NULL_ALLOWED, Boolean.FALSE, //
      TSID_NAME, avStr( STR_N_DT_VIDEO_DURATION ), //
      TSID_DESCRIPTION, avStr( STR_D_DT_VIDEO_DURATION ), //
      IValedControlConstants.OPID_EDITOR_FACTORY_NAME, ValedAvIntHhmmss.FACTORY_NAME, //
      ValedAvIntHhmmss.IS_ONLY_MMSS, AV_TRUE //
  );

  /**
   * Валидатор атомарного значения типа {@value #DT_VIDEO_DURATION}.
   */
  ITsValidator<IAtomicValue> VIDEO_DURATION_VALIDATOR_AV =
      new DefaultAvValidator( EAtomicType.INTEGER, DT_VIDEO_DURATION.params() );

  // ------------------------------------------------------------------------------------
  // Interval in video

  // /**
  // * Идентификатор типа {@link #DT_VIDEO_INTERVAL} для регистрации в {@link IDvInfoRegistry}.
  // */
  // String DTID_VIDEO_INTERVAL = "com.hazard157.VideoInterval"; //$NON-NLS-1$

  /**
   * Тип данного - продолжительность видео в секундах.
   */
  IDataType DT_VIDEO_INTERVAL = DataType.create( EAtomicType.VALOBJ, //
      TSID_DEFAULT_VALUE, avValobj( new Secint( 0, 3 * 60 ) ), //
      TSID_IS_NULL_ALLOWED, Boolean.FALSE, //
      TSID_NAME, avStr( STR_N_DT_VIDEO_INTERVAL ), //
      TSID_DESCRIPTION, avStr( STR_D_DT_VIDEO_INTERVAL ), //
      IValedControlConstants.OPID_EDITOR_FACTORY_NAME, ValedAvSecintFactory.FACTORY_NAME //
  );

  /**
   * Регистрация всех констант из этого интерфейса.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   */
  // static void init( IEclipseContext aWinContext ) {
  // IDvInfoRegistry registry = aWinContext.get( IDvInfoRegistry.class );
  // // duraton type
  // DvInfo dvInfo =
  // new DvInfo( DTID_VIDEO_DURATION, STR_N_DT_VIDEO_DURATION, STR_D_DT_VIDEO_DURATION, DT_VIDEO_DURATION );
  // dvInfo.setValidator( VIDEO_DURATION_VALIDATOR_AV );
  // dvInfo.setDefaultValue( AV_0 );
  // dvInfo.setComparator( DEFAULT_AV_COMPARATOR );
  // registry.register( DTID_VIDEO_DURATION, dvInfo );
  // // duraton type
  // dvInfo = new DvInfo( DTID_VIDEO_INTERVAL, STR_N_DT_VIDEO_INTERVAL, STR_D_DT_VIDEO_INTERVAL, DT_VIDEO_INTERVAL );
  // dvInfo.setComparator( DEFAULT_AV_COMPARATOR );
  // registry.register( DTID_VIDEO_INTERVAL, dvInfo );
  // // position type
  // dvInfo = new DvInfo( DTID_VIDEO_POSITION, STR_N_DT_VIDEO_POSITION, STR_D_DT_VIDEO_POSITION, DT_VIDEO_POSITION );
  // dvInfo.setValidator( VIDEO_POSITION_VALIDATOR_AV );
  // dvInfo.setDefaultValue( AV_0 );
  // dvInfo.setComparator( DEFAULT_AV_COMPARATOR );
  // registry.register( DTID_VIDEO_POSITION, dvInfo );
  // }

}
