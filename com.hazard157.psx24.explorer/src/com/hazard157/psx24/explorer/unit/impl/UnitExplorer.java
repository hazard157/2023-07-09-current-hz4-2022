package com.hazard157.psx24.explorer.unit.impl;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx24.explorer.unit.*;

/**
 * {@link IUnitExplorer} implementation.
 *
 * @author hazard157
 */
public class UnitExplorer
    extends AbstractSinentManager<Inquiry, InquiryInfo>
    implements IUnitExplorer {

  /**
   * Constructor.
   */
  public UnitExplorer() {
    super( TsLibUtils.EMPTY_STRING, InquiryKeeper.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSinentManager
  //

  @Override
  protected Inquiry doCreateItem( String aId, InquiryInfo aInfo ) {
    return new Inquiry( aId, aInfo );
  }

  // ------------------------------------------------------------------------------------
  // IUnitExplorer
  //

}
