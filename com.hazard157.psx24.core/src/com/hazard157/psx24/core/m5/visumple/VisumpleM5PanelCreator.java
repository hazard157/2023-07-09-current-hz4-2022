package com.hazard157.psx24.core.m5.visumple;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;

import com.hazard157.lib.core.quants.visumple.*;

/**
 * Создатель панелей для модели {@link VisumpleM5Model}.
 *
 * @author hazard157
 */
public class VisumpleM5PanelCreator
    extends M5DefaultPanelCreator<Visumple> {

  /**
   * Конструктор.
   */
  public VisumpleM5PanelCreator() {
    // nop
  }

  @Override
  protected IM5EntityPanel<Visumple> doCreateEntityEditorPanel( ITsGuiContext aContext,
      IM5LifecycleManager<Visumple> aLifecycleManager ) {
    return new VisumpleEditPanel( aContext, model(), false );
  }

  @Override
  protected IM5EntityPanel<Visumple> doCreateEntityViewerPanel( ITsGuiContext aContext ) {
    return new VisumpleEditPanel( aContext, model(), true );
  }

  @Override
  protected IM5EntityPanel<Visumple> doCreateEntityDetailsPanel( ITsGuiContext aContext ) {
    return new VisumpleDetailsPanel( aContext, model() );
  }

  @Override
  protected IM5CollectionPanel<Visumple> doCreateCollChecksPanel( ITsGuiContext aContext,
      IM5ItemsProvider<Visumple> aItemsProvider ) {
    ITsGuiContext ctx = new TsGuiContext( aContext );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_REORDER.setValue( ctx.params(), AV_TRUE );
    return super.doCreateCollChecksPanel( ctx, aItemsProvider );
  }

  @Override
  protected IM5CollectionPanel<Visumple> doCreateCollViewerPanel( ITsGuiContext aContext,
      IM5ItemsProvider<Visumple> aItemsProvider ) {
    return new VisumpleCollViewerPanel( aContext, model(), aItemsProvider );
  }

}
