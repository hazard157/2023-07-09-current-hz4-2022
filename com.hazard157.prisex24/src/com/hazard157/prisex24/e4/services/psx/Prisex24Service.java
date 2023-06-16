package com.hazard157.prisex24.e4.services.psx;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IPrisex24Service} implementation.
 *
 * @author hazard157
 */
public class Prisex24Service
    implements IPrisex24Service, IPsxGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Prisex24Service( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IPrisex24Service
  //

  @Override
  public TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFrame, aThumbSize );
    File frameFile = cofsFrames().findFrameFile( aFrame );
    if( frameFile == null ) {
      return null;
    }
    return imageManager().findThumb( frameFile, aThumbSize );
  }

}
