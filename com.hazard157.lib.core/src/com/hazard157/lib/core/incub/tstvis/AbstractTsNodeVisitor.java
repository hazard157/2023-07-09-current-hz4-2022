package com.hazard157.lib.core.incub.tstvis;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsNodeVisitor} base implementation contains {@link #visitSubtree(ITsNode)} logic.
 * <p>
 * This visitor visits root node <b>before</b> starting iteration over subtree.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractTsNodeVisitor
    implements ITsNodeVisitor {

  private Throwable exception   = null;
  private ITsNode   stopperNode = null;

  /**
   * Copnstructor for subclasses.
   */
  protected AbstractTsNodeVisitor() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean internalVisitSubtree( ITsNode aNode ) {
    stopperNode = aNode;
    if( visitNode( aNode ) ) {
      return true;
    }
    for( ITsNode n : aNode.childs() ) {
      if( internalVisitSubtree( n ) ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsNodeVisitor
  //

  @Override
  final public boolean visitSubtree( ITsNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    exception = null;
    stopperNode = null;
    if( beforeStartSubtree( aNode ) ) {
      return true;
    }
    boolean retval;
    try {
      retval = internalVisitSubtree( aNode );
    }
    catch( Exception ex ) {
      exception = ex;
      retval = true;
    }
    afterEndSubtree( aNode, retval, exception, stopperNode );
    return retval;
  }

  @Override
  final public boolean visitNode( ITsNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    return doVisitNode( aNode );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Called from {@link #visitNode(ITsNode)}before starting subtree iteration.
   * <p>
   * In base class returns <code>false</code> there ios no need to call superclass method when overriding.
   * <p>
   * This method is <b>not</b> called when visitin single node by {@link #visitNode(ITsNode)}.
   *
   * @param aRoot {@link ITsNode} - the root of visited subtree, never is <code>null</code>
   * @return boolean - <code>true</code> indicates to stop iteration
   */
  protected boolean beforeStartSubtree( ITsNode aRoot ) {
    return false;
  }

  /**
   * Called from {@link #visitNode(ITsNode)} before subtree iteration has finished.
   * <p>
   * Iteration may finished by 3 reasons:
   * <ul>
   * <li>all nodes in subtree were visited;</li>
   * <li>iteration was cancelled by implementation (any {@link #visitNode(ITsNode)} returned false);</li>
   * <li>an exception was thrown while visiting any node.</li>
   * </ul>
   * <p>
   * Arguments <code>aWasCancelled</code>, <code>aException</code> and <code>aStopperNode</code> indicates and supplies
   * information about iteration process. For normal completion <b>aWasCancelled</b> is <code>false</code> other
   * arguments are <code>null</code>.
   * <p>
   * When <code>aWasCancelled</code>=<code>true</code> value of <code>aException</code> determines the reason.
   * <code>aException</code> is <code>null</code> means that <code>aStopperNode</code> returns <code>true</code> from
   * {@link #doVisitNode(ITsNode)}. Otherwise <code>aStopperNode</code> has thrown an <code>aEcpetion</code>.
   * <p>
   * This method is <b>not</b> called when visitin single node by {@link #visitNode(ITsNode)}.
   *
   * @param aRoot {@link ITsNode} - the root of visited subtree, never is <code>null</code>
   * @param aWasCancelled boolean - <code>true</code> only if all nodes were visited
   * @param aException {@link Thread} - an exception or <code>null</code>
   * @param aStopperNode {@link ITsNode} - node that cancelled iteration
   */
  protected void afterEndSubtree( ITsNode aRoot, boolean aWasCancelled, Throwable aException, ITsNode aStopperNode ) {
    // nop
  }

  /**
   * IOmplementaton must perform node visiting.
   *
   * @param aNode {@link ITsNode} - visited node, never is <code>null</code>
   * @return boolean - <code>true</code> indicates to stop iteration
   */
  protected abstract boolean doVisitNode( ITsNode aNode );

}
