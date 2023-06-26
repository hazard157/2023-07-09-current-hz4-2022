package com.hazard157.prisex24.glib.fravisum;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.glib.fravisum.IPsxFrameVisumplesProviderConstants.*;
import static com.hazard157.prisex24.glib.fravisum.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.visumple.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.glib.dialogs.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IVisumplesProvider} implementation providing PRISEX {@link IFrame} as an image.
 *
 * @author hazard157
 */
public class PsxFrameVisumplesProvider
    extends StridableParameterized
    implements IVisumplesProvider, IPsxGuiContextable {

  private final ITsGuiContext                tsContext;
  private final ITsVisualsProvider<Visumple> visualsProvider;

  private IFrame lastFrame = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PsxFrameVisumplesProvider( ITsGuiContext aContext ) {
    super( VPKIND_ID );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    visualsProvider = new FrameVisumpleVisualizer( tsContext );
    params().setStr( TSID_NAME, STR_VP_FRAME_IMAGE );
    params().setStr( TSID_DESCRIPTION, STR_VP_FRAME_IMAGE_D );
    params().setStr( TSID_ICON_ID, ICONID_PORNICON );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVisumplesProvider
  //

  @Override
  public Visumple selectVisumple() {
    IFrame sel = DialogSelectAnyFrame.select( tsContext(), lastFrame );
    if( sel != null ) {
      lastFrame = sel;
      File f = cofsFrames().findFrameFile( sel );
      if( f != null ) {
        IOptionSetEdit params = new OptionSet();
        OPDEF_VP_FRAME.setValue( params, avValobj( sel ) );
        return new Visumple( f.getAbsolutePath(), params );
      }
      throw new TsInternalErrorRtException(); // must not happen!
    }
    return null;
  }

  @Override
  public ITsVisualsProvider<Visumple> visualizer() {
    return visualsProvider;
  }

}
