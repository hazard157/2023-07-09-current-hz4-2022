package com.hazard157.psx24.catnote.main;

import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

/**
 * Notebook.
 *
 * @author hazard157
 */
public interface INbNotebook
    extends IProjDataUnit {

  /**
   * Returns the notes manager.
   *
   * @return {@link IStriparManager}&lt;{@link INbNote}&gt; - the notes manager
   */
  IStriparManager<INbNote> notes();

  /**
   * Returns the categories.
   *
   * @return {@link IStriparManager}&lr;{@link INbCategory}&gt; - categories manager
   */
  IStriparManager<INbCategory> rootCategory();

}
