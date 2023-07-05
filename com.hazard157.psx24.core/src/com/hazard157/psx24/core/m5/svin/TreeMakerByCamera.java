package com.hazard157.psx24.core.m5.svin;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Groups SVINs by {@link Svin#cameraId()}.
 *
 * @author hazard157
 */
class TreeMakerByCamera
    implements ITsTreeMaker<Svin> {

  static final String MODE_ID = "ByCamera"; //$NON-NLS-1$

  private final IUnitEpisodes unitEpisodes;

  public TreeMakerByCamera( IUnitEpisodes aUnitEpisodes ) {
    unitEpisodes = aUnitEpisodes;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Svin> aItems ) {
    IStringMapEdit<DefaultTsNode<String>> camMap = new StringMap<>();
    for( Svin s : aItems ) {
      String camId = IStridable.NONE_ID;
      IEpisode e = unitEpisodes.items().findByKey( s.episodeId() );
      if( e != null ) {
        IScene scene = e.story().findBestSceneFor( s.interval(), true );
        camId = scene.frame().cameraId();
      }

      DefaultTsNode<String> parent = camMap.findByKey( camId );
      if( parent == null ) {
        parent = new DefaultTsNode<>( SvinM5Mpc.NK_CAM_ID, aRootNode, camId );
        camMap.put( camId, parent );
      }
      DefaultTsNode<Svin> node = new DefaultTsNode<>( SvinM5Mpc.NK_SVIN, parent, s );
      parent.addNode( node );
    }
    return (IList)camMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == SvinM5Mpc.NK_SVIN;
  }

}
