package com.hazard157.prisex24.m5.snippet;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.prisex24.pdus.snippets.*;

/**
 * Group {@link ISnippet} by {@link ISnippet#category()}.
 *
 * @author hazard157
 */
class TreeMakerByCategory
    implements ITsTreeMaker<ISnippet> {

  static final ITsNodeKind<String>   NK_CATEG   = new TsNodeKind<>( "Categ", String.class, true );      //$NON-NLS-1$
  static final ITsNodeKind<ISnippet> NK_SNIPPET = new TsNodeKind<>( "Snippet", ISnippet.class, false ); //$NON-NLS-1$

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISnippet> aItems ) {
    IStringMapEdit<DefaultTsNode<String>> rootNodes = new SortedStringMap<>();
    for( ISnippet s : aItems ) {
      DefaultTsNode<String> groupNode = rootNodes.findByKey( s.category() );
      if( groupNode == null ) {
        groupNode = new DefaultTsNode<>( NK_CATEG, aRootNode, s.category() );
        groupNode.setName( s.category() );
        rootNodes.put( s.category(), groupNode );
      }
      DefaultTsNode<ISnippet> node = new DefaultTsNode<>( NK_SNIPPET, groupNode, s );
      node.setName( s.nmName() );
      groupNode.addNode( node );
    }
    return (IList)rootNodes.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_SNIPPET;
  }

}
