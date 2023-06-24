package com.hazard157.lib.core.excl_done.tstvis;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;

/**
 * {@link ITsNode} based tree visitor.
 * <p>
 * The only permited implementation is {@link AbstractTsNodeVisitor}.
 * <p>
 * Order of iteration over tree depends on visitor implementation. Eg when calling {@link #visitSubtree(ITsNode)} tyhe
 * subtree root may be cvisited befor subtree of after.
 *
 * @author hazard157
 */
public sealed interface ITsNodeVisitor permits AbstractTsNodeVisitor {

  /**
   * Iterates over subtree starting from specified root.
   * <p>
   * This method calls {@link #visitNode(ITsNode)} to visit any node in subtree.
   * <p>
   * Returning <code>true</code> will cancel iteration. For exmple this is useful if visitor used to find something in
   * tree.
   *
   * @param aSubtreeRoot {@link ITsNode} - the root of visited subtree, never is <code>null</code>
   * @return boolean - <code>true</code> indicates to stop iteration
   */
  boolean visitSubtree( ITsNode aSubtreeRoot );

  /**
   * Visits the single node of tree.
   * <p>
   * Returned vale has no sense when called for single node, only when called from inside
   * {@link #visitSubtree(ITsNode)}. При одинчном вызве возвращаемое значение не имеет смысла.
   *
   * @param aNode {@link ITsNode} - visited node, never is <code>null</code>
   * @return boolean - <code>true</code> indicates to stop iteration
   */
  boolean visitNode( ITsNode aNode );

}
