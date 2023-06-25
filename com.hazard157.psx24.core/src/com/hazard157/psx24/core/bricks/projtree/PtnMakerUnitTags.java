package com.hazard157.psx24.core.bricks.projtree;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.lib.core.excl_plan.projtree.*;
import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx24.core.bricks.projtree.nodes.*;

/**
 * Project tree maker for {@link IUnitTags }.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PtnMakerUnitTags
    extends AbstractProjTreeUnitNodeMaker<IUnitTags> {

  public static final String NKID_ROOT = "unit.Tags"; //$NON-NLS-1$

  public static final ITsNodeKind<IUnitTags> NK_ROOT = new TsNodeKind<>( NKID_ROOT, //
      STR_N_NK_UNIT_TAGS, STR_D_NK_UNIT_TAGS, IUnitTags.class, true, ICON_TAGS_LIST );

  /**
   * Constructor.
   */
  public PtnMakerUnitTags() {
    super( IUnitTags.class );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjTreeUnitNodeMaker
  //

  @Override
  protected ITsNode doMakeSubtree( ITsNode aRoot, IUnitTags aUnit ) {
    ITsNode root = new ChildedTsNode<>( NK_ROOT, aRoot, aUnit ) {

      @Override
      protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
        for( ITag e : entity().root().childNodes() ) {
          ITsNode n = new PnUnitTagsTag( this, e );
          aChilds.add( n );
        }
      }
    };
    return root;
  }

}
