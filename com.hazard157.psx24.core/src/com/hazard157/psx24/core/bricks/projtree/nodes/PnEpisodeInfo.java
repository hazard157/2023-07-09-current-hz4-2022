package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Узел сущности {@link EpisodeInfo} в {@link EpisodeInfo}.
 *
 * @author hazard157
 */
public class PnEpisodeInfo
    extends LeafTsNode<EpisodeInfo> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "episode_info"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<EpisodeInfo> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_EPISODE_INFO, STR_D_NK_EPISODE_INFO, EpisodeInfo.class, false, ICONID_DIALOG_INFORMATION );

  PnEpisodeInfo( ITsNode aParent, EpisodeInfo aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( String.format( FMT_N_EPISODE_INFO, entity().actionInterval().toString(), entity().place() ) );
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
