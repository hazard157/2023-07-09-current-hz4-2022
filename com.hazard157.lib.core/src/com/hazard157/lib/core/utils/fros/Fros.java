package com.hazard157.lib.core.utils.fros;

import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;

/**
 * Position in media stream counted in seconds/frames.
 * <p>
 * FROS = FRames pOSition.
 * <p>
 * Fros is counted as <b><code>double</code></b> non-negative value in secons. Fraction part may be interpreted as frame
 * number in last second.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public final class Fros {

  /**
   * Maximal value of allowd FPS.
   * <p>
   * Max value is determined for correct two-digit frame-in-second display.
   */
  public static final int MAX_FPS = 99;

  /**
   * Maximum value of FROS.
   */
  public static final double MAX_FROS = (Integer.MAX_VALUE - 1) / MAX_FPS;

  /**
   * Max value correctly formatted as HH:MM:SS (up to "99:59:59").
   */
  public static final int MAX_HHMMSS_VALUE = 99 * 3600 + 59 * 60 + 59;

  /**
   * Max value correctly formatted as MM:SS (up to "99:59").
   */
  public static final int MAX_MMSS_VALUE = 99 * 3600 + 59 * 60 + 59;

  private final double fros;

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Constructor.
   *
   * @param aFros double - frames postion in stream
   * @throws TsIllegalArgumentRtException argument is not a number
   * @throws TsIllegalArgumentRtException argument is negative number
   * @throws TsIllegalArgumentRtException fros is greater than {@link #MAX_FROS}
   */
  public Fros( double aFros ) {
    internaclCheckFros( aFros );
    fros = aFros;
  }

  public static Fros ofFros( double aFros ) {
    return new Fros( aFros );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  @SuppressWarnings( "boxing" )
  private static String internalHhmmssff( double aFros, int aFps ) {
    int secs = (int)aFros;
    int h = secs / 3600;
    secs -= h * 3600;
    int m = secs / 60;
    int s = secs - m * 60;
    int fris = (int)((aFros - secs) * aFps);
    return String.format( "%02d:%02d:%02d.%02d", h, m, s, fris ); //$NON-NLS-1$
  }

  @SuppressWarnings( "boxing" )
  private static String internalMmssff( double aFros, int aFps ) {
    int secs = (int)aFros;
    int m = secs / 60;
    int s = secs - m * 60;
    int fris = (int)((aFros - secs) * aFps);
    return String.format( "%02d:%02d.%02d", m, s, fris ); //$NON-NLS-1$
  }

  private static double internaclCheckFros( double aFros ) {
    TsIllegalArgumentRtException.checkFalse( Double.isFinite( aFros ) );
    TsIllegalArgumentRtException.checkTrue( aFros < 0 );
    TsIllegalArgumentRtException.checkTrue( aFros > MAX_FROS );
    return aFros;
  }

  private static int internaclCheckFps( int aFps ) {
    TsIllegalArgumentRtException.checkTrue( aFps <= 0 );
    TsIllegalArgumentRtException.checkTrue( aFps > MAX_FPS );
    return aFps;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public static Fros ofFrameNo( long aFrameNo, int aFps ) {
    TsIllegalArgumentRtException.checkTrue( aFrameNo < 0 );
    internaclCheckFps( aFps );
    double fros = aFrameNo / ((double)aFps);
    return new Fros( fros );
  }

  public int sec() {
    return (int)fros;
  }

  public int fris( int aFps ) {
    internaclCheckFps( aFps );
    int secs = (int)fros;
    double fraction = fros - secs;
    int fris = (int)(fraction * aFps);
    return fris;
  }

  public int frameNo( int aFps ) {
    internaclCheckFps( aFps );
    int frameNo = (int)(fros * aFps);
    return frameNo;
  }

  public boolean isInHhmmssRange() {
    return fros < (MAX_HHMMSS_VALUE + 1);
  }

  public String hhmmssff( int aFps ) {
    internaclCheckFps( aFps );
    return internalHhmmssff( fros, aFps );
  }

  public boolean isInMmssRange() {
    return fros < (MAX_MMSS_VALUE + 1);
  }

  public String mmssff( int aFps ) {
    internaclCheckFps( aFps );
    return internalMmssff( fros, aFps );
  }

  // ------------------------------------------------------------------------------------
  // Static API
  //

  public static String hhmmssff( double aFros, int aFps ) {
    internaclCheckFros( aFros );
    internaclCheckFps( aFps );
    return internalHhmmssff( aFros, aFps );
  }

  public static String mmssff( double aFros, int aFps ) {
    internaclCheckFros( aFros );
    internaclCheckFps( aFps );
    return internalMmssff( aFros, aFps );
  }

}
