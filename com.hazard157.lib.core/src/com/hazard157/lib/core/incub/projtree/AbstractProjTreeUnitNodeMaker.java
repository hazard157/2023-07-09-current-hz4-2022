package com.hazard157.lib.core.incub.projtree;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * The only implementation of {@link IProjTreeUnitNodeMaker}.
 *
 * @author hazard157
 * @param <U> - expected type of unit
 */
public abstract non-sealed class AbstractProjTreeUnitNodeMaker<U extends IProjDataUnit>
    implements IProjTreeUnitNodeMaker, ITsGuiContextable {

  private final Class<U> classOfUnit;
  private ITsNode        rootNode = null;

  /**
   * Constructor for subclasses.
   *
   * @param aClassOfUnit {@link Class}&lt;U&gt; - class of unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractProjTreeUnitNodeMaker( Class<U> aClassOfUnit ) {
    TsNullArgumentRtException.checkNull( aClassOfUnit );
    TsIllegalArgumentRtException.checkFalse( IProjDataUnit.class.isAssignableFrom( aClassOfUnit ) );
    classOfUnit = aClassOfUnit;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNull( rootNode );
    return rootNode.context();
  }

  // ------------------------------------------------------------------------------------
  // IProjTreeUnitNodeMaker
  //

  @Override
  public boolean isMyUnit( IProjDataUnit aUnit ) {
    if( classOfUnit.isInstance( aUnit ) ) {
      return doIsMyUnit( classOfUnit.cast( aUnit ) );
    }
    return false;
  }

  @Override
  public ITsNode makeSubtree( ITsNode aRoot, IProjDataUnit aUnit ) {
    TsNullArgumentRtException.checkNulls( aRoot, aUnit );
    rootNode = aRoot;
    ITsNode node = doMakeSubtree( aRoot, classOfUnit.cast( aUnit ) );
    rootNode = null;
    TsInternalErrorRtException.checkNull( node );
    return node;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may perform additional check.
   * <p>
   * In base class returns <code>true</code> there is no need to call superclass method when overriding.
   *
   * @param aUnit &lt;U&gt; - unit of expected class
   * @return boolean - unit will be handled by {@link #makeSubtree(ITsNode, IProjDataUnit)}
   */
  protected boolean doIsMyUnit( U aUnit ) {
    return true;
  }

  /**
   * Subclass must create subtree node.
   *
   * @param aRoot {@link ITsNode} - the project root node
   * @param aUnit &lt;U&gt; - project unit of expected type checked to pass {@link #isMyUnit(IProjDataUnit)}
   * @return {@link ITsNode} - subtree root created under <code>aRoot</code> node
   */
  protected abstract ITsNode doMakeSubtree( ITsNode aRoot, U aUnit );

}
