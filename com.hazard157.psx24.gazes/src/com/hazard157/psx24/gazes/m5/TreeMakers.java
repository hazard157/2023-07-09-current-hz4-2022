package com.hazard157.psx24.gazes.m5;

import static com.hazard157.psx24.gazes.m5.IPsxResources.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.lib.core.quants.rating.*;
import com.hazard157.psx.proj3.gaze.*;

class TreeMakers {

  static final ITsNodeKind<IGaze> NK_GAZE = new TsNodeKind<>( "Gaze", //$NON-NLS-1$
      IGaze.class, false, null );

  static final ITsNodeKind<ERating> NK_RATING = new TsNodeKind<>( "Rating", //$NON-NLS-1$
      ERating.class, true, null );

  static final ITsNodeKind<Integer> NK_YEAR = new TsNodeKind<>( "Year", //$NON-NLS-1$
      Integer.class, true, null );

  static final ITsNodeKind<Month> NK_MONTH = new TsNodeKind<>( "Month", //$NON-NLS-1$
      Month.class, true, null );

  static final ITsNodeKind<Integer> NK_HH = new TsNodeKind<>( "Hh", //$NON-NLS-1$
      Integer.class, true, null );

  static final TreeModeInfo<IGaze> TMI_BY_RATING = new TreeModeInfo<>( "ByRating", //$NON-NLS-1$
      STR_TMI_BY_RATING, STR_TMI_BY_RATING_D, null, new TmByRating() );

  static final TreeModeInfo<IGaze> TMI_BY_YEAR = new TreeModeInfo<>( "ByYear", //$NON-NLS-1$
      STR_TMI_BY_YEAR, STR_TMI_BY_YEAR_D, null, new TmByYear() );

  static final TreeModeInfo<IGaze> TMI_BY_MONTH = new TreeModeInfo<>( "ByMonth", //$NON-NLS-1$
      STR_TMI_BY_MONTH, STR_TMI_BY_MONTH_D, null, new TmByMonth() );

  // TODO ooh... time of day is NOT supported in project v3
  static final TreeModeInfo<IGaze> TMI_BY_HH = new TreeModeInfo<>( "ByHh", //$NON-NLS-1$
      STR_TMI_BY_HH, STR_TMI_BY_HH_D, null, new TmByHh() );

  static class TmByRating
      implements ITsTreeMaker<IGaze> {

    @SuppressWarnings( { "rawtypes", "unchecked", "boxing" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IGaze> aItems ) {
      // init "rating" - "root node" map
      IMapEdit<ERating, DefaultTsNode<ERating>> ratingsMap = new ElemMap<>();
      for( ERating r : ERating.asList() ) {
        DefaultTsNode<ERating> n = new DefaultTsNode<>( NK_RATING, aRootNode, r );
        n.setName( r.gaugeString() );
        ratingsMap.put( r, n );
      }
      // group items
      for( IGaze g : aItems ) {
        DefaultTsNode<ERating> parent = ratingsMap.getByKey( g.rating() );
        DefaultTsNode<IGaze> n = new DefaultTsNode<>( NK_GAZE, parent, g );
        n.setName( g.incidentDate().toString() );
        parent.addNode( n );
      }
      // add number of items to root node names
      for( ERating r : ERating.asList() ) {
        DefaultTsNode<ERating> n = ratingsMap.getByKey( r );
        String name = String.format( "%s (%d)", n.name(), n.childs().size() ); //$NON-NLS-1$
        n.setName( name );
      }
      return (IList)ratingsMap.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_GAZE;
    }
  }

  static class TmByYear
      implements ITsTreeMaker<IGaze> {

    @SuppressWarnings( { "rawtypes", "unchecked", "boxing" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IGaze> aItems ) {
      IIntMapEdit<DefaultTsNode<Integer>> yearsMap = new SortedIntMap();
      // group items
      for( IGaze g : aItems ) {
        Integer y = Integer.valueOf( g.incidentDate().getYear() );
        DefaultTsNode<Integer> parent = yearsMap.findByKey( y );
        if( parent == null ) {
          parent = new DefaultTsNode<>( NK_YEAR, aRootNode, y );
          parent.setName( y.toString() );
          yearsMap.put( y.intValue(), parent );
        }
        DefaultTsNode<IGaze> n = new DefaultTsNode<>( NK_GAZE, parent, g );
        n.setName( g.incidentDate().toString() );
        parent.addNode( n );
      }
      // add number of items to root node names
      for( Integer y : yearsMap.keys() ) {
        DefaultTsNode<Integer> n = yearsMap.getByKey( y );
        String name = String.format( "%s (%d)", n.name(), n.childs().size() ); //$NON-NLS-1$
        n.setName( name );
      }
      return (IList)yearsMap.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_GAZE;
    }
  }

  static class TmByMonth
      implements ITsTreeMaker<IGaze> {

    @SuppressWarnings( { "rawtypes", "unchecked", "boxing" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IGaze> aItems ) {
      IMapEdit<Month, DefaultTsNode<Month>> monthsMap = new SortedElemMap();
      // group items
      for( IGaze g : aItems ) {
        Month m = g.incidentDate().getMonth();
        DefaultTsNode<Month> parent = monthsMap.findByKey( m );
        if( parent == null ) {
          parent = new DefaultTsNode<>( NK_MONTH, aRootNode, m );
          parent.setName( m.getDisplayName( TextStyle.FULL_STANDALONE, Locale.getDefault() ) );
          monthsMap.put( m, parent );
        }
        DefaultTsNode<IGaze> n = new DefaultTsNode<>( NK_GAZE, parent, g );
        n.setName( g.incidentDate().toString() );
        parent.addNode( n );
      }
      // add number of items to root node names
      for( Month m : monthsMap.keys() ) {
        DefaultTsNode<Month> n = monthsMap.getByKey( m );
        String name = String.format( "%s (%d)", n.name(), n.childs().size() ); //$NON-NLS-1$
        n.setName( name );
      }
      return (IList)monthsMap.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_GAZE;
    }
  }

  static class TmByHh
      implements ITsTreeMaker<IGaze> {

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IGaze> aItems ) {
      IIntMapEdit<DefaultTsNode<Integer>> hhMap = new SortedIntMap();

      // TODO ooh... time of day is NOT supported in project v3

      // // group items
      // for( IGaze g : aItems ) {
      // Integer y = Integer.valueOf( g.incidentDate().getYear() );
      // DefaultTsNode<Integer> parent = yearsMap.findByKey( y );
      // if( parent == null ) {
      // parent = new DefaultTsNode<>( NK_YEAR, aRootNode, y );
      // parent.setName( y.toString() );
      // yearsMap.put( y.intValue(), parent );
      // }
      // DefaultTsNode<IGaze> n = new DefaultTsNode<>( NK_GAZE, parent, g );
      // n.setName( g.incidentDate().toString() );
      // parent.addNode( n );
      // }
      // // add number of items to root node names
      // for( Integer y : yearsMap.keys() ) {
      // DefaultTsNode<Integer> n = yearsMap.getByKey( y );
      // String name = String.format( "%s (%d)", n.name(), n.childs().size() ); //$NON-NLS-1$
      // n.setName( name );
      // }
      return (IList)hhMap.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_GAZE;
    }
  }

}
