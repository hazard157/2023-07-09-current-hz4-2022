package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Узел элемента интервалов ярлыка теглайна {@link ITagLine#marks(String)}.
 *
 * @author hazard157
 */
public class PnEpisodeTaglineTagInterval
    extends LeafTsNode<Secint> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "tagline_tag_interval"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<Secint> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_TAGLINE_TAG_IN, STR_D_NK_TAGLINE_TAG_IN, Secint.class, false, null );

  PnEpisodeTaglineTagInterval( ITsNode aParent, Secint aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( entity().toString() );
  }

}
