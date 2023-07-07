package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.episodes.TreeMakerEpisodeByPlace.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.proj3.episodes.*;

class TreeMakerEpisodeByYear
    implements ITsTreeMaker<IEpisode> {

  static final ITsNodeKind<Integer> NK_YEAR = new TsNodeKind<>( "Year", Integer.class, true ); //$NON-NLS-1$

  TreeMakerEpisodeByYear() {
    // nop
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IEpisode> aItems ) {
    IMapEdit<Integer, DefaultTsNode<Integer>> yMap = new SortedElemMap<>();
    for( IEpisode e : aItems ) {
      Integer year = Integer.valueOf( e.incidentDate().getYear() );
      DefaultTsNode<Integer> parent = yMap.findByKey( year );
      if( parent == null ) {
        parent = new DefaultTsNode<>( NK_YEAR, aRootNode, year );
        yMap.put( year, parent );
        // parent.setName( year.toString() );
      }
      DefaultTsNode<IEpisode> node = new DefaultTsNode<>( NK_EPISODE, parent, e );
      parent.addNode( node );
    }
    // specify name of episodes in the root mode
    for( DefaultTsNode<?> n : yMap ) {
      n.setName( String.format( "%s (%d)", n.name(), Integer.valueOf( n.childs().size() ) ) ); //$NON-NLS-1$
    }
    return (IList)yMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_EPISODE;
  }

}
