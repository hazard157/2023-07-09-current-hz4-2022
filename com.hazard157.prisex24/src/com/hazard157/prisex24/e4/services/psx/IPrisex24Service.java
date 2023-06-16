package com.hazard157.prisex24.e4.services.psx;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Application-specific methods.
 *
 * @author hazard157
 */
public interface IPrisex24Service {

  /**
   * Finds frame file and loads thumbnail image.
   *
   * @param aFrame {@link IFrame} - the frame
   * @param aThumbSize {@link EThumbSize} - asked size of the thumbnail
   * @return {@link TsImage} - loaded thumbnail or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize );

}
