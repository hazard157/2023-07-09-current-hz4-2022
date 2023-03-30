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
   * Количество кадров в секунду всех видеоматериалов.
   */
  int FPS = 25;

  /**
   * Минимально допустимый год эпизода.
   */
  int MIN_YEAR = 1995;

  /**
   * Минимально допустимая дата эпизода (1995-01-01).
   */
  long MIN_TIMESTAMP = 788893200000L;

  /**
   * Минимально допустимый год эпизода.
   */
  int MAX_YEAR = 2095;

  /**
   * Максимально допустимая дата эпизода (2095-01-01).
   */
  long MAX_TIMESTAMP = 3944653200000L;

  /**
   * Минимально допустимая дата эпизода (1995-01-01).
   */
  LocalDate MIN_PSX_DATE = LocalDate.of( 1995, Month.JANUARY, 1 );

  /**
   * {@link LocalDateTime} of {@link #MIN_TIMESTAMP}.
   */
  LocalDateTime MIN_PSX_DATE_TIME = LocalDateTime.of( MIN_PSX_DATE, LocalTime.NOON );

  /**
   * Максимально допустимая дата эпизода (2095-12-31).
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
   * ! Расширение (без точки) анимированных GIF изображений.
   */
  String ANIMATED_FILE_EXTENSION = "gif"; //$NON-NLS-1$

  /**
   * Расширение (с точкой) анимированных GIF изображений.
   */
  String ANIMATED_FILE_DOT_EXTENSION = "." + ANIMATED_FILE_EXTENSION; //$NON-NLS-1$

}
