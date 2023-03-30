package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Узел елемента списка {@link IEpisode#getIllustrations(boolean)}.
 *
 * @author goga
 */
public class PnEpisodeIllsFrame
    extends LeafTsNode<IFrame> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "illustrations_frame"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<IFrame> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_EPISODE_ILLS_FRAME, STR_D_NK_EPISODE_ILLS_FRAME, IFrame.class, false, ICON_PSX_IMAGE_X_GENERIC );

  PnEpisodeIllsFrame( ITsNode aParent, IFrame aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( String.format( FMT_N_ILLS_FRAME, entity().toString() ) );
  }

  @Override
  protected Image doGetImage( EIconSize aIconSize ) {
    IPsxFileSystem fileSystem = context().get( IPsxFileSystem.class );
    TsImage mi = fileSystem.findThumb( entity(), EThumbSize.findIncluding( aIconSize ) );
    if( mi != null ) {
      return mi.image();
    }
    return null;
  }

}
