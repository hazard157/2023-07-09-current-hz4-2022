package com.hazard157.prisex24.m5.plep;

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

  // returns parent node (created or an existing one) or null for aRootNode
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private static DefaultTsNode<?> ensureParentNode( String aPlepId, IStringMap<IPlep> aPlepsMap,
      IStringMapEdit<DefaultTsNode<String>> aGroupNodes, IStringMap<DefaultTsNode<IPlep>> aPlepNodes,
      IListEdit<ITsNode> aRootNodes, ITsNode aRootNode ) {
    String parentId = StridUtils.removeTailingIdNames( aPlepId, 1 );
    // empty parent ID means that this PLEP is aRootNode's child, so return null
    if( parentId.isEmpty() ) {
      return null;
    }
    DefaultTsNode<?> parentNode = aGroupNodes.findByKey( parentId );
    if( parentNode != null ) {
      return parentNode;
    }
    parentNode = aPlepNodes.findByKey( parentId );
    if( parentNode != null ) {
      return parentNode;
    }
    DefaultTsNode<?> papaNode = ensureParentNode( parentId, aPlepsMap, aGroupNodes, aPlepNodes, aRootNodes, aRootNode );
    if( papaNode != null ) {
      parentNode = new DefaultTsNode<>( NK_ID_GROUP, papaNode, parentId );
      papaNode.addNode( parentNode );
    }
    else {
      parentNode = new DefaultTsNode<>( NK_ID_GROUP, aRootNode, parentId );
      aRootNodes.add( parentNode );
    }
    parentNode.setName( parentId );
    aGroupNodes.put( parentId, (DefaultTsNode)parentNode );
    return parentNode;
  }

  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IPlep> aItems ) {
    // list IDs of PLEPs - we need to NOT create dedicated group nodes when PLEPs have scion IDs
    IStringMapEdit<IPlep> plepsMap = new SortedStringMap<>();
    for( IPlep p : aItems ) {
      plepsMap.put( p.id(), p );
    }
    // iterate over PLEPs creating parent nodes as necessary
    IStringMapEdit<DefaultTsNode<String>> groupNodes = new SortedStringMap<>();
    IStringMapEdit<DefaultTsNode<IPlep>> plepNodes = new SortedStringMap<>();
    IListEdit<ITsNode> rootNodes = new ElemArrayList<>();
    for( IPlep p : plepsMap ) {
      DefaultTsNode<IPlep> node;
      DefaultTsNode<?> parentNode = ensureParentNode( p.id(), plepsMap, groupNodes, plepNodes, rootNodes, aRootNode );
      if( parentNode != null ) {
        node = new DefaultTsNode<>( NK_PLEP, parentNode, p );
        parentNode.addNode( node );
      }
      else { // returned null means that parent is aRootNode
        node = new DefaultTsNode<>( NK_PLEP, aRootNode, p );
        rootNodes.add( node );
      }
      node.setName( p.nmName() );
      plepNodes.put( p.id(), node );
    }
    return rootNodes;
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_PLEP;
  }

}
