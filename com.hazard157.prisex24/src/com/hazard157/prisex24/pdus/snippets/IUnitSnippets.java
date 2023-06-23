package com.hazard157.prisex24.pdus.snippets;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

/**
 * {@link ISnippet} entities management and storage engine.
 *
 * @author hazard157
 */
public interface IUnitSnippets
    extends IStriparManager<ISnippet> {

  /**
   * Returns the categories mentioned in all snippets {@link #items()}.
   * <p>
   * Category of the snippet is returned by {@link ISnippet#category()}.
   *
   * @return {@link IStringList} - categories list, including an empty category name (if present)
   */
  IStringList listCategories();

  /**
   * Returns snippets of the specified category.
   * <p>
   * For non-existing categories returns an empty list.
   *
   * @param aCategory - the category, may be an empty string
   * @return {@link IStridablesList}&lt;{@link ISnippet}&gt; - the list of the snippets
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IStridablesList<ISnippet> listByCategory( String aCategory );

}
