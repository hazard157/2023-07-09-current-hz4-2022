package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Узел сущности {@link IEpisode} в {@link IEpisode}.
 *
 * @author hazard157
 */
public class PnEpisode
    extends ChildedTsNode<IEpisode> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "episode"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<IEpisode> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_EPISODE, STR_D_NK_EPISODE, IEpisode.class, true, ICON_PSX_IMAGE_X_GENERIC );

  @SuppressWarnings( "javadoc" )
  public PnEpisode( ITsNode aParent, IEpisode aEntity ) {
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

  @Override
  protected void doCollectNodes( IListEdit<ITsNode> aChilds ) {
    aChilds.add( new PnEpisodeInfo( this, entity().info() ) );
    aChilds.add( new PnEpisodeStory( this, entity().story() ) );
    aChilds.add( new PnEpisodeTagline( this, entity().tagLine() ) );
    aChilds.add( new PnEpisodeNotes( this, entity().noteLine() ) );
    aChilds.add( new PnEpisodePlanes( this, entity().planesLine() ) );
    // FIXME временно, пока не удалим illustrations()
    aChilds.add( new PnEpisodeIlls( this, entity().getIllustrations( true ) ) );
  }

}
