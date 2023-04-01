package com.hazard157.psx24.planning.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx24.planning.e4.services.*;
import com.hazard157.psx24.planning.m5.*;

/**
 * Вью просмотра и правки свойств текущего {@link ICurrentStirService#current()}.
 *
 * @author hazard157
 */
public class UipartCurrentStir
    extends MwsAbstractPart {

  private final ICurrentEntityChangeListener<IStir> currentStirChangeListener = aCurrent -> updateOnCurrentStir();

  private final IGenericChangeListener panelContentChangeListener = aSource -> whenPanelContentChanged();

  private final ITsActionHandler toolbarListener = this::processAction;

  @Inject
  ICurrentStirService currentStirService;

  TsToolbar toolbar;

  IM5Model<IStir>       model;
  IM5EntityPanel<IStir> panel;

  @Override
  protected void doInit( Composite aParent ) {
    currentStirService.addCurrentEntityChangeListener( currentStirChangeListener );
    model = m5().getModel( StirM5Model.MODEL_ID, IStir.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    // GUI
    aParent.setLayout( new BorderLayout() );
    // toolbar
    toolbar = TsToolbar.create( aParent, tsContext(), EIconSize.IS_24X24, //
        ACDEF_ADD //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // panel
    panel = new M5DefaultEntityViewerPanel<>( ctx, model ) {

      @Override
      protected void doInitEditors() {
        // addField( StirM5Model.DESCRIPTION.id() );
        addField( StirM5Model.VISUMPLES.id() );
      }
    };
    panel.createControl( aParent );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.genericChangeEventer().addListener( panelContentChangeListener );
    updateOnCurrentStir();
  }

  void processAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ADD: {
        TsDialogUtils.underDevelopment( getShell() );
        // GOGA удален плагин com.hazard157.psx5.lib.main
        // ITsGuiContext ctx = TsGuiContext.createInitial( getWindowLevelContext() );
        // IFrame f = DialogFrameSelectionFromAll.select( ctx, null );
        // if( f != null ) {
        // IStir s = currentStirService.current();
        // IListEdit<Visumple> ll = new ElemArrayList<>( s.visumples() );
        // IPsxFileSystem fs = ctx.get( IPsxFileSystem.class );
        // File file = fs.findFrameFile( f );
        // if( file != null ) {
        // ll.add( new Visumple( file.getAbsolutePath(), IOptionSet.NULL ) );
        // }
        // s.setVisumples( ll );
        // panel.setEntity( s );
        // }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  void updateActionsState() {
    boolean isAlive = currentStirService.current() != null;
    toolbar.setActionEnabled( ACTID_ADD, isAlive );
  }

  void updateOnCurrentStir() {
    IStir stir = currentStirService.current();
    if( stir != null ) {
      IM5LifecycleManager<IStir> lm = model.getLifecycleManager( stir.plep() );
      panel.setLifecycleManager( lm );
      panel.setEntity( stir );
    }
    else {
      panel.setLifecycleManager( null );
      panel.setEntity( null );
    }
    updateActionsState();
  }

  void whenPanelContentChanged() {
    IM5Bunch<IStir> vals = panel.getValues();
    panel.lifecycleManager().edit( vals );
    updateActionsState();
  }

}
