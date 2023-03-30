package com.hazard157.psx24.core.m5.svin;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx24.core.*;

/**
 * Построитель дерева {@link Svin}-ов по эпизодам.
 *
 * @author goga
 */
class TreeMakerByEpisode
    implements ITsTreeMaker<Svin>, IPsxStdReferences {

  static final String MODE_ID = "ByEpisode"; //$NON-NLS-1$

  private final ITsGuiContext tsContext;

  public TreeMakerByEpisode( ITsGuiContext aContext ) {
    tsContext = aContext;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Svin> aItems ) {
    IStringMapEdit<DefaultTsNode<String>> epMap = new StringMap<>();
    for( Svin s : aItems ) {
      DefaultTsNode<String> parent = epMap.findByKey( s.episodeId() );
      if( parent == null ) {
        parent = new DefaultTsNode<>( SvinM5Mpc.NK_EP_ID, aRootNode, s.episodeId() ) {

          @Override
          protected Image doGetImage( EIconSize aIconSize ) {
            // TODO IEpisode e = unitEpisodes().items().findByKey( entity() );
            // if( e != null ) {
            // File frFile = psxFileSystem().findFrameFile( e.frame() );
            // EThumbSize thSize = EThumbSize.findIncluding( aIconSize );
            // TsImage tsim = imageManager().findThumb( frFile, thSize );
            // if( tsim != null ) {
            // return tsim.image();
            // }
            // }
            return null;
          }

        };
        epMap.put( s.episodeId(), parent );
      }
      DefaultTsNode<Svin> node = new DefaultTsNode<>( SvinM5Mpc.NK_SVIN, parent, s );
      parent.addNode( node );
    }
    return (IList)epMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == SvinM5Mpc.NK_SVIN;
  }

  // ------------------------------------------------------------------------------------
  // IPsxStdReferences
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
