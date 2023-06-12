package com.hazard157.psx.proj3.pleps.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.quants.visumple.*;
import com.hazard157.psx.proj3.pleps.*;

/**
 * Реализация {@link IStir}.
 *
 * @author hazard157
 */
class Stir
    implements IStir {

  /**
   * Экземпляр-синглтон хранителя объектов этого класса, но не наследников.
   */
  static final IEntityKeeper<IStir> KEEPER =
      new AbstractEntityKeeper<>( IStir.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aDw, IStir aEntity ) {
          OptionSetKeeper.KEEPER_INDENTED.write( aDw, aEntity.params() );
        }

        @Override
        protected IStir doRead( IStrioReader aDr ) {
          IOptionSet ops = OptionSetKeeper.KEEPER.read( aDr );
          Stir s = new Stir();
          s.params().addAll( ops );
          return s;
        }

      };

  private final ITsCollectionChangeListener paramsChangeListener =
      ( aSource, aOp, aItem ) -> eventer().fireChangeEvent();

  private final GenericChangeEventer   eventer;
  private final INotifierOptionSetEdit params = new NotifierOptionSetEditWrapper( new OptionSet() );

  private IPlep plep = null;

  /**
   * Конструктор.
   */
  Stir() {
    eventer = new GenericChangeEventer( this );
    params.addCollectionChangeListener( paramsChangeListener );
  }

  protected GenericChangeEventer eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  void setPlep( IPlep aPlep ) {
    plep = aPlep;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public INotifierOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IVisumplable
  //

  @Override
  public IVisumplesList visumples() {
    // return IVisumpleConstants.OPDEF_VISUMPLES.getValue( params ).asValobj();
    IAtomicValue av =
        params.getValue( IVisumpleConstants.FID_VISUMPLES, avStr( IEntityKeeper.STR_EMPTY_COLLECTION_REPRESENTATION ) );
    return new VisumplesList( VisumpleKeeper.KEEPER.str2coll( av.asString() ) );
  }

  // ------------------------------------------------------------------------------------
  // IVisumplableEdit
  //

  @Override
  public void setVisumples( IVisumplesList aVisumples ) {
    IVisumpleConstants.OPDEF_VISUMPLES.setValue( params, AvUtils.avValobj( aVisumples ) );
  }

  // ------------------------------------------------------------------------------------
  // IStir
  //

  @Override
  public IPlep plep() {
    TsIllegalStateRtException.checkNull( plep );
    return plep;
  }

  @Override
  public Secint getIntervalInPlep() {
    int start = 0;
    for( IStir s : plep().stirs() ) {
      if( s == this ) {
        return new Secint( start, start + duration() - 1 );
      }
      start += s.duration();
    }
    throw new TsInternalErrorRtException();
  }

}
