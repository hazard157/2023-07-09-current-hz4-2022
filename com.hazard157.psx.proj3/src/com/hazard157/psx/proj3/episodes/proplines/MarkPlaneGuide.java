package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;

/**
 * Маркер объектами {@link PlaneGuide},
 *
 * @author hazard157
 */
public class MarkPlaneGuide
    extends Mark<PlaneGuide>
    implements Comparable<MarkPlaneGuide> {

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aIn {@link Secint} - интервал времени
   * @param aMarker {@link PlaneGuide} - маркер на интервале
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public MarkPlaneGuide( Secint aIn, PlaneGuide aMarker ) {
    super( aIn, aMarker );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( MarkPlaneGuide aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return this.in().compareTo( aThat.in() );
  }

}
