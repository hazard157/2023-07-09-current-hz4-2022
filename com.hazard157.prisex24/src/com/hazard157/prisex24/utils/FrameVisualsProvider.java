package com.hazard157.prisex24.utils;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.dialogs.*;
import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IDialogShowImageFilesVisualsProvider} implementation for {@link IFrame}.
 * <p>
 * Obviously also implements {@link ITsVisualsProvider}.
 *
 * @author hazard157
 */
public class FrameVisualsProvider
    implements IPsxGuiContextable, IDialogShowImageFilesVisualsProvider<IFrame> {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FrameVisualsProvider( ITsGuiContext aContext ) {
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
  // ITsVisualsProvider
  //

  @Override
  public String getName( IFrame aItem ) {
    return aItem != null ? aItem.toString() : TsLibUtils.EMPTY_STRING;
  }

  @Override
  public String getDescription( IFrame aItem ) {
    return aItem != null ? aItem.toString() : TsLibUtils.EMPTY_STRING;
  }

  @Override
  public Image getIcon( IFrame aItem, EIconSize aIconSize ) {
    if( aItem == null ) {
      return null;
    }
    EThumbSize thumbSize = EThumbSize.findIncluding( aIconSize );
    TsImage tsImg = psxService().findThumb( aItem, thumbSize );
    if( tsImg != null ) {
      return tsImg.image();
    }
    return null;
  }

  @Override
  public TsImage getThumb( IFrame aItem, EThumbSize aThumbSize ) {
    return aItem != null ? psxService().findThumb( aItem, aThumbSize ) : null;
  }

  // ------------------------------------------------------------------------------------
  // IDialogShowImageFilesVisualsProvider
  //

  @Override
  public TsImage getFullSizeImage( IFrame aItem ) {
    File f = cofsFrames().findFrameFile( aItem );
    if( f.exists() ) {
      return imageManager().findImage( f );
    }
    return null;
  }

}
