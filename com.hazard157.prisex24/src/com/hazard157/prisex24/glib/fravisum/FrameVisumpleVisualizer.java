package com.hazard157.prisex24.glib.fravisum;

import static com.hazard157.prisex24.glib.fravisum.IPsxFrameVisumplesProviderConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.visumple.*;
import com.hazard157.common.quants.visumple.impl.*;
import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;

/**
 * {@link ITsVisualsProvider} implementation for {@link PsxFrameVisumplesProvider}.
 *
 * @author hazard157
 */
public class FrameVisumpleVisualizer
    extends VisumpleVisualizerBase
    implements IPsxGuiContextable {

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FrameVisumpleVisualizer( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IFrame frame( Visumple aVisumple ) {
    return OPDEF_VP_FRAME.getValue( aVisumple.params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // VisumpleVisualizerBase
  //

  @Override
  protected String doGetName( Visumple aItem ) {
    return HhMmSsFfUtils.mmssff( frame( aItem ).frameNo() );
  }

  @Override
  protected String doGetDescription( Visumple aItem ) {
    return frame( aItem ).toString();
  }

  @Override
  protected Image doGetIcon( Visumple aItem, EIconSize aIconSize ) {
    return super.doGetIcon( aItem, aIconSize );
  }

  @Override
  protected TsImage doGetThumb( Visumple aItem, EThumbSize aThumbSize ) {
    return psxService().findThumb( frame( aItem ), aThumbSize );
  }

}
