package com.hazard157.psx24.catnote.main;

import org.toxsoft.core.tslib.bricks.strid.*;

import com.hazard157.common.quants.visumple.*;
import com.hazard157.psx.common.stuff.place.*;

/**
 * Notebook note.
 *
 * @author hazard157
 */
public interface INbNote
    extends IStridableParameterized, IParamsVisumplable, IParamsPlaceable {

  /**
   * Returns the note kind.
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
