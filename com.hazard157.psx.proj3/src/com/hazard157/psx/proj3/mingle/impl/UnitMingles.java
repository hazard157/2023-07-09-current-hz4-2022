package com.hazard157.psx.proj3.mingle.impl;

import static com.hazard157.psx.proj3.mingle.IMingleConstants.*;
import static com.hazard157.psx.proj3.mingle.impl.IHzResources.*;

import java.time.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx.proj3.incident.*;
import com.hazard157.psx.proj3.incident.when.*;
import com.hazard157.psx.proj3.mingle.*;

/**
 * {@link IUnitMingles} implementation.
 *
 * @author hazard157
 */
public class UnitMingles
    extends StriparManager<IMingle>
    implements IUnitMingles {

  private final IStriparManagerValidator mingleValidator = new IStriparManagerValidator() {

    private ValidationResult internalValidate( String aId, IOptionSet aInfo ) {
      LocalDate date = OPDEF_DATE.getValue( aInfo ).asValobj();
      ValidationResult vr = IncidentDateValidator.VALIDATOR.validate( date );
      if( vr.isError() ) {
        return vr;
      }
      vr = ValidationResult.firstNonOk( vr, EPsxIncidentKind.MINGLE.idStrValidator().validate( aId ) );
      if( vr.isError() ) {
        return vr;
      }
      // check ID and DATE matches
      LocalDate dateFromId = MingleUtils.mingleId2LocalDate( aId );
      if( !date.equals( dateFromId ) ) {
        return ValidationResult.error( FMT_ERR_DATE_AND_ID_MISMATCH, date.toString(), dateFromId.toString() );
      }
      return vr;
    }

    @Override
    public ValidationResult canCreateItem( String aId, IOptionSet aInfo ) {
      return internalValidate( aId, aInfo );
    }

    @Override
    public ValidationResult canEditItem( String aOldId, String aId, IOptionSet aInfo ) {
      return internalValidate( aId, aInfo );
    }

    @Override
    public ValidationResult canRemoveItem( String aId ) {
      return ValidationResult.SUCCESS;
    }

  };

  /**
   * Constructor.
   */
  public UnitMingles() {
    super( Mingle.CREATOR, true );
    svs().addValidator( mingleValidator );
  }

}
