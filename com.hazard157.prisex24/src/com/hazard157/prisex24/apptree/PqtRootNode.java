package com.hazard157.prisex24.apptree;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The application data tree model root node corresponds to the application itself.
 *
 * @author hazard157
 */
public class PqtRootNode
    extends AbstractQRootNode<IPrisexApplication> {

  /**
   * Root node kind ID.
   */
  public static final String NODE_KIND_ID = APP_ID;

  /**
   * Root node ID.
   */
  public static final String ROOT_NODE_ID = APP_ID;

  /**
   * Root node kind.
   */
  public static final IQNodeKind<IPrisexApplication> NODE_KIND = new QNodeKind<>( NODE_KIND_ID, //
      APP_INFO.nmName(), APP_INFO.description(), IPrisexApplication.class, true, ICONID_APP_ICON );

  /**
   * Constructor.
   *
   * @param aContext {@link ITsContext} - tree context
   * @param aEntity &lt;T&gt; - entity in this node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public PqtRootNode( ITsContext aContext, IPrisexApplication aEntity ) {
    super( ROOT_NODE_ID, NODE_KIND, aContext, aEntity, IOptionSet.NULL );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQRootNode
  //

  @Override
  protected IStridablesList<IQNode> doGetNodes() {
    // TODO реализовать PqtRootNode.doGetNodes()
    throw new TsUnderDevelopmentRtException( "PqtRootNode.doGetNodes()" );
  }

}
