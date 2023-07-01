package com.hazard157.psx24.planning.m5;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx.proj3.pleps.*;

class PlepTreeMakerByPlace
    implements ITsTreeMaker<IPlep> {

  public static final ITsNodeKind<String> NK_PLACE = new TsNodeKind<>( "Place", String.class, true ); //$NON-NLS-1$
  public static final ITsNodeKind<IPlep>  NK_PLEP  = new TsNodeKind<>( "Plep", IPlep.class, false );  //$NON-NLS-1$

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IPlep> aItems ) {
    IStringMapEdit<DefaultTsNode<String>> plMap = new StringMap<>();
    for( IPlep p : aItems ) {
      String place = p.info().place();
      DefaultTsNode<String> parent = plMap.findByKey( place );
      if( parent == null ) {
        parent = new DefaultTsNode<>( NK_PLACE, aRootNode, place );
        plMap.put( place, parent );
      }
      DefaultTsNode<IPlep> node = new DefaultTsNode<>( NK_PLEP, parent, p );
      node.setName( p.nmName() );
      parent.addNode( node );
    }
    return (IList)plMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_PLEP;
  }

}
