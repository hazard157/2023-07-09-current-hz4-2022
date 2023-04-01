package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Узел списка {@link IEpisode#story()}.
 *
 * @author hazard157
 */
public class PnEpisodeStory
    extends ChildedTsNode<IStory> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "story"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<IStory> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_STORY, STR_D_NK_STORY, IStory.class, true, ICON_PSX_STORY_EDITOR );

  PnEpisodeStory( ITsNode aParent, IStory aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( STR_N_NK_STORY );
  }

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    for( IScene s : entity().childScenes() ) {
      aChilds.add( new PnEpisodeScene( this, s ) );
    }
  }

}
