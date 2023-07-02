package com.hazard157.psx.proj3.incident;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.time.*;

import org.toxsoft.core.tslib.av.*;

/**
 * Constants of common rules for all PRISEX application entities.
 * <p>
 * These rules does not depends either on PRISEX project file format or PRISEX application version.
 *
 * @author hazard157
 */
public interface IPrisexIncidentConstants {

  /**
   * Syntactically valid but non-existing incident ID.
   */
  String GENERIC_INCIDENT_ID = "u1995-01-01"; //$NON-NLS-1$

  /**
   * The prefix character of the episode ID.
   */
  char EPISODE_ID_PREFIX = 'e';

  /**
   * The prefix character of the gaze ID.
   */
  char GAZE_ID_PREFIX = 'g';

  /**
   * The prefix character of the misc incident ID.
   */
  char MINGLE_ID_PREFIX = 'm';

  /**
   * The prefix character of the incident ID of an unknown kind.
   */
  char UNKNOWN_ID_PREFIX = 'u';

  /**
   * ID of non-existing episode is syntactically valid.
   */
  String EPISODE_ID_NONE = EPISODE_ID_PREFIX + "1995_01_01"; //$NON-NLS-1$

  /**
   * ID of non-existing gaze is syntactically valid.
   */
  String GAZE_ID_NONE = GAZE_ID_PREFIX + "1995_01_01"; //$NON-NLS-1$

  /**
   * ID of non-existing mingle incident is syntactically valid.
   */
  String MINGLE_ID_NONE = MINGLE_ID_PREFIX + "1995_01_01"; //$NON-NLS-1$

  /**
   * ID of non-existing unknown incident is syntactically valid.
   */
  String UNKNOWN_ID_NONE = UNKNOWN_ID_PREFIX + "1995_01_01"; //$NON-NLS-1$

  /**
   * Atomic value representation of {@link #EPISODE_ID_NONE_AV}.
   */
  IAtomicValue EPISODE_ID_NONE_AV = avStr( EPISODE_ID_NONE );

  /**
   * Atomic value representation of {@link #GAZE_ID_NONE_AV}.
   */
  IAtomicValue GAZE_ID_NONE_AV = avStr( GAZE_ID_NONE );

  /**
   * Atomic value representation of {@link #MINGLE_ID_NONE_AV}.
   */
  IAtomicValue MINGLE_ID_NONE_AV = avStr( MINGLE_ID_NONE );

  /**
   * Default valid value for timestamps when crating new incidents.
   */
  long DEFAULT_NEW_INCIDENT_TIMESTAMP = System.currentTimeMillis();

  /**
   * Value of {@link #DEFAULT_NEW_INCIDENT_TIMESTAMP} as {@link Instant}.
   */
  Instant DEFAULT_NEW_INCIDENT_INSTANT = Instant.ofEpochMilli( DEFAULT_NEW_INCIDENT_TIMESTAMP );

  /**
   * Value of {@link #DEFAULT_NEW_INCIDENT_TIMESTAMP} as {@link LocalDate}.
   */
  LocalDate DEFAULT_NEW_INCIDENT_DATE = LocalDate.ofInstant( DEFAULT_NEW_INCIDENT_INSTANT, ZoneId.systemDefault() );

}
