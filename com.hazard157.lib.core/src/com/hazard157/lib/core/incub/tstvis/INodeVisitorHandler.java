package com.hazard157.lib.core.incub.tstvis;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;

/**
 * Handles node visitor call for {@link ExpandableVisitor}.
 *
 * @author hazard157
 */
public interface INodeVisitorHandler {

  /**
   * Determines if node will be handled for this visitor.
   *
   * @param aNode {@link ITsNode} - node to be visited
   * @return boolean - node will be handled by {@link #visitNode(ITsNode)}
   */
  boolean isMyNode( ITsNode aNode );

  /**
   * Visits the single node of tree from the {@link ITsNodeVisitor#visitNode(ITsNode)}.
   * <p>
   * Node is guaranteed to pass {@link #isMyNode(ITsNode)} check.
   *
   * @param <T> - node class
   * @param aNode {@link ITsNode} - visited node, never is <code>null</code>
   * @return boolean - <code>true</code> indicates to stop iteration
   */
  <T extends ITsNode> boolean visitNode( T aNode );

}
