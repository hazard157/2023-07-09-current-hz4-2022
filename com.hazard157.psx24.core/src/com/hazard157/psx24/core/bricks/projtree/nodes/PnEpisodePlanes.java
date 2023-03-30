package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Узел списка {@link IEpisode#planesLine()}.
 *
 * @author goga
 */
public class PnEpisodePlanes
    extends ChildedTsNode<IMlPlaneGuide> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "episode_planes"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<IMlPlaneGuide> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_PLANES, STR_D_NK_PLANES, IMlPlaneGuide.class, true, ICON_PSX_PLANE_LINE );

  PnEpisodePlanes( ITsNode aParent, IMlPlaneGuide aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( STR_N_NK_PLANES );
  }

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    for( MarkPlaneGuide f : entity().marksList() ) {
      aChilds.add( new PnEpisodePlanesPlane( this, f ) );
    }
  }

}
