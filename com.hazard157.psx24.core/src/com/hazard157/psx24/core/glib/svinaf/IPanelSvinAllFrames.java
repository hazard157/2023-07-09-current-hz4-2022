package com.hazard157.psx24.core.glib.svinaf;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.generic.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Displays all frames of the svin by cameras.
 *
 * @author hazard157
 */
public interface IPanelSvinAllFrames
    extends IGenericCollPanel<IFrame>, IThumbSizeableEx {

  /**
   * returns current selected {@link Svin}.
   *
   * @return {@link Svin} - {@link Svin} or <code>null</code>
   */
  Svin getSvin();

  /**
   * Sets SVIN to show frames for.
   *
   * @param aSvin {@link Svin} - {@link Svin} or <code>null</code>
   */
  void setSvin( Svin aSvin );

}
