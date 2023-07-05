package com.hazard157.psx.common;

import java.time.*;

/**
 * PRISEX hard constants.
 *
 * @author hazard157
 */
public interface IPsxHardConstants {

  /**
   * Application specific identifiers short prefix.
   */
  String PSX_ID = "psx"; //$NON-NLS-1$

  /**
   * Application specific identifiers full prefix incuding domain name.
   */
  String PSX_FULL_ID = "com.hazard157." + PSX_ID; //$NON-NLS-1$

  /**
   * The number of frames per second of all video materials.
   */
  int FPS = 25;

  /**
   * Minimum allowed episode year.
   */
  int MIN_YEAR = 1995;

  /**
   * The minimum allowed episode date is 1995-01-01.
   */
  long MIN_TIMESTAMP = 788893200000L;

  /**
   * The maximum allowed episode year.
   */
  int MAX_YEAR = 2095;

  /**
   * The maximum allowed episode date is 2095-01-01.
   */
  long MAX_TIMESTAMP = 3944653200000L;

  /**
   * The minimum allowed episode date is 1995-01-01.
   */
  LocalDate MIN_PSX_DATE = LocalDate.of( 1995, Month.JANUARY, 1 );

  /**
   * {@link LocalDateTime} of {@link #MIN_TIMESTAMP}.
   */
  LocalDateTime MIN_PSX_DATE_TIME = LocalDateTime.of( MIN_PSX_DATE, LocalTime.NOON );

  /**
   * The maximum allowed date for an episode is 2095-12-31.
   */
  LocalDate MAX_PSX_DATE = LocalDate.of( 2095, Month.DECEMBER, 31 );

  /**
   * {@link LocalDateTime} of {@link #MAX_PSX_DATE}.
   */
  LocalDateTime MAX_PSX_DATE_TIME = LocalDateTime.of( MAX_PSX_DATE, LocalTime.NOON );

  /**
   * Duration of animated GIF frames in seconds.
   */
  int ANIMATED_GIF_SECS = 5;

  /**
   * An extension (without a dot) of animated GIF images.
   */
  String GIF_FILE_EXT = "gif"; //$NON-NLS-1$

  /**
   * An extension (with a dot) of animated GIF images.
   */
  String GIF_FILE_DOT_EXT = "." + GIF_FILE_EXT; //$NON-NLS-1$

}
