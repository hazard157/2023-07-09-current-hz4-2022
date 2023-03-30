package com.hazard157.psx24.explorer.gui.resview;

import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx24.core.m5.svin.*;

/**
 * Отображение результатов как простого списка (таблицы) элементов типа {@link Svin}.
 *
 * @author goga
 */
public class ResultsPanelAsSimpleList
    extends AbstractResultsPanel {

  private final IM5ItemsProvider<Svin> itemsProvider = () -> getPqResults().listAllSvins();

  private final SvinM5Mpc panel;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ResultsPanelAsSimpleList( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    IM5Model<Svin> model = m5().getModel( MID_SVIN, Svin.class );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_SUMMARY_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_TRUE );
    OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( EIconSize.IS_64X64 ) );
    panel = new SvinM5Mpc( ctx, model, itemsProvider, null );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionChangeEventHelper );
  }

  @Override
  protected void refresh() {
    panel.refresh();
    panel.tree().columnManager().columns().values().get( 0 ).pack();
    panel.tree().columnManager().columns().values().get( 1 ).pack();
  }

  @Override
  public Svin selectedItem() {
    return panel.selectedItem();
  }

  @Override
  public void setSelectedItem( Svin aItem ) {
    panel.setSelectedItem( aItem );
  }

  @Override
  public IList<Svin> getSelectionSvins() {
    return panel.getSelectedNodeSvins();
  }

}
