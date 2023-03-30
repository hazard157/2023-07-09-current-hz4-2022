package com.hazard157.psx24.core.m5.episode;

import static com.hazard157.psx24.core.m5.episode.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx.proj3.episodes.*;

class TreeMakerEpisodeByPlace
    implements ITsTreeMaker<IEpisode> {

  static final ITsNodeKind<String>   NK_PLACE   = new TsNodeKind<>( "Place", String.class, true );       //$NON-NLS-1$
  static final ITsNodeKind<IEpisode> NK_EPISODE = new TsNodeKind<>( "IEpisode", IEpisode.class, false ); //$NON-NLS-1$

  TreeMakerEpisodeByPlace() {
    // nop
  }

  private static int getPlaceCount( String aPlace, IList<IEpisode> aItems ) {
    int count = 0;
    for( IEpisode e : aItems ) {
      String s = e.info().place();
      if( s.equals( aPlace ) ) {
        ++count;
      }
    }
    return count;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IEpisode> aItems ) {
    IStringMapEdit<DefaultTsNode<String>> plMap = new StringMap<>();
    // сортированная карта мест
    IStringListBasicEdit places = new SortedStringLinkedBundleList();
    for( IEpisode e : aItems ) {
      String s = e.info().place();
      if( getPlaceCount( s, aItems ) > 1 ) {
        if( !places.hasElem( s ) ) {
          places.add( s );
          plMap.put( s, new DefaultTsNode<>( NK_PLACE, aRootNode, s ) );
        }
      }
    }
    plMap.put( STR_OTHER_PLACES, new DefaultTsNode<>( NK_PLACE, aRootNode, STR_OTHER_PLACES ) );
    // распределим эпизоды
    for( IEpisode e : aItems ) {
      String s = e.info().place();
      DefaultTsNode<String> parent = plMap.findByKey( s );
      if( parent == null ) {
        parent = plMap.getByKey( STR_OTHER_PLACES );
      }
      DefaultTsNode<IEpisode> node = new DefaultTsNode<>( NK_EPISODE, parent, e );
      parent.addNode( node );
    }
    for( DefaultTsNode<?> n : plMap ) {
      n.setName( String.format( "%s (%d)", n.name(), Integer.valueOf( n.childs().size() ) ) ); //$NON-NLS-1$
    }
    return (IList)plMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_EPISODE;
  }

}
