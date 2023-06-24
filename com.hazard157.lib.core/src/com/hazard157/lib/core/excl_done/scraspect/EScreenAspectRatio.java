package com.hazard157.lib.core.excl_done.scraspect;

import static com.hazard157.lib.core.excl_done.scraspect.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Commonly used screen aspect ratios.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EScreenAspectRatio
    implements IStridable {

  AR_4_3( "ar4_3", 4, 3 ), //$NON-NLS-1$

  AR_16_9( "ar16_9", 16, 9 ), //$NON-NLS-1$

  AR_21_9( "ar21_9", 21, 9 ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EScreenAspectRatio"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EScreenAspectRatio> KEEPER = new StridableEnumKeeper<>( EScreenAspectRatio.class );

  private static IStridablesListEdit<EScreenAspectRatio> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    wMul;
  private final int    hDiv;
  private final double ratio;

  @SuppressWarnings( "boxing" )
  EScreenAspectRatio( String aId, int aWMul, int aHDiv ) {
    id = aId;
    wMul = aWMul;
    hDiv = aHDiv;
    ratio = wMul / hDiv;
    name = String.format( FMT_N_SCREEN_ASPECT_RATIO, wMul, hDiv, ratio );
    description = String.format( FMT_D_SCREEN_ASPECT_RATIO, wMul, hDiv, ratio );
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns aspect ratio numerator.
   *
   * @return int - the numerator
   */
  public int wMul() {
    return wMul;
  }

  /**
   * Returns aspect ratio denominator.
   *
   * @return int - the denominator
   */
  public int hDiv() {
    return hDiv;
  }

  /**
   * Returns aspect ratio.
   *
   * @return double - aspect ratio
   */
  public double ratio() {
    return ratio;
  }

  /**
   * Finds constant with {@link EScreenAspectRatio#ratio()} closest to the specified aspect ratio.
   *
   * @param aRatio double - the aspect ratio
   * @return {@link EScreenAspectRatio} - constant with closest aspect ratio
   */
  public static EScreenAspectRatio findNearest( double aRatio ) {
    EScreenAspectRatio found = asList().first();
    double delta = Double.MAX_VALUE;
    for( EScreenAspectRatio sar : asList() ) {
      double d = Math.abs( aRatio - sar.ratio );
      if( d < delta ) {
        delta = d;
        found = sar;
      }
    }
    return found;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EScreenAspectRatio} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EScreenAspectRatio> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EScreenAspectRatio} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EScreenAspectRatio getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EScreenAspectRatio} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EScreenAspectRatio findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EScreenAspectRatio item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EScreenAspectRatio} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EScreenAspectRatio getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
