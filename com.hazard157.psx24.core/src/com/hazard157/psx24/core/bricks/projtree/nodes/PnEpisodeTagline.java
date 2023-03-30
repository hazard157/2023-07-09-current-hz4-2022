package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Узел списка {@link IEpisode#tagLine()}.
 *
 * @author goga
 */
public class PnEpisodeTagline
    extends ChildedTsNode<ITagLine> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "episode_tagline"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<ITagLine> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_TAGLINE, STR_D_NK_TAGLINE, ITagLine.class, true, ICON_TAGS_LIST );

  PnEpisodeTagline( ITsNode aParent, ITagLine aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( STR_N_NK_TAGLINE );
  }

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    IUnitTags tagMan = context().get( IUnitTags.class );
    for( String tagId : entity().usedTagIds() ) {
      ITag tag = tagMan.root().allNodesBelow().getByKey( tagId );
      aChilds.add( new PnEpisodeTaglineTag( this, tag ) );
    }
  }

}
