package com.hazard157.psx24.core.bricks.projtree.nodes;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.bricks.projtree.nodes.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Узел сцены сюжета {@link IScene}.
 *
 * @author goga
 */
public class PnEpisodeScene
    extends ChildedTsNode<IScene> {

  /**
   * Идентификатор вида узла {@link #NODE_KIND}.
   */
  public static final String NODE_KIND_ID = "scene"; //$NON-NLS-1$

  /**
   * Вид узла.
   */
  public static final ITsNodeKind<IScene> NODE_KIND = new TsNodeKind<>( NODE_KIND_ID, //
      STR_N_NK_SCENE, STR_D_NK_SCENE, IScene.class, true, ICON_PSX_STORY_EDITOR );

  PnEpisodeScene( ITsNode aParent, IScene aEntity ) {
    super( NODE_KIND, aParent, aEntity );
    setName( String.format( FMT_N_SCENE, entity().interval().toString(), entity().info().name() ) );
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
    for( IScene s : entity().childScenes() ) {
      aChilds.add( new PnEpisodeScene( this, s ) );
    }
  }

}
