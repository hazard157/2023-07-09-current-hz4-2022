package com.hazard157.psx24.planning.m5;

import static com.hazard157.psx.proj3.pleps.IUnitPlepsConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;

import com.hazard157.lib.core.quants.visumple.*;
import com.hazard157.psx.proj3.pleps.*;

class StirLifecycleManager
    extends M5LifecycleManager<IStir, IPlep> {

  StirLifecycleManager( IM5Model<IStir> aModel, IPlep aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  private static IOptionSet makeStirParams( IM5Bunch<IStir> aValues ) {
    IOptionSetEdit ops = new OptionSet();
    for( IDataDef op : ALL_STIR_OPS ) {
      if( op.id().equals( IVisumpleConstants.FID_VISUMPLES ) ) {
        IList<Visumple> ll = aValues.getAs( op.id(), IList.class );
        ops.setStr( op.id(), VisumpleKeeper.KEEPER.coll2str( ll ) );
      }
      else {
        IAtomicValue av = aValues.getAsAv( op.id() );
        ops.setValue( op, av );
      }
    }
    return ops;
  }

  @Override
  protected IStir doCreate( IM5Bunch<IStir> aValues ) {
    IOptionSet p = makeStirParams( aValues );
    int insertionIndex = aValues.getAsAv( OPID_STIR_INSERTION_INDEX ).asInt();
    if( insertionIndex < -1 || insertionIndex > master().stirs().size() ) {
      insertionIndex = -1;
    }
    return master().newStir( insertionIndex, p );
  }

  @Override
  protected IStir doEdit( IM5Bunch<IStir> aValues ) {
    IOptionSet p = makeStirParams( aValues );
    aValues.originalEntity().params().setAll( p );
    return aValues.originalEntity();
  }

  @Override
  protected void doRemove( IStir aEntity ) {
    int index = master().stirs().indexOf( aEntity );
    if( index >= 0 ) {
      master().removeStir( index );
    }
  }

  @Override
  protected IList<IStir> doListEntities() {
    return master().stirs();
  }

  @Override
  protected IListReorderer<IStir> doGetItemsReorderer() {
    return master().stirsReorderer();
  }

}
