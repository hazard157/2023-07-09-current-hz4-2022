package com.hazard157.prisex24.glib.dialogs;

import static com.hazard157.prisex24.glib.dialogs.IPsxResources.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Dialog to select an episode.
 *
 * @author hazard157
 */
public class DialogSelectSingleEpisodeId {

  private static class Panel
      extends AbstractTsDialogPanel<String, ITsGuiContext>
      implements IPsxGuiContextable {

    static final EThumbSize EP_THUMB_SIZE = EThumbSize.SZ360;

    private final ITsSelectionChangeListener<IEpisode> selectionChangeListener = new ITsSelectionChangeListener<>() {

      @Override
      public void onTsSelectionChanged( Object aSource, IEpisode aSelectedItem ) {
        if( aSelectedItem == null ) {
          imgViewer.setTsImage( null );
        }
        else {
          TsImage mi = psxService().findThumb( aSelectedItem.frame(), EP_THUMB_SIZE );
          imgViewer.setTsImage( mi );
        }
        imgViewer.redraw();
      }
    };

    final IM5Model<IEpisode>           model;
    final IM5CollectionPanel<IEpisode> panel;
    final IPdwWidget                   imgViewer;

    Panel( Composite aParent, TsDialog<String, ITsGuiContext> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      model = m5().getModel( MID_EPISODE, IEpisode.class );
      ITsGuiContext ctx = new TsGuiContext( tsContext() );
      OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
      OPDEF_IS_TOOLBAR.setValue( ctx.params(), AV_FALSE );
      OPDEF_DBLCLICK_ACTION_ID.setValue( ctx.params(), AV_STR_EMPTY );
      OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), avValobj( EThumbSize.SZ180 ) );
      OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( EIconSize.IS_64X64 ) );
      IM5LifecycleManager<IEpisode> lm = model.getLifecycleManager( null );
      panel = model.panelCreator().createCollViewerPanel( ctx, lm.itemsProvider() );
      aParent.setLayout( new BorderLayout() );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.WEST );
      panel.addTsSelectionListener( selectionChangeListener );
      panel.addTsDoubleClickListener( ( aSource, aSelectedItem ) -> ownerDialog().closeDialog( ETsDialogCode.OK ) );
      // imgViewer
      imgViewer = new PdwWidgetSimple( ctx );
      imgViewer.createControl( this );
      imgViewer.getControl().setLayoutData( BorderLayout.CENTER );
      imgViewer.setAreaPreferredSize( EP_THUMB_SIZE.pointSize() );
      imgViewer.setFitInfo( RectFitInfo.BEST );
      imgViewer.setFulcrum( ETsFulcrum.CENTER );
      imgViewer.setPreferredSizeFixed( true );
      imgViewer.addTsUserInputListener( new ITsUserInputListener() {

        @Override
        public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
            Control aWidget ) {
          ownerDialog().closeDialog( ETsDialogCode.OK );
          return true;
        }

        @Override
        public boolean onMouseWheel( Object aSource, int aState, ITsPoint aCoors, Control aWidget, int aScrollLines ) {
          IEpisode sel = panel.selectedItem();
          IEpisode toSel;
          if( aScrollLines < 0 ) {
            toSel = ETsCollMove.NEXT.findElemAtWni( sel, panel.items(), 5, true );
          }
          else {
            toSel = ETsCollMove.PREV.findElemAtWni( sel, panel.items(), 5, true );
          }
          panel.setSelectedItem( toSel );
          return true;
        }
      } );
    }

    @Override
    protected void doSetDataRecord( String aData ) {
      IUnitEpisodes ue = tsContext().get( IUnitEpisodes.class );
      IEpisode e = aData != null ? ue.items().findByKey( aData ) : null;
      panel.setSelectedItem( e );
    }

    @Override
    protected String doGetDataRecord() {
      IEpisode e = panel.selectedItem();
      return e != null ? e.id() : null;
    }

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Show episode selection dialog.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aSelEpisodeId String - initially selected episode or <code>null</code>
   * @return String -selected episode ID or <code>null</code>
   */
  public static final String select( ITsGuiContext aContext, String aSelEpisodeId ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<String, ITsGuiContext> creator = Panel::new;
    TsDialogInfo cdi = new TsDialogInfo( aContext, STD_C_DIALOG_SELECT_EPISODE, STD_T_DIALOG_SELECT_EPISODE );
    cdi.setMinSizeShellRelative( 70, 80 );
    TsDialog<String, ITsGuiContext> d = new TsDialog<>( cdi, aSelEpisodeId, aContext, creator );
    return d.execData();
  }

}
