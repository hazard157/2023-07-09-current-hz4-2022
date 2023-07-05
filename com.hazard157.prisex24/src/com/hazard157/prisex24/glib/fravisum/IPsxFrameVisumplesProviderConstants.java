package com.hazard157.prisex24.glib.fravisum;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

import com.hazard157.common.quants.visumple.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * The package constants.
 *
 * @author hazard157
 */
public interface IPsxFrameVisumplesProviderConstants {

  /**
   * VISUMPLE provider kind ID.
   */
  String VPKIND_ID = "PsxFrame"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_VP_FRAME}.
   */
  String OPID_VP_FRAME = PSX_ID + ".Frame"; //$NON-NLS-1$

  /**
   * Option for {@link Visumple#params()}: holds {@link IFrame} that provides the image.
   */
  IDataDef OPDEF_VP_FRAME = DataDef.create( OPID_VP_FRAME, VALOBJ, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_KEEPER_ID, Frame.KEEPER_ID, //
      TSID_DEFAULT_VALUE, Frame.AV_FRAME_NONE //
  );

}
