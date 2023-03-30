package com.hazard157.psx24.catnote.main;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Notebook note.
 *
 * @author goga
 */
public interface INbNote
    extends IStridableParameterized {

  /**
   * Retunr the note kind.
   *
   * @return {@link ENbNoteKind} - the note kind
   */
  ENbNoteKind kind();

  /**
   * Returns the category ID.
   *
   * @return String - the category ID (IDpath) or an empty string
   */
  String categoryId();

}
