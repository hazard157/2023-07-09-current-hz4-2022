package com.hazard157.lib.core.excl_done.flags;

/**
 * Bits set used as flags.
 * <p>
 * This is read-only interface, flags may be edited by implementing class {@link Flags}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IFlags {

  int bits();

  void setBits( int aBits );

  default boolean hasBit( int aBits ) {
    return (bits() & aBits) != 0;
  }

  default boolean hasAllBits( int aBits ) {
    return (bits() & aBits) == 0;
  }

  default void resetBits( int aBits ) {
    setBits( aBits & (~aBits) );
  }

}
