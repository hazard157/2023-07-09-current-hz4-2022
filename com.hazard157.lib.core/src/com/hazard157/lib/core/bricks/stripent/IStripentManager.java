package com.hazard157.lib.core.bricks.stripent;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Changeable {@link IStripent} entities manager.
 *
 * @author goga
 * @param <T> - the entity type
 */
public interface IStripentManager<T extends IStripent>
    extends IProjDataUnit, IParameterizedEdit {

  /**
   * Returns the items in manager.
   *
   * @return {@link INotifierStridablesListEdit}&lt;T&gt; - items list
   */
  INotifierStridablesListEdit<T> items();

  /**
   * Returns the entity creator.
   *
   * @return {@link IStripentCreator}&lt;T&gt; - the entity creator
   */
  IStripentCreator<T> creator();

  /**
   * Returns the {@link #items()} reorderer.
   *
   * @return {@link IListReorderer}&lt;T&gt; - the {@link #items()} reorderer
   */
  IListReorderer<T> reorderer();

}
