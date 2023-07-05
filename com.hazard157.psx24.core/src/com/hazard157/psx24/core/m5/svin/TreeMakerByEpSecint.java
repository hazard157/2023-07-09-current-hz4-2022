package com.hazard157.psx24.core.m5.svin;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.*;

/**
 * Groups SVINs by {@link Svin#episodeId()}.
 *
 * @author hazard157
 */
class TreeMakerByEpSecint
    implements ITsTreeMaker<Svin>, IPsxGuiContextable {

  static final String MODE_ID = "ByEpSecint"; //$NON-NLS-1$

  private final ITsGuiContext tsContext;

  public TreeMakerByEpSecint( ITsGuiContext aContext ) {
    tsContext = aContext;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Svin> aItems ) {
    IStringMapEdit<DefaultTsNode<Svin>> epMap = new SortedStringMap<>();
    // create tree
    for( Svin s : aItems ) {
      DefaultTsNode<Svin> parent = epMap.findByKey( s.episodeId() );
      if( parent == null ) {
        IEpisode e = unitEpisodes().items().getByKey( s.episodeId() );
        parent = new DefaultTsNode<>( SvinM5Mpc.NK_EP_SECINT, aRootNode, e.svin() ) {

          @Override
          protected Image doGetImage( EIconSize aIconSize ) {
            File frFile = psxFileSystem().findFrameFile( e.frame() );
            EThumbSize thSize = EThumbSize.findIncluding( aIconSize );
            TsImage tsim = imageManager().findThumb( frFile, thSize );
            if( tsim != null ) {
              return tsim.image();
            }
            return null;
          }

        };
        parent.setName( e.episodeId() );
        epMap.put( s.episodeId(), parent );
      }
      DefaultTsNode<Svin> node = new DefaultTsNode<>( SvinM5Mpc.NK_SVIN, parent, s );
      parent.addNode( node );
    }
    // update episode nodes SVINs
    for( DefaultTsNode<Svin> epNode : epMap.values() ) {
      int dur = 0;
      for( ITsNode node : epNode.childs() ) {
        if( node.entity() instanceof Svin s ) {
          dur += s.interval().duration();
        }
      }
      Svin epSvin = epNode.entity();
      Svin newSvin = new Svin( epSvin.episodeId(), SvinM5Mpc.EP_SECINT_ENTITY_SVIN_CAM_ID, new Secint( 0, dur - 1 ),
          epSvin.frame() );
      epNode.setEntity( newSvin );
    }
    return (IList)epMap.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == SvinM5Mpc.NK_SVIN || aNode.kind() == SvinM5Mpc.NK_EP_SECINT;
  }

  // ------------------------------------------------------------------------------------
  // IPsxStdReferences
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
