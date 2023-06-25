package com.hazard157.psx24.core.bricks.projtree;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.common.incub.projtree.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.bricks.projtree.nodes.*;

/**
 * Project tree maker for {@link IUnitSourceVideos }.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PtnMakerUnitSourceVideos
    extends AbstractProjTreeUnitNodeMaker<IUnitSourceVideos> {

  public static final String NKID_ROOT = "unit.SourceVideos"; //$NON-NLS-1$

  public static final ITsNodeKind<IUnitSourceVideos> NK_ROOT = new TsNodeKind<>( NKID_ROOT, //
      STR_N_NK_SOURCE_VIDEO, STR_D_NK_SOURCE_VIDEO, IUnitSourceVideos.class, true, ICON_PSX_FOLDER_VIDEO );

  /**
   * Constructor.
   */
  public PtnMakerUnitSourceVideos() {
    super( IUnitSourceVideos.class );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjTreeUnitNodeMaker
  //

  @Override
  protected ITsNode doMakeSubtree( ITsNode aRoot, IUnitSourceVideos aUnit ) {
    ITsNode root = new ChildedTsNode<>( NK_ROOT, aRoot, aUnit ) {

      @Override
      protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
        for( ISourceVideo e : entity().items() ) {
          ITsNode n = new PnSourceVideo( this, e );
          aChilds.add( n );
        }
      }
    };
    return root;
  }

}
