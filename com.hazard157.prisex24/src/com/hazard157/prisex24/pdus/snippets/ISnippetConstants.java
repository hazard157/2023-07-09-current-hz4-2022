package com.hazard157.prisex24.pdus.snippets;

import static com.hazard157.prisex24.pdus.snippets.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

import com.hazard157.common.quants.visumple.*;
import com.hazard157.psx.common.stuff.place.*;

/**
 * Unit constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISnippetConstants {

  String UNITID_SNIPPETS = "Snippets"; //$NON-NLS-1$

  String OPID_CATEGORY = "Category"; //$NON-NLS-1$

  IDataDef OPDEF_CATEGORY = DataDef.create( OPID_CATEGORY, STRING, //
      TSID_NAME, STR_SNIPPET_CATEGORY, //
      TSID_DESCRIPTION, STR_SNIPPET_CATEGORY_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IStridablesList<IDataDef> ALL_OPTION_DEFS = new StridablesList<>( //
      DDEF_NAME, //
      DDEF_DESCRIPTION, //
      IPlaceableConstants.DDEF_PLACE, //
      OPDEF_CATEGORY, //
      IVisumpleConstants.OPDEF_VISUMPLES //
  );

}
