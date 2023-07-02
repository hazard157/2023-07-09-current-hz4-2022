package com.hazard157.prisex24.m5.mingle;

import static com.hazard157.prisex24.m5.mingle.IPsxResources.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.common.quants.rating.*;
import com.hazard157.psx.proj3.mingle.*;

class TreeMakers {

  static final ITsNodeKind<IMingle> NK_MINGLE = new TsNodeKind<>( "Mingle", //$NON-NLS-1$
      IMingle.class, false, null );

  static final ITsNodeKind<ERating> NK_RATING = new TsNodeKind<>( "Rating", //$NON-NLS-1$
      ERating.class, true, null );

  static final ITsNodeKind<Integer> NK_YEAR = new TsNodeKind<>( "Year", //$NON-NLS-1$
      Integer.class, true, null );

  static final ITsNodeKind<Month> NK_MONTH = new TsNodeKind<>( "Month", //$NON-NLS-1$
      Month.class, true, null );

  static final ITsNodeKind<Integer> NK_HH = new TsNodeKind<>( "Hh", //$NON-NLS-1$
      Integer.class, true, null );

  static final TreeModeInfo<IMingle> TMI_BY_YEAR = new TreeModeInfo<>( "ByYear", //$NON-NLS-1$
      STR_TMI_BY_YEAR, STR_TMI_BY_YEAR_D, null, new TmByYear() );

  static final TreeModeInfo<IMingle> TMI_BY_MONTH = new TreeModeInfo<>( "ByMonth", //$NON-NLS-1$
      STR_TMI_BY_MONTH, STR_TMI_BY_MONTH_D, null, new TmByMonth() );

  // TODO ooh... time of day is NOT supported in project v3
  static final TreeModeInfo<IMingle> TMI_BY_HH = new TreeModeInfo<>( "ByHh", //$NON-NLS-1$
      STR_TMI_BY_HH, STR_TMI_BY_HH_D, null, new TmByHh() );

  static class TmByYear
      implements ITsTreeMaker<IMingle> {

    @SuppressWarnings( { "rawtypes", "unchecked", "boxing" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IMingle> aItems ) {
      IIntMapEdit<DefaultTsNode<Integer>> yearsMap = new SortedIntMap();
      // group items
      for( IMingle g : aItems ) {
        Integer y = Integer.valueOf( g.incidentDate().getYear() );
        DefaultTsNode<Integer> parent = yearsMap.findByKey( y );
        if( parent == null ) {
          parent = new DefaultTsNode<>( NK_YEAR, aRootNode, y );
          parent.setName( y.toString() );
          yearsMap.put( y.intValue(), parent );
        }
        DefaultTsNode<IMingle> n = new DefaultTsNode<>( NK_MINGLE, parent, g );
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
      return aNode.kind() == NK_MINGLE;
    }
  }

  static class TmByMonth
      implements ITsTreeMaker<IMingle> {

    @SuppressWarnings( { "rawtypes", "unchecked", "boxing" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IMingle> aItems ) {
      IMapEdit<Month, DefaultTsNode<Month>> monthsMap = new SortedElemMap();
      // group items
      for( IMingle g : aItems ) {
        Month m = g.incidentDate().getMonth();
        DefaultTsNode<Month> parent = monthsMap.findByKey( m );
        if( parent == null ) {
          parent = new DefaultTsNode<>( NK_MONTH, aRootNode, m );
          parent.setName( m.getDisplayName( TextStyle.FULL_STANDALONE, Locale.getDefault() ) );
          monthsMap.put( m, parent );
        }
        DefaultTsNode<IMingle> n = new DefaultTsNode<>( NK_MINGLE, parent, g );
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
      return aNode.kind() == NK_MINGLE;
    }
  }

  static class TmByHh
      implements ITsTreeMaker<IMingle> {

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IMingle> aItems ) {
      IIntMapEdit<DefaultTsNode<Integer>> hhMap = new SortedIntMap();

      // TODO ooh... time of day is NOT supported in project v3

      // // group items
      // for( IMingle g : aItems ) {
      // Integer y = Integer.valueOf( g.incidentDate().getYear() );
      // DefaultTsNode<Integer> parent = yearsMap.findByKey( y );
      // if( parent == null ) {
      // parent = new DefaultTsNode<>( NK_YEAR, aRootNode, y );
      // parent.setName( y.toString() );
      // yearsMap.put( y.intValue(), parent );
      // }
      // DefaultTsNode<IMingle> n = new DefaultTsNode<>( NK_MINGLE, parent, g );
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
      return aNode.kind() == NK_MINGLE;
    }
  }

}
