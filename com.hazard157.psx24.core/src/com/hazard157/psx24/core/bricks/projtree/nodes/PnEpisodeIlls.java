package com.hazard157.psx24.core.bricks.projtree.nodes;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Узел списка {@link IEpisode#getIllustrations(boolean)}.
 *
 * @author goga
 */
@SuppressWarnings( "rawtypes" )
public class PnEpisodeIlls
    extends ChildedTsNode<IList> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "illustrations"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<IList> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_EPISODE_ILLS, STR_D_NK_EPISODE_ILLS, IList.class, true, ICON_PSX_FOLDER_IMAGE );

  PnEpisodeIlls( ITsNode aParent, IList aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( STR_N_NK_EPISODE_ILLS );
  }

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    for( IFrame f : ((IList<IFrame>)entity()) ) {
      aChilds.add( new PnEpisodeIllsFrame( this, f ) );
    }
  }

}
