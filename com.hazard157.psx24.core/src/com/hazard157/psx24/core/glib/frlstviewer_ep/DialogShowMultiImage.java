package com.hazard157.psx24.core.glib.frlstviewer_ep;

import static com.hazard157.psx24.core.glib.frlstviewer_ep.IPsxResources.*;
import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Диалог показа заданного {@link TsImage}.
 *
 * @author hazard157
 */
public class DialogShowMultiImage {

  static class Panel
      extends AbstractTsDialogPanel<TsImage, ITsGuiContext> {

    final IPdwWidget imageWidget;

    protected Panel( Composite aParent, TsDialog<TsImage, ITsGuiContext> aOwnerDialog, TsImage aImage ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      imageWidget = new PdwWidgetSimple( environ() );
      imageWidget.createControl( this );
      imageWidget.getControl().setLayoutData( BorderLayout.CENTER );
      imageWidget.setTsImage( aImage );
    }

    @Override
    protected void doSetDataRecord( TsImage aData ) {
      imageWidget.setTsImage( aData );
      this.getParent().layout( true, true );
    }

    @Override
    protected TsImage doGetDataRecord() {
      return imageWidget.getTsImage();
    }

  }

  /**
   * Показывает изображение в диалоге.
   *
   * @param aTsContext {@link ITsGuiContext} - контекст
   * @param aImage {@link TsImage} - изображение, может быть <code>null</code>
   * @param aCaption String - название диалога или <code>null</code> для умолчания
   * @param aTitle String - текст в диалоге или <code>null</code> для пустой строки
   */
  public static final void showImage( ITsGuiContext aTsContext, TsImage aImage, String aCaption, String aTitle ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    Shell shell = aTsContext.get( Shell.class );
    if( aImage == null ) {
      TsDialogUtils.warn( shell, MSG_WARN_NULL_MULTI_IMAGE_TO_SHOW );
      return;
    }
    IDialogPanelCreator<TsImage, ITsGuiContext> creator =
        ( aParent, aOwnerDialog ) -> new Panel( aParent, aOwnerDialog, aImage );
    String caption = DLG_C_SHOW_MULTI_IMAGE;
    if( aCaption != null && !aCaption.isEmpty() ) {
      caption = aCaption;
    }
    String title = aTitle != null ? aTitle : TsLibUtils.EMPTY_STRING;
    ITsDialogInfo tdi = new TsDialogInfo( aTsContext, shell, caption, title, DF_NO_APPROVE );
    TsDialog<TsImage, ITsGuiContext> d = new TsDialog<>( tdi, aImage, aTsContext, creator );
    d.execDialog();
  }

}
