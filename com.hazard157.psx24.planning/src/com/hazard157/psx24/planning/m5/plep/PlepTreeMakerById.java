package com.hazard157.psx24.planning.m5.plep;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx.proj3.pleps.*;

class PlepTreeMakerById
    implements ITsTreeMaker<IPlep> {

  public static final ITsNodeKind<String> NK_ID_GROUP = new TsNodeKind<>( "IdGroup", String.class, true ); //$NON-NLS-1$
  public static final ITsNodeKind<IPlep>  NK_PLEP     = new TsNodeKind<>( "Plep", IPlep.class, false );    //$NON-NLS-1$

  private DefaultTsNode<String> ensureGroupNode( String aGid, IStringMapEdit<DefaultTsNode<String>> aNodes,
      ITsNode aRootNode, IList<IPlep> aItems ) {
    DefaultTsNode<String> gn = aNodes.findByKey( aGid );
    if( gn == null ) {
      if( !aGid.isEmpty() ) { // add as to the group
        String pid = StridUtils.removeTailingIdNames( aGid, 1 );
        DefaultTsNode<String> parentNode = ensureGroupNode( pid, aNodes, aRootNode, aItems );
        gn = new DefaultTsNode<>( NK_ID_GROUP, parentNode, aGid );
        parentNode.addNode( gn );
        aNodes.put( aGid, gn );
      }
      else { // add as root node
        gn = new DefaultTsNode<>( NK_ID_GROUP, aRootNode, aGid );
      }
    }
    return gn;
  }

  private IStringMap<DefaultTsNode<String>> createIdGroupNodes( ITsNode aRootNode, IList<IPlep> aItems ) {
    IStringMapEdit<DefaultTsNode<String>> nodes = new StringMap<>();
    for( IPlep p : aItems ) {
      String pid = p.id();
      String gid = StridUtils.removeTailingIdNames( pid, 1 );
      ensureGroupNode( gid, nodes, aRootNode, aItems );
    }
    return nodes;
  }

  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IPlep> aItems ) {
    IStringMap<DefaultTsNode<String>> groupNodes = createIdGroupNodes( aRootNode, aItems );
    DefaultTsNode<IPlep> node;
    for( IPlep p : aItems ) {
      String pid = p.id();
      String gid = StridUtils.removeTailingIdNames( pid, 1 );
      if( !gid.isEmpty() ) { // add as to the group
        DefaultTsNode<String> parentNode = groupNodes.getByKey( gid );
        node = new DefaultTsNode<>( NK_PLEP, parentNode, p );
        parentNode.addNode( node );
      }
      else { // add to the root
        node = new DefaultTsNode<>( NK_PLEP, aRootNode, p );
      }
      node.setName( p.nmName() );
    }
    // extract root nodes
    IListEdit<ITsNode> roots = new ElemArrayList<>();
    for( DefaultTsNode<String> n : groupNodes ) {
      if( n.parent() == aRootNode ) {
        roots.add( n );
      }
    }
    return roots;
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_PLEP;
  }

}
