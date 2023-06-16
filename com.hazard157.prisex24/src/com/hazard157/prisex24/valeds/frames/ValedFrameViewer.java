package com.hazard157.prisex24.valeds.frames;

import static com.hazard157.prisex24.valeds.frames.ValedFrameFactory.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.e4.services.psx.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IFrameable#frame()} viewer.
 *
 * @author hazard157
 */
public class ValedFrameViewer
    extends AbstractValedControl<IFrame, Control> {

  private final IPdwWidget       imageWidget;
  private final IPrisex24Service psxServ;
  private IFrame                 frame = IFrame.NONE;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedFrameViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
    psxServ = tsContext().get( IPrisex24Service.class );
    imageWidget = new PdwWidgetSimple( tsContext() );
    EThumbSize thumbSize = OPDEF_THUMB_SIZE.getValue( params() ).asValobj();
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
    EThumbSize thumbSize = OPDEF_THUMB_SIZE.getValue( params() ).asValobj();
    imageWidget.setTsImage( psxServ.findThumb( frame, thumbSize ) );
    imageWidget.redraw();
  }

  @Override
  protected void doClearValue() {
    frame = IFrame.NONE;
    imageWidget.setTsImage( null );
  }

}
