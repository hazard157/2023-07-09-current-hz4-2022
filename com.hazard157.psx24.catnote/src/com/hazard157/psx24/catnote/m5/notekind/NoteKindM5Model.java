package com.hazard157.psx24.catnote.m5.notekind;

import static com.hazard157.psx24.catnote.m5.INbNotebookM5Constants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * {@link ENbNoteKind} M5 model.
 *
 * @author hazard157
 */
public class NoteKindM5Model
    extends M5StridableEnumModelBase<ENbNoteKind> {

  /**
   * Constructor.
   */
  public NoteKindM5Model() {
    super( MID_NB_NOTE_KIND, ENbNoteKind.class );
  }

}
