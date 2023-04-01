package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Узел дерева ярлыков {@link IEpisode#tagLine()}.
 *
 * @author hazard157
 */
public class PnEpisodeTaglineTag
    extends ChildedTsNode<ITag> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "tagline_tag"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<ITag> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_TAGLINE_TAG, STR_D_NK_TAGLINE_TAG, ITag.class, true, ICON_TAG );

  PnEpisodeTaglineTag( ITsNode aParent, ITag aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( String.format( STR_N_TAGLINE_TAG, entity().id(), entity().description() ) );
  }

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    ITsNode epNode = parent();
    while( epNode.kind() != PnEpisode.NODE_KIND ) {
      epNode = epNode.parent();
      TsInternalErrorRtException.checkNull( epNode );
    }
    String episodeId = ((IEpisode)epNode.entity()).episodeId();
    IUnitEpisodes epMan = context().get( IUnitEpisodes.class );
    ITagLine tagLine = epMan.items().getByKey( episodeId ).tagLine();
    for( Secint in : tagLine.marks( entity().id() ).values() ) {
      aChilds.add( new PnEpisodeTaglineTagInterval( this, in ) );
    }
  }

}
