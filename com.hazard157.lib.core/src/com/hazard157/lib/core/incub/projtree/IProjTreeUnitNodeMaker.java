package com.hazard157.lib.core.incub.projtree;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Creates the subtree for {@link IProjTreeBuilder} starting from specified {@link IProjDataUnit}.
 *
 * @author hazard157
 */
public sealed interface IProjTreeUnitNodeMaker permits AbstractProjTreeUnitNodeMaker<?> {

  /**
   * Determines if unit has to be handled by this maker.
   *
   * @param aUnit {@link IProjDataUnit} - project unit
   * @return boolean - unit will be handled by {@link #makeSubtree(ITsNode, IProjDataUnit)}
   */
  boolean isMyUnit( IProjDataUnit aUnit );

  /**
   * Creates subtree for specified unit.
   *
   * @param aRoot {@link ITsNode} - the project root node
   * @param aUnit {@link IProjDataUnit} - project unit checked to pass {@link #isMyUnit(IProjDataUnit)}
   * @return {@link ITsNode} - subtree root created under <code>aRoot</code> node
   */
  ITsNode makeSubtree( ITsNode aRoot, IProjDataUnit aUnit );

}
