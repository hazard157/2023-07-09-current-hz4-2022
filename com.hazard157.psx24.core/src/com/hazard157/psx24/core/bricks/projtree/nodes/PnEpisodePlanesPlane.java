package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Узел елемента списка {@link IEpisode#planesLine()}.
 *
 * @author hazard157
 */
public class PnEpisodePlanesPlane
    extends LeafTsNode<MarkPlaneGuide> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "planeline_plane"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<MarkPlaneGuide> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_PLANES_PLANE, STR_D_NK_PLANES_PLANE, MarkPlaneGuide.class, false, ICON_PSX_PLANE );

  PnEpisodePlanesPlane( ITsNode aParent, MarkPlaneGuide aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    Mark<PlaneGuide> mark = entity();
    setName( String.format( FMT_N_PLANELINE_PLANE, HmsUtils.mmss( mark.in().start() ), mark.marker().name() ) );
  }

  @Override
  protected Image doGetImage( EIconSize aIconSize ) {
    // TODO PnEpisodePlanesPlane.doGetImage()
    // IPsxFileSystem fileSystem = context().get( IPsxFileSystem.class );
    // IEpisode e = context().get( IUnitEpisodes.class ).items().getItem( EPISODE_ID.getValue( ops() ).asString() );
    // IFrame frame = e.story().findBestSceneFor( entity().in(), true ).frame();
    // IMultiImage mi = fileSystem.findThumb( frame, EThumbSize.findIncluding( aIconSize ) );
    // if( mi != null ) {
    // return mi.image();
    // }
    return null;
  }

}
