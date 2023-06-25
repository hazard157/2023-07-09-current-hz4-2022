package com.hazard157.lib.core.excl_plan.projtree;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * The only implementation of {@link IProjTreeBuilder}.
 *
 * @author hazard157
 */
public final class ProjTreeBuilder
    implements IProjTreeBuilder {

  private final IListEdit<IProjTreeUnitNodeMaker> makers = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public ProjTreeBuilder() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IProjTreeUnitNodeMaker findMaker( IProjDataUnit aUnit ) {
    for( IProjTreeUnitNodeMaker m : makers ) {
      if( m.isMyUnit( aUnit ) ) {
        return m;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IProjTreeBuilder
  //

  @Override
  public void registerMaker( IProjTreeUnitNodeMaker aMaker ) {
    if( !makers.hasElem( aMaker ) ) {
      makers.add( aMaker );
    }
  }

  @Override
  public ITsNode createRootNode( ITsProject aProject, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aProject );
    DefaultTsNode<ITsProject> root = new DefaultTsNode<>( ROOT_KIND, aProject, aContext );
    for( IProjDataUnit u : aProject.units() ) {
      IProjTreeUnitNodeMaker maker = findMaker( u );
      if( maker != null ) {
        ITsNode unitNode = maker.makeSubtree( root, u );
        root.addNode( unitNode );
      }
    }
    return root;
  }

}
