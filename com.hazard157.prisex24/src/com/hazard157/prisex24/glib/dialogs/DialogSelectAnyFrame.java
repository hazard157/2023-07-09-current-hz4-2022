package com.hazard157.prisex24.glib.dialogs;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;

import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Dialog to select any frame.
 * <p>
 * Contains {@link IAllFramesSelector} panel.
 *
 * @author hazard157
 */
public class DialogSelectAnyFrame {

  static class DialogPanel
      extends AbstractTsDialogPanel<IFrame, Object> {

    private final IAllFramesSelector framesSelector;

    DialogPanel( Composite aParent, TsDialog<IFrame, Object> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      framesSelector = new AllFramesSelector( tsContext() );
      framesSelector.createControl( this );
      framesSelector.getControl().setLayoutData( BorderLayout.CENTER );
    }

    @Override
    protected void doSetDataRecord( IFrame aData ) {
      framesSelector.setSelectedItem( aData );
    }

    @Override
    protected IFrame doGetDataRecord() {
      return framesSelector.selectedItem();
    }

  }

  /**
   * Invokes frame selection dialog with {@link IAllFramesSelector} panel.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aInitial {@link IFrame} - initially selected frame or <code>null</code>
   * @return {@link IFrame} - selected frame or <code>null</code>
   */
  public static IFrame select( ITsGuiContext aContext, IFrame aInitial ) {
    ITsDialogInfo di = TsDialogInfo.forSelectEntity( aContext );
    IDialogPanelCreator<IFrame, Object> creator = DialogPanel::new;
    TsDialog<IFrame, Object> d = new TsDialog<>( di, aInitial, null, creator );
    return d.execData();
  }

}
