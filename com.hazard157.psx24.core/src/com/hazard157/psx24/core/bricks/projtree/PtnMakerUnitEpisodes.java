package com.hazard157.psx24.core.bricks.projtree;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.lib.core.incub.projtree.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.bricks.projtree.nodes.*;

/**
 * Project tree maker for {@link IUnitEpisodes }.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PtnMakerUnitEpisodes
    extends AbstractProjTreeUnitNodeMaker<IUnitEpisodes> {

  public static final String NKID_ROOT = "unit.Episodes"; //$NON-NLS-1$

  public static final ITsNodeKind<IUnitEpisodes> NK_ROOT = new TsNodeKind<>( NKID_ROOT, //
      STR_N_NK_UNIT_EPISODES, STR_D_NK_UNIT_EPISODES, IUnitEpisodes.class, true, ICON_PSX_EPISODES );

  /**
   * Constructor.
   */
  public PtnMakerUnitEpisodes() {
    super( IUnitEpisodes.class );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjTreeUnitNodeMaker
  //

  @Override
  protected ITsNode doMakeSubtree( ITsNode aRoot, IUnitEpisodes aUnit ) {
    ITsNode root = new ChildedTsNode<>( NK_ROOT, aRoot, aUnit ) {

      @Override
      protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
        for( IEpisode e : entity().items() ) {
          ITsNode n = new PnEpisode( this, e );
          aChilds.add( n );
        }
      }
    };
    return root;
  }

}
