package com.hazard157.lib.core.quants.zodsign;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.quants.zodsign.IHzResources.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.*;

/**
 * Zodiac sign.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EZodiacSign
    implements IRadioPropEnum {

  UNKNOWN( UNKNOWN_ID, "⨀ " + STR_N_UNKNOWN, STR_D_UNKNOWN, ICONID_UNKNOWN_ICON_ID ), //$NON-NLS-1$

  ARIES( "aries", "♈ " + STR_N_ARIES, STR_D_ARIES, ICON_ZS_ARIES ), //$NON-NLS-1$ //$NON-NLS-2$

  TAURUS( "taurus", "♉ " + STR_N_TAURUS, STR_D_TAURUS, ICON_ZS_TAURUS ), //$NON-NLS-1$ //$NON-NLS-2$

  GEMINI( "gemini", "♊ " + STR_N_GEMINI, STR_D_GEMINI, ICON_ZS_GEMINI ), //$NON-NLS-1$ //$NON-NLS-2$

  CANCER( "cancer", "♋ " + STR_N_CANCER, STR_D_CANCER, ICON_ZS_CANCER ), //$NON-NLS-1$ //$NON-NLS-2$

  LEO( "leo", "♌ " + STR_N_LEO, STR_D_LEO, ICON_ZS_LEO ), //$NON-NLS-1$ //$NON-NLS-2$

  VIRGO( "virgo", "♍ " + STR_N_VIRGO, STR_D_VIRGO, ICON_ZS_VIRGO ), //$NON-NLS-1$ //$NON-NLS-2$

  LIBRA( "libra", "♎ " + STR_N_LIBRA, STR_D_LIBRA, ICON_ZS_LIBRA ), //$NON-NLS-1$ //$NON-NLS-2$

  SCORPIO( "scorpio", "♏ " + STR_N_SCORPIO, STR_D_SCORPIO, ICON_ZS_SCORPIO ), //$NON-NLS-1$ //$NON-NLS-2$

  SAGITTARIUS( "sagittarius", "♐ " + STR_N_SAGITTARIUS, STR_D_SAGITTARIUS, ICON_ZS_SAGITTARIUS ), //$NON-NLS-1$ //$NON-NLS-2$

  CAPRICORN( "capricorn", "♑ " + STR_N_CAPRICORN, STR_D_CAPRICORN, ICON_ZS_CAPRICORN ), //$NON-NLS-1$ //$NON-NLS-2$

  AQUARIUS( "aquarius", "♒ " + STR_N_AQUARIUS, STR_D_AQUARIUS, ICON_ZS_AQUARIUS ), //$NON-NLS-1$ //$NON-NLS-2$

  PISCES( "pisces", "♓ " + STR_N_PISCES, STR_D_PISCES, ICON_ZS_PISCES ), //$NON-NLS-1$ //$NON-NLS-2$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EZodiacSign"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EZodiacSign> KEEPER = new StridableEnumKeeper<>( EZodiacSign.class );

  private static IStridablesListEdit<EZodiacSign> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final String iconId;

  EZodiacSign( String aId, String aName, String aDescription, String aIconId ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
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

  // ------------------------------------------------------------------------------------
  // IRadioPropEnum
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EZodiacSign} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EZodiacSign> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EZodiacSign} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EZodiacSign getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EZodiacSign} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EZodiacSign findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EZodiacSign item : values() ) {
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
   * @return {@link EZodiacSign} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EZodiacSign getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
