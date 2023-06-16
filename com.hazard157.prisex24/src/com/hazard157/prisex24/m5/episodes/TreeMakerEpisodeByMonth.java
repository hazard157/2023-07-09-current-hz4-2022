package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.episodes.TreeMakerEpisodeByPlace.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.proj3.episodes.*;

class TreeMakerEpisodeByMonth
    implements ITsTreeMaker<IEpisode> {

  static final ITsNodeKind<Month> NK_MONTH = new TsNodeKind<>( "Month", Month.class, true ); //$NON-NLS-1$

  TreeMakerEpisodeByMonth() {
    // nop
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IEpisode> aItems ) {
    IMapEdit<Month, DefaultTsNode<Month>> monMap = new ElemMap<>();
    for( Month m : Month.values() ) {
      DefaultTsNode<Month> n = new DefaultTsNode<>( NK_MONTH, aRootNode, m );
      monMap.put( m, n );
      n.setName( m.getDisplayName( TextStyle.FULL_STANDALONE, Locale.getDefault() ) );
    }
    // распределим эпизоды
    for( IEpisode e : aItems ) {
      DefaultTsNode<Month> parent = monMap.findByKey( e.date().getMonth() );
      DefaultTsNode<IEpisode> node = new DefaultTsNode<>( NK_EPISODE, parent, e );
      parent.addNode( node );
    }
    // укажем количествоэлементов в узле
    for( DefaultTsNode<?> n : monMap ) {
      n.setName( String.format( "%s (%d)", n.name(), Integer.valueOf( n.childs().size() ) ) ); //$NON-NLS-1$
    }
    return (IList)monMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_EPISODE;
  }

}
