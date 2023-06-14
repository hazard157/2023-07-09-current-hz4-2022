package com.hazard157.prisex24.m5.tags;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Group {@link ITag} as natural tree.
 *
 * @author hazard157
 */
class TreeMakerByGroups
    implements ITsTreeMaker<ITag> {

  static final ITsNodeKind<ITag> NK_NODE = new TsNodeKind<>( "node", //$NON-NLS-1$
      ITag.class, true );

  static final ITsNodeKind<ITag> NK_LEAF = new TsNodeKind<>( "leaf", //$NON-NLS-1$
      ITag.class, false );

  private final IRootTag rootTag;

  private final IStringMapEdit<DefaultTsNode<ITag>> allNodes = new StringMap<>();

  TreeMakerByGroups( IRootTag aRootTag ) {
    TsNullArgumentRtException.checkNull( aRootTag );
    rootTag = aRootTag;
  }

  DefaultTsNode<ITag> ensureTagNode( ITag aTag, ITsNode aRootNode ) {
    // узел уже есть, выходим
    DefaultTsNode<ITag> node = allNodes.findByKey( aTag.id() );
    if( node != null ) {
      return node;
    }
    // определим, узел или лист (с точки зрения свойств ярлыка!, а не наличия дочерных узлов)
    boolean canHaveChilds = !ITagsConstants.IS_LEAF.getValue( aTag.params() ).asBool();
    ITsNodeKind<ITag> kind = canHaveChilds ? NK_NODE : NK_LEAF;
    // узлы ярлыков высшего уровня создаются особым образом
    if( aTag.parent().isRoot() ) {
      node = new DefaultTsNode<>( kind, aRootNode, aTag );
      allNodes.put( aTag.id(), node );
      return node;
    }
    // создаем все остальные узлы
    DefaultTsNode<ITag> parent = ensureTagNode( aTag.parent(), aRootNode );
    node = new DefaultTsNode<>( kind, parent, aTag );
    parent.addNode( node );
    allNodes.put( aTag.id(), node );
    return node;
  }

  IList<ITsNode> makeRootNodes() {
    IListEdit<ITsNode> rootNodes = new ElemArrayList<>();
    for( ITag t : rootTag.childNodes() ) {
      DefaultTsNode<ITag> node = allNodes.findByKey( t.id() );
      if( node != null ) {
        rootNodes.add( node );
      }
    }
    return rootNodes;
  }

  // ------------------------------------------------------------------------------------
  // ITsTreeMaker
  //

  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ITag> aItems ) {
    allNodes.clear();
    for( ITag t : aItems ) {
      TsInternalErrorRtException.checkTrue( t.isRoot() );
      ensureTagNode( t, aRootNode );
    }
    return makeRootNodes();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_LEAF || aNode.kind() == NK_NODE;
  }

}
