package com.hazard157.psx24.catnote.main;

import static com.hazard157.psx24.catnote.main.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
public interface INbNotebookConstants {

  /**
   * {@link INbNote#params()} option: the note kind.
   */
  IDataDef OP_NOTE_KIND = DataDef.create( "kind", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_OP_NOTE_KIND, //
      TSID_DESCRIPTION, STR_D_OP_NOTE_KIND, //
      TSID_DEFAULT_VALUE, avValobj( ENbNoteKind.MISC ), //
      TSID_KEEPER_ID, ENbNoteKind.KEEPER_ID //
  );

  /**
   * {@link INbNote#params()} option: the note categoryID.
   */
  IDataDef OP_CATEGORY_ID = DataDef.create( "categoryId", STRING, //$NON-NLS-1$
      TSID_NAME, STR_N_OP_CATEGORY_ID, //
      TSID_DESCRIPTION, STR_D_OP_CATEGORY_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

}
