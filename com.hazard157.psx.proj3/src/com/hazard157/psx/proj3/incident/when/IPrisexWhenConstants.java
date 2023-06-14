package com.hazard157.psx.proj3.incident.when;

import static com.hazard157.psx.proj3.incident.when.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;

/**
 * Constants of the PEISEX incident date ("<i>when</i>"),
 *
 * @author hazard157
 */
public interface IPrisexWhenConstants {

  /**
   * Default valid value for timestamp.
   */
  long DEFAULT_TIMESTAMP = System.currentTimeMillis();

  /**
   * Default timestamp as {@link Instant}.
   */
  Instant DEFAULT_INSTANT = Instant.ofEpochMilli( DEFAULT_TIMESTAMP );

  /**
   * Default timestamp as {@link LocalDateTime}.
   */
  LocalDateTime DEFAULT_WHEN = LocalDateTime.ofInstant( DEFAULT_INSTANT, ZoneId.systemDefault() );

  /**
   * Min allowed episode date (1995-01-01).
   */
  LocalDate MIN_PSX_DATE = LocalDate.of( 1995, Month.JANUARY, 1 );

  /**
   * {@link LocalDateTime} of {@link #MIN_PSX_DATE}.
   */
  LocalDateTime MIN_PSX_DATE_TIME = LocalDateTime.of( MIN_PSX_DATE, LocalTime.NOON );

  /**
   * Max allowed episode date (2095-12-31).
   */
  LocalDate MAX_PSX_DATE = LocalDate.of( 2095, Month.DECEMBER, 31 );

  /**
   * {@link LocalDateTime} of {@link #MAX_PSX_DATE}.
   */
  LocalDateTime MAX_PSX_DATE_TIME = LocalDateTime.of( MAX_PSX_DATE, LocalTime.NOON );

  /**
   * ID of option {@link #OPDEF_WHEN}.
   */
  String OPID_WHEN = "when"; //$NON-NLS-1$

  /**
   * Option: value of a property {@link IWhenable#when()} - an incident date.
   */
  IDataDef OPDEF_WHEN = create( OPID_WHEN, VALOBJ, //
      TSID_NAME, STR_PROP_WHEN_D, //
      TSID_DESCRIPTION, STR_PROP_WHEN_D, //
      TSID_KEEPER_ID, LocalDateTimeKeeper.KEEPER_ID, //
      TSID_MIN_INCLUSIVE, avValobj( MIN_PSX_DATE_TIME ), //
      TSID_MAX_INCLUSIVE, avValobj( MAX_PSX_DATE_TIME ), //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, avValobj( LocalDateTime.now() ) //
  );

}
