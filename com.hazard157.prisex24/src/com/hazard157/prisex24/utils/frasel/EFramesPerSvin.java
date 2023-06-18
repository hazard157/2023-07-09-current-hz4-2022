package com.hazard157.prisex24.utils.frasel;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.utils.frasel.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * How many frames will be selected per SVIN.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EFramesPerSvin
    implements IStridable, IIconIdable {

  SELECTED( "selected", STR_FRPSP_SELECTED, STR_FRPSP_SELECTED_D, ICONID_FRAMES_PER_SVIN_SELECTED ), //$NON-NLS-1$

  ONE_NO_MORE( "one_no_more", STR_FRPSP_ONE_NO_MORE, STR_FRPSP_ONE_NO_MORE_D, ICONID_FRAMES_PER_SVIN_ONE_NO_MORE ), //$NON-NLS-1$

  FORCE_ONE( "force_one", STR_FRPSP_FORCE_ONE, STR_FRPSP_FORCE_ONE_D, ICONID_FRAMES_PER_SVIN_FORCE_ONE ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EFramePerSpan"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EFramesPerSvin> KEEPER = new StridableEnumKeeper<>( EFramesPerSvin.class );

  private static IStridablesListEdit<EFramesPerSvin> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final String iconId;

  EFramesPerSvin( String aId, String aName, String aDescription, String aIconId ) {
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
  // IIconIdable
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EFramesPerSvin} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EFramesPerSvin> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EFramesPerSvin} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EFramesPerSvin getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EFramesPerSvin} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EFramesPerSvin findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EFramesPerSvin item : values() ) {
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
   * @return {@link EFramesPerSvin} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EFramesPerSvin getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
