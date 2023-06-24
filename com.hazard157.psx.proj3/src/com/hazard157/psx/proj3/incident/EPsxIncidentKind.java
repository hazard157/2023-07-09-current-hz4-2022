package com.hazard157.psx.proj3.incident;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx.proj3.incident.IPrisexIncidentConstants.*;
import static com.hazard157.psx.proj3.incident.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.time.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.utils.*;
import com.hazard157.psx.proj3.incident.when.*;

/**
 * The enumeration of XXX.
 *
 * @author hazard157
 */
public enum EPsxIncidentKind
    implements IRadioPropEnum {

  /**
   * Unknown kind - either yet unsorted or indeterminable in the usage context.
   */
  UNKNOWN( UNKNOWN_ID, STR_INC_UNKNOWN, STR_INC_UNKNOWN_D, UNKNOWN_ID_PREFIX, UNKNOWN_ID_NONE, //
      ICONID_INCIDENT_KIND_UNKNOWN ),

  /**
   * The episode - sexual intercourse filmed.
   */
  EPISODE( "episode", STR_INC_EPISODE, STR_INC_EPISODE_D, EPISODE_ID_PREFIX, EPISODE_ID_NONE, //$NON-NLS-1$
      ICONID_INCIDENT_KIND_EPISODE ),

  /**
   * The gaze - nude she was caught and shot on camera.
   */
  GAZE( "gaze", STR_INC_GAZE, STR_INC_GAZE_D, GAZE_ID_PREFIX, GAZE_ID_NONE, //$NON-NLS-1$
      ICONID_INCIDENT_KIND_GAZE ),

  /**
   * Miscellaneous and mingle incidents.
   */
  MINGLE( "mingle", STR_INC_MINGLE, STR_INC_MINGLE_D, MINGLE_ID_PREFIX, MINGLE_ID_NONE, //$NON-NLS-1$
      ICONID_INCIDENT_KIND_MINGLE ),;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EPsxIncidentKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EPsxIncidentKind> KEEPER = new StridableEnumKeeper<>( EPsxIncidentKind.class );

  private static IStridablesListEdit<EPsxIncidentKind> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final String iconId;
  private final char   chIdPrefix;

  private final ITsValidator<String>       validatorIdStr;
  private final ITsValidator<IAtomicValue> validatorIdAv;

  private final String       noneIdStr;
  private final IAtomicValue noneIdAv;

  EPsxIncidentKind( String aId, String aName, String aDescription, char aIdPrefixChar, String aNoneId,
      String aIconId ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
    chIdPrefix = aIdPrefixChar;
    validatorIdStr = new IncidentIdValidator( chIdPrefix, false );
    validatorIdAv = new IncidentIdAvValidator( validatorIdStr );
    noneIdStr = aNoneId;
    noneIdAv = avStr( noneIdStr );
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

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Extracts date from the incident ID string.
   * <p>
   * Assumes valid incident ID as input.
   *
   * @param aIncidentId String - incident ID in form "xYYYY-DD-MM"
   * @return {@link LocalDate} - extracted date
   */
  public static LocalDate internalIncidentId2date( String aIncidentId ) {
    int year = Integer.parseInt( aIncidentId.substring( 1, 5 ) );
    int month = Integer.parseInt( aIncidentId.substring( 6, 8 ) );
    int day = Integer.parseInt( aIncidentId.substring( 9 ) );
    return LocalDate.of( year, month, day );
  }

  /**
   * Returns ID validator for this kind of incidents.
   *
   * @return {@link ITsValidator}&gt;{@link String}&lt; - the String ID validator
   */
  public ITsValidator<String> idStrValidator() {
    return validatorIdStr;
  }

  /**
   * Returns ID validator for this kind of incidents.
   *
   * @return {@link ITsValidator}&gt;{@link IAtomicValue}&lt; - the {@link IAtomicValue} ID validator
   */
  public ITsValidator<IAtomicValue> idAvValidator() {
    return validatorIdAv;
  }

  /**
   * Returns syntactically valid non-existing incident ID.
   *
   * @return String - the ID of the non-existing incident of this kind
   */
  public String noneId() {
    return noneIdStr;
  }

  /**
   * Returns syntactically valid non-existing incident ID as {@link IAtomicValue}.
   *
   * @return {@link IAtomicValue} - the ID of the non-existing incident of this kind
   */
  public IAtomicValue noneIdAv() {
    return noneIdAv;
  }

  /**
   * Extracts date from the incident ID.
   *
   * @param aIncidentId String - the incident ID
   * @return {@link LocalDate} - the date
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an this incident ID
   */
  public LocalDate id2date( String aIncidentId ) {
    validatorIdStr.checkValid( aIncidentId );
    return internalIncidentId2date( aIncidentId );
  }

  /**
   * Converts incident date to the incident ID.
   *
   * @param aDate {@link LocalDate} - the date
   * @return String - incident ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException date is out of range
   */
  public String date2id( LocalDate aDate ) {
    IncidentDateValidator.VALIDATOR.checkValid( aDate );
    return chIdPrefix + aDate.toString().replace( '-', '_' );
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EPsxIncidentKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EPsxIncidentKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EPsxIncidentKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EPsxIncidentKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EPsxIncidentKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EPsxIncidentKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EPsxIncidentKind item : values() ) {
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
   * @return {@link EPsxIncidentKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EPsxIncidentKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
