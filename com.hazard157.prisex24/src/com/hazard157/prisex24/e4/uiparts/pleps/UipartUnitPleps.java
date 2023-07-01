package com.hazard157.prisex24.e4.uiparts.pleps;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import com.hazard157.prisex24.e4.services.pleps.*;
import com.hazard157.psx.proj3.pleps.*;

/**
 * Вью просмотра и правки {@link IUnitPleps#items()}.
 *
 * @author hazard157
 */
public class UipartUnitPleps
    extends MwsAbstractPart {

  private final ITsSelectionChangeListener<IPlep> selectedPlepChangeListener = ( aSource, aSelectedItem ) -> {
    this.currentStirService.setCurrent( null );
    this.currentPlepService.setCurrent( aSelectedItem );
  };

  @Inject
  IUnitPleps unitPleps;

  @Inject
  ICurrentPlepService currentPlepService;

  @Inject
  ICurrentStirService currentStirService;

  IM5CollectionPanel<IPlep> plepsPanel;

  @Override
  protected void doInit( Composite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5Model<IPlep> plepsModel = m5().getModel( MID_PLEP, IPlep.class );
    IM5LifecycleManager<IPlep> lm = plepsModel.getLifecycleManager( unitPleps );
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    plepsPanel = plepsModel.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    plepsPanel.createControl( aParent );
    plepsPanel.addTsSelectionListener( selectedPlepChangeListener );
  }

}
