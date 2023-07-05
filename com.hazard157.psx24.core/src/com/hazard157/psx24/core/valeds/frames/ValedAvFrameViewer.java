package com.hazard157.psx24.core.valeds.frames;

import static com.hazard157.psx24.core.valeds.frames.ValedFrameFactory.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Просмотрщик поля {@link IFrameable#frame()}.
 *
 * @author hazard157
 */
public class ValedAvFrameViewer
    extends AbstractValedControl<IAtomicValue, Control> {

  private final IPdwWidget     imageWidget;
  private final IPsxFileSystem fileSystem;
  private IAtomicValue         avFrame = Frame.AV_FRAME_NONE;

  /**
   * Конструктор.
   *
   * @param aTsContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedAvFrameViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    imageWidget = new PdwWidgetSimple( tsContext() );
    EThumbSize thumbSize = PI_THUMB_SIZE.getValue( params() ).asValobj();
    imageWidget.setAreaPreferredSize( thumbSize.pointSize() );
    imageWidget.setPreferredSizeFixed( true );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    imageWidget.createControl( aParent );
    return imageWidget.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return avFrame;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    avFrame = aValue != null ? aValue : Frame.AV_FRAME_NONE;
    EThumbSize thumbSize = PI_THUMB_SIZE.getValue( params() ).asValobj();
    IFrame frame = avFrame.asValobj();
    if( frame != null ) {
      imageWidget.setTsImage( fileSystem.findThumb( frame, thumbSize ) );
    }
    else {
      imageWidget.setTsImage( null );
    }
    imageWidget.redraw();
  }

  @Override
  protected void doClearValue() {
    avFrame = Frame.AV_FRAME_NONE;
    imageWidget.setTsImage( null );
  }

}
