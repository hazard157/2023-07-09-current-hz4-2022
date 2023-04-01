package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Узел сущности {@link ISourceVideo} в {@link ISourceVideo}.
 *
 * @author hazard157
 */
public class PnSourceVideo
    extends LeafTsNode<ISourceVideo> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "unit_source_videos_source_video"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<ISourceVideo> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_SOURCE_VIDEO, STR_D_NK_SOURCE_VIDEO, ISourceVideo.class, false, ICON_PSX_VIDEO_X_GENERIC );

  @SuppressWarnings( "javadoc" )
  public PnSourceVideo( ITsNode aParent, ISourceVideo aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( StridUtils.printf( StridUtils.FORMAT_ID_NAME, entity() ) );
  }

  @Override
  protected Image doGetImage( EIconSize aIconSize ) {
    IPsxFileSystem fileSystem = context().get( IPsxFileSystem.class );
    TsImage mi = fileSystem.findThumb( entity().frame(), EThumbSize.findIncluding( aIconSize ) );
    if( mi != null ) {
      return mi.image();
    }
    return null;
  }

}
