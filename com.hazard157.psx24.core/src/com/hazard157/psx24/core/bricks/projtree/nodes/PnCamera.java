package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx24.core.bricks.projtree.IPsxTreeMetaInfo.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

import com.hazard157.psx.proj3.cameras.*;

/**
 * Узел сущности {@link Camera} в {@link Camera}.
 *
 * @author hazard157
 */
public class PnCamera
    extends LeafTsNode<Camera> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "unit_cameras_camera"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<Camera> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_CAMERA, STR_D_NK_CAMERA, Camera.class, false, ICONID_CAMERA_GENERIC );

  @SuppressWarnings( "javadoc" )
  public PnCamera( ITsNode aParent, Camera aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( StridUtils.printf( StridUtils.FORMAT_ID_NAME, entity() ) );
    if( !entity().isCamAvailable() ) {
      setIconId( ICONID_CAMERA_GENERIC_DIMMED );
      params().setBool( PTN_OPDEF_IS_DIMMED, true );
    }
  }

}
