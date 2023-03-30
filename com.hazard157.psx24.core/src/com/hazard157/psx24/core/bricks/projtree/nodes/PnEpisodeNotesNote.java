package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Узел елемента списка {@link IEpisode#noteLine()}.
 *
 * @author goga
 */
@SuppressWarnings( "rawtypes" )
public class PnEpisodeNotesNote
    extends LeafTsNode<Mark> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "episode_notes_mark"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<Mark> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_NOTES_NOTE, STR_D_NK_NOTES_NOTE, Mark.class, false, ICON_PSX_NOTE );

  PnEpisodeNotesNote( ITsNode aParent, Mark aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( String.format( FMT_N_NOTELINE_NOTE, entity().in().toString(), entity().marker().toString() ) );
  }

  @Override
  protected Image doGetImage( EIconSize aIconSize ) {
    // TODO PnEpisodeNotesNote.doGetImage()
    // IEpisode e = context().get( IUnitEpisodes.class ).items().getItem( EPISODE_ID.getValue( ops() ).asString() );
    // IFrame frame = e.story().findBestSceneFor( entity().in(), true ).frame();
    // IMultiImage mi = fileSystem().findThumb( frame, EThumbSize.findIncluding( aIconSize ) );
    // if( mi != null ) {
    // return mi.image();
    // }
    return null;
  }

}
