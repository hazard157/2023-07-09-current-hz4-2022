package com.hazard157.lib.core.incub.projtree;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * An expandable builder of project tree.
 *
 * @author hazard157
 */
public sealed interface IProjTreeBuilder permits ProjTreeBuilder {

  /**
   * Kind of node created by {@link #createRootNode(ITsProject, ITsGuiContext)}.
   */
  ITsNodeKind<ITsProject> ROOT_KIND = new TsNodeKind<>( "ProjectRoot", ITsProject.class, true ); //$NON-NLS-1$

  /**
   * Register maker for tree building process.
   * <p>
   * Registered makers are ignored.
   *
   * @param aMaker {@link IProjTreeUnitNodeMaker} - the maker
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void registerMaker( IProjTreeUnitNodeMaker aMaker );

  /**
   * Builds project tree and returns the root node.
   *
   * @param aProject {@link ITsProject} - source project
   * @param aContext {@link ITsGuiContext} - the context for root node
   * @return {@link ITsNode} - root node of the project
   */
  ITsNode createRootNode( ITsProject aProject, ITsGuiContext aContext );

}
