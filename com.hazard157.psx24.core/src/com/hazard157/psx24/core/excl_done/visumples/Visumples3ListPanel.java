package com.hazard157.psx24.core.excl_done.visumples;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.glib.pgviewer.*;
import com.hazard157.lib.core.glib.pgviewer.impl.*;
import com.hazard157.lib.core.quants.visumple3.*;

/**
 * Panel to edit list of {@link Visumple3}.
 *
 * @author hazard157
 */
public class Visumples3ListPanel
    extends TsStdEventsProducerPanel<Visumple3>
    implements IGenericChangeEventCapable, ITsContextable, ITsActionHandler {

  private final ITsToolbar                 toolbar;
  private final IPicsGridViewer<Visumple3> pgViewer;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Visumples3ListPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    // toolbar
    TsGuiContext ctx = new TsGuiContext( tsContext() );
    toolbar = TsToolbar.create( this, ctx, //
        ACDEF_ADD, ACDEF_REMOVE, ACDEF_SEPARATOR, //
        AI_THUMB_SIZEABLE_ZOOM_MENU ///
    );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( this );

    // pgViewer
    ctx = new TsGuiContext( tsContext() );
    pgViewer = new PicsGridViewer<>( this, ctx );
    pgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    pgViewer.addTsSelectionListener( ( aSrc, aSel ) -> updateActionsState() );
    pgViewer.addTsSelectionListener( selectionChangeEventHelper );

    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateActionsState() {
    Visumple3 sel = selectedItem();
    boolean isSel = sel != null;
    toolbar.setActionEnabled( ACTID_REMOVE, isSel );
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ADD: {
        // TODO Visumples3ListPanel.onToolButtonPressed()
        break;
      }
      case ACTID_REMOVE: {
        // TODO Visumples3ListPanel.onToolButtonPressed()
        break;
      }
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        // TODO Visumples3ListPanel.onToolButtonPressed()
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aActionId );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public Visumple3 selectedItem() {
    return pgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( Visumple3 aItem ) {
    pgViewer.setSelectedItem( aItem );
  }

  // public IList<Visumple3> items() {
  // return pgViewer.items();
  // }
  //
  // public void refresh() {
  // pgViewer.refresh();
  // }
  //
  // public ITsCheckSupport<Visumple3> checkSupport() {
  // return ITsCheckSupport.NONE;
  // }

}
