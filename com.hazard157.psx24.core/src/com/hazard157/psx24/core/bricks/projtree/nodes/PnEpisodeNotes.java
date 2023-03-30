package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Узел списка {@link IEpisode#noteLine()}.
 *
 * @author goga
 */
public class PnEpisodeNotes
    extends ChildedTsNode<INoteLine> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "episode_notes"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<INoteLine> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_NOTES, STR_D_NK_NOTES, INoteLine.class, true, ICON_PSX_NOTE );

  PnEpisodeNotes( ITsNode aParent, INoteLine aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( STR_N_NK_PLANES );
  }

  @Override
  protected void doCollectNodes( org.toxsoft.core.tslib.coll.IListEdit<ITsNode> aChilds ) {
    for( Mark<String> mark : entity().listMarks() ) {
      aChilds.add( new PnEpisodeNotesNote( this, mark ) );
    }
  }

}
