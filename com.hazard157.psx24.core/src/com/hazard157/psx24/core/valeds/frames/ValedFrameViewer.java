package com.hazard157.psx24.core.valeds.frames;

import static com.hazard157.psx24.core.valeds.frames.ValedFrameFactory.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Просмотрщик поля {@link IFrameable#frame()}.
 *
 * @author goga
 */
public class ValedFrameViewer
    extends AbstractValedControl<IFrame, Control> {

  private final IPdwWidget     imageWidget;
  private final IPsxFileSystem fileSystem;
  private IFrame               frame = IFrame.NONE;

  /**
   * Конструктор.
   *
   * @param aTsContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedFrameViewer( ITsGuiContext aTsContext ) {
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
  protected IFrame doGetUnvalidatedValue() {
    return frame;
  }

  @Override
  protected void doSetUnvalidatedValue( IFrame aValue ) {
    frame = aValue != null ? aValue : IFrame.NONE;
    EThumbSize thumbSize = PI_THUMB_SIZE.getValue( params() ).asValobj();
    imageWidget.setTsImage( fileSystem.findThumb( frame, thumbSize ) );
    imageWidget.redraw();
  }

  @Override
  protected void doClearValue() {
    frame = IFrame.NONE;
    imageWidget.setTsImage( null );
  }

}
