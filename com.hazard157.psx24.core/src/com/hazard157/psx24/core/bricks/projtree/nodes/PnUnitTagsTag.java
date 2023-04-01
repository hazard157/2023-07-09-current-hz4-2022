package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Узел сущности {@link ITag} в {@link ITag}.
 *
 * @author hazard157
 */
public class PnUnitTagsTag
    extends ChildedTsNode<ITag> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "unit_tags_tag"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<ITag> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_TAG, STR_D_NK_TAG, ITag.class, true, ICON_TAG );

  @SuppressWarnings( "javadoc" )
  public PnUnitTagsTag( ITsNode aParent, ITag aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( StridUtils.printf( StridUtils.FORMAT_ID_NAME, entity() ) );
  }

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    for( ITag tag : entity().childNodes() ) {
      aChilds.add( new PnUnitTagsTag( this, tag ) );
    }
  }

}
