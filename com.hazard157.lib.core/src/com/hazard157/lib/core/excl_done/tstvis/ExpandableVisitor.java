package com.hazard157.lib.core.excl_done.tstvis;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsNodeVisitor} implementation with ability to registed node visitors.
 *
 * @author hazard157
 */
public class ExpandableVisitor
    extends AbstractTsNodeVisitor {

  static class UnhandledNodesHandler
      implements INodeVisitorHandler {

    final IStringMapEdit<IListEdit<ITsNode>> unhandledNodes = new StringMap<>();

    @Override
    public boolean isMyNode( ITsNode aNode ) {
      return true;
    }

    @Override
    public boolean visitNode( ITsNode aNode ) {
      String kindId = aNode.kind().id();
      IListEdit<ITsNode> ll = unhandledNodes.findByKey( kindId );
      if( ll == null ) {
        ll = new ElemLinkedBundleList<>();
        unhandledNodes.put( kindId, ll );
      }
      ll.add( aNode );
      return false;
    }

  }

  private final IListEdit<INodeVisitorHandler> handlers = new ElemArrayList<>();

  private UnhandledNodesHandler currentUnhanledHandler = null;

  /**
   * Constructor.
   */
  public ExpandableVisitor() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private INodeVisitorHandler getdHandler( ITsNode aNode ) {
    for( INodeVisitorHandler h : handlers ) {
      if( h.isMyNode( aNode ) ) {
        return h;
      }
    }
    return currentUnhanledHandler;
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsNodeVisitor
  //

  @Override
  final protected boolean beforeStartSubtree( ITsNode aRoot ) {
    currentUnhanledHandler = new UnhandledNodesHandler();
    return doBeforeStartSubtree( aRoot );
  }

  @Override
  final protected void afterEndSubtree( ITsNode aRoot, boolean aWasCancelled, Throwable aException,
      ITsNode aStopperNode ) {
    IStringMapEdit<IListEdit<ITsNode>> unhandledNodes = currentUnhanledHandler.unhandledNodes;
    currentUnhanledHandler = null;
    doAfterEndSubtree( aRoot, aWasCancelled, aException, aStopperNode, unhandledNodes );
  }

  @Override
  final protected boolean doVisitNode( ITsNode aNode ) {
    INodeVisitorHandler handler = getdHandler( aNode );
    return handler.visitNode( aNode );
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
  protected boolean doBeforeStartSubtree( ITsNode aRoot ) {
    return false;
  }

  /**
   * Called from {@link #visitNode(ITsNode)} before subtree iteration has finished.
   * <p>
   * <code>aUnhandledNodes</code> contains map "node kind ID" - "nodes" that were visited but not handled by any of
   * registered visitors beqacuse no visitor claimed it by {@link INodeVisitorHandler#isMyNode(ITsNode)}.
   *
   * @param aRoot {@link ITsNode} - the root of visited subtree, never is <code>null</code>
   * @param aWasCancelled boolean - <code>true</code> only if all nodes were visited
   * @param aException {@link Thread} - an exception or <code>null</code>
   * @param aStopperNode {@link ITsNode} - node that cancelled iteration
   * @param aUnhandledNodes {@link IStringMap}&lt;{@link IListEdit}&lt;ITsNode&gt;&gt; - unhandled nodes
   */
  protected void doAfterEndSubtree( ITsNode aRoot, boolean aWasCancelled, Throwable aException, ITsNode aStopperNode,
      IStringMap<IListEdit<ITsNode>> aUnhandledNodes ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Registers node visiting handler.
   * <p>
   * Exsiting handler registration is ignored.
   *
   * @param aHandler {@link INodeVisitorHandler} - the handler
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void registerNodeHandler( INodeVisitorHandler aHandler ) {
    if( !handlers.hasElem( aHandler ) ) {
      handlers.add( aHandler );
    }
  }

}
