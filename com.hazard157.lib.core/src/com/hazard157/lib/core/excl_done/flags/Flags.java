package com.hazard157.lib.core.excl_done.flags;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Bits set used as flags.
 *
 * @author hazard157
 */
public final class Flags
    implements IFlags {

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "Flags"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   * <p>
   * Read instance of {@link IFlags} may be safely cast to {@link Flags}.
   */
  public static final IEntityKeeper<IFlags> KEEPER =
      new AbstractEntityKeeper<>( IFlags.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IFlags aEntity ) {
          aSw.writeIntHex( aEntity.bits() );
        }

        @Override
        protected IFlags doRead( IStrioReader aSr ) {
          return new Flags( aSr.readInt() );
        }
      };

  private int bits = 0;

  /**
   * Constructor.
   * <p>
   * Created with all bits reset, that is {@link #bits()} = 0.
   */
  public Flags() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aBits int - intial value of {@link #bits()}
   */
  public Flags( int aBits ) {
    bits = aBits;
  }

  // ------------------------------------------------------------------------------------
  // IFlags
  //

  @Override
  public int bits() {
    return bits;
  }

  @Override
  public void setBits( int aBits ) {
    bits = aBits;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "0x%X", Integer.valueOf( bits ) ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    // equality check will be performed on IAtomicValue because tslib does not creates IAtomicValue instances
    if( aThat instanceof Flags that ) {
      return this.bits == that.bits;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + bits;
    return result;
  }

}
