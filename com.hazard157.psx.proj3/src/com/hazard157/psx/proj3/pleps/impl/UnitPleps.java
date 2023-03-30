package com.hazard157.psx.proj3.pleps.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Реализация {@link IUnitPleps}.
 *
 * @author hazard157
 */
public class UnitPleps
    extends AbstractSinentManager<IPlep, PlepInfo>
    implements IUnitPleps {

  private final IEntityKeeper<IPlep> PLEP_KEEPER =
      new AbstractEntityKeeper<>( IPlep.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aDw, IPlep aEntity ) {
          PlepKeeper.KEEPER.write( aDw, aEntity );
        }

        @Override
        protected IPlep doRead( IStrioReader aDr ) {
          Plep p = (Plep)PlepKeeper.KEEPER.read( aDr );
          p.setOwner( UnitPleps.this );
          return p;
        }

      };

  /**
   * Конструктор.
   */
  public UnitPleps() {
    super( TsLibUtils.EMPTY_STRING, null );
    setSinentKeeper( PLEP_KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractSinentManager
  //

  @Override
  protected IPlep doCreateItem( String aId, PlepInfo aInfo ) {
    Plep p = new Plep( aId, aInfo );
    p.setOwner( this );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IUnitPleps
  //

  // nop

}
