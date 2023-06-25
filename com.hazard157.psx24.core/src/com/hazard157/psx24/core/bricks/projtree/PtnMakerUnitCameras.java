package com.hazard157.psx24.core.bricks.projtree;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx24.core.bricks.projtree.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.common.incub.projtree.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx24.core.bricks.projtree.nodes.*;

/**
 * Project tree maker for {@link IUnitCameras}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class PtnMakerUnitCameras
    extends AbstractProjTreeUnitNodeMaker<IUnitCameras> {

  public static final String NKID_ROOT = "unit.Cameras"; //$NON-NLS-1$

  public static final ITsNodeKind<IUnitCameras> NK_ROOT = new TsNodeKind<>( NKID_ROOT, //
      STR_N_NK_UNIT_CAMERAS, STR_D_NK_UNIT_CAMERAS, IUnitCameras.class, true, ICONID_CAMERA_GENERIC );

  /**
   * Constructor.
   */
  public PtnMakerUnitCameras() {
    super( IUnitCameras.class );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjTreeUnitNodeMaker
  //

  @Override
  protected ITsNode doMakeSubtree( ITsNode aRoot, IUnitCameras aUnit ) {
    ITsNode root = new ChildedTsNode<>( NK_ROOT, aRoot, aUnit ) {

      @Override
      protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
        for( Camera e : entity().items() ) {
          ITsNode n = new PnCamera( this, e );
          aChilds.add( n );
        }
      }
    };
    return root;
  }

}
