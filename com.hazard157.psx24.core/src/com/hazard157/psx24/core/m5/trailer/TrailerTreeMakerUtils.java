package com.hazard157.psx24.core.m5.trailer;

import static com.hazard157.psx24.core.m5.trailer.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;

/**
 * Построители дерева (группираторы) трейлеров.
 *
 * @author goga
 */
class TrailerTreeMakerUtils {

  static final ITsNodeKind<Trailer>  NK_TRAILER = new TsNodeKind<>( "Trailer", Trailer.class, false );  //$NON-NLS-1$
  static final ITsNodeKind<IEpisode> NK_EPISODE = new TsNodeKind<>( "IEpisode", IEpisode.class, true ); //$NON-NLS-1$
  static final ITsNodeKind<String>   NK_NAME    = new TsNodeKind<>( "Name", String.class, true );       //$NON-NLS-1$

  /**
   * Группиратор трейлеров по эпизодам.
   *
   * @author goga
   */
  static class GroupByEpisode
      implements ITsTreeMaker<Trailer> {

    private final ITsGuiContext tsContext;

    GroupByEpisode( ITsGuiContext aContext ) {
      tsContext = TsNullArgumentRtException.checkNull( aContext );
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Trailer> aItems ) {
      IMapEdit<String, DefaultTsNode<IEpisode>> rootNodes = new SortedElemMap<>();
      IUnitTrailers unitTrailers = tsContext.get( IUnitTrailers.class );
      IUnitEpisodes unitEpisodes = tsContext.get( IUnitEpisodes.class );
      // сначала создадим корневые узлы всех эпизодов
      for( IEpisode e : unitEpisodes.items() ) {
        DefaultTsNode<IEpisode> epNode = new DefaultTsNode<>( TrailerTreeMakerUtils.NK_EPISODE, aRootNode, e );
        epNode.setName( EpisodeUtils.ymdFromId( e.id() ) );
        rootNodes.put( e.id(), epNode );
      }
      // пройдем по трейлерам и разложим по эпизодам
      for( Trailer t : unitTrailers.tsm().items() ) {
        IEpisode e = unitEpisodes.items().findByKey( t.episodeId() );
        if( e != null ) {
          DefaultTsNode<IEpisode> epNode = rootNodes.getByKey( t.episodeId() );
          DefaultTsNode<Trailer> trNode = new DefaultTsNode<>( TrailerTreeMakerUtils.NK_TRAILER, epNode, t );
          epNode.addNode( trNode );
        }
        else {
          LoggerUtils.errorLogger().warning( FMT_ERR_UNKNOWN_EPISODES_TRAILER, t.id(), t.episodeId() );
        }
      }
      return (IList)rootNodes.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == TrailerTreeMakerUtils.NK_TRAILER;
    }

  }

  /**
   * Группиратор трейлеров по имени (локальному идентификатору).
   *
   * @author goga
   */
  static class GroupByName
      implements ITsTreeMaker<Trailer> {

    private final ITsGuiContext tsContext;

    GroupByName( ITsGuiContext aContext ) {
      tsContext = TsNullArgumentRtException.checkNull( aContext );
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Trailer> aItems ) {
      IMapEdit<String, DefaultTsNode<String>> rootNodes = new SortedElemMap<>();
      IUnitTrailers unitTrailers = tsContext.get( IUnitTrailers.class );
      for( Trailer t : unitTrailers.tsm().items() ) {
        String name = t.localId();
        DefaultTsNode<String> nRoot = rootNodes.findByKey( name );
        if( nRoot == null ) {
          nRoot = new DefaultTsNode<>( TrailerTreeMakerUtils.NK_NAME, aRootNode, name );
          rootNodes.put( name, nRoot );
        }
        nRoot.addNode( new DefaultTsNode<>( TrailerTreeMakerUtils.NK_TRAILER, nRoot, t ) );
      }
      return (IList)rootNodes.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == TrailerTreeMakerUtils.NK_TRAILER;
    }

  }

}
