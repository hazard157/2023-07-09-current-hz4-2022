package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Маркер заметками типа {@link String},
 *
 * @author hazard157
 */
public final class MarkNote
    extends Mark<String>
    implements Comparable<MarkNote> {

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aIn {@link Secint} - интервал времени
   * @param aMarker {@link String} - маркер на интервале
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public MarkNote( Secint aIn, String aMarker ) {
    super( aIn, aMarker );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( MarkNote aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return this.in().compareTo( aThat.in() );
  }

}
