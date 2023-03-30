package com.hazard157.psx24.films.e4.uiparts;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.films.IPsx24FilmsConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.e4.services.mps.*;
import com.hazard157.psx24.films.e4.services.*;
import com.hazard157.psx24.films.m5.*;

/**
 * Вью списка фильмов.
 *
 * @author goga
 */
public class UipartFilmsList
    extends MwsAbstractPart {

  class Mpc
      extends MultiPaneComponentModown<File> {

    public Mpc( ITsGuiContext aContext, IM5Model<File> aModel, IM5ItemsProvider<File> aItemsProvider ) {
      super( aContext, aModel, aItemsProvider, null );
      OPDEF_DBLCLICK_ACTION_ID.setValue( tsContext().params(), avStr( AID_PLAY ) );
      addTsSelectionListener( ( aSource, aSelectedItem ) -> currentFilmService.setCurrent( aSelectedItem ) );
    }

    @Override
    protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
        IListEdit<org.toxsoft.core.tsgui.bricks.actions.ITsActionDef> aActs ) {
      aActs.addAll( ACDEF_REFRESH, AI_SHOW_LEGACY_FILMS, ACDEF_SEPARATOR, AI_PLAY, ACDEF_SEPARATOR, AI_RUN_KDENLIVE );
      return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
    }

    @Override
    protected void doProcessAction( String aActionId ) {
      File sel = selectedItem();
      switch( aActionId ) {
        case AID_PLAY:
          if( sel != null ) {
            IMediaPlayerService mps = tsContext().get( IMediaPlayerService.class );
            boolean isFullScreen = OP_SHOW_FILM_FULLSCREEN.getValue( filmsService.modulePrefs().prefs() ).asBool();
            mps.playVideoFile( sel, isFullScreen );
          }
          break;
        case AID_RUN_KDENLIVE: {
          if( sel != null ) {
            File kproj = filmsService.kdenliveProjectFile( sel );
            if( kproj.exists() ) {
              TsMiscUtils.runProgram( PROGRAM_KDENLIVE, kproj.getAbsolutePath() );
            }
          }
          break;
        }
        case AID_SHOW_LEGACY_FILMS: {
          boolean isShown = OP_SHOW_LEGACY_FILMS.getValue( filmsService.modulePrefs().prefs() ).asBool();
          filmsService.modulePrefs().prefs().setBool( OP_SHOW_LEGACY_FILMS, !isShown );
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }

    @Override
    protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, File aSel ) {
      toolbar().setActionEnabled( AID_PLAY, aIsSel );
      boolean isShown = OP_SHOW_LEGACY_FILMS.getValue( filmsService.modulePrefs().prefs() ).asBool();
      toolbar().setActionChecked( AID_SHOW_LEGACY_FILMS, isShown );
      boolean isKdenliveProj = false;
      if( aSel != null ) {
        File kproj = filmsService.kdenliveProjectFile( aSel );
        isKdenliveProj = kproj.exists();
      }
      toolbar().setActionEnabled( AID_RUN_KDENLIVE, isKdenliveProj );
    }

  }

  private final ITsCollectionChangeListener prefsChangeListener = ( aSource, aOp, aItem ) -> {
    if( this.panel != null ) {
      this.panel.refresh();
    }
  };

  private final IGenericChangeListener fsChangeListener = aSource -> {
    if( this.panel != null ) {
      this.panel.refresh();
    }
  };

  @Inject
  ICurrentFilmService currentFilmService;

  @Inject
  IPsxFilmsService filmsService;

  IM5CollectionPanel<File> panel;

  @Override
  protected void doInit( Composite aParent ) {
    filmsService.modulePrefs().prefs().addCollectionChangeListener( prefsChangeListener );
    filmsService.fsChangeEventProducer().addListener( fsChangeListener );
    IM5Model<File> m5model = m5().getModel( FilmM5Model.MODEL_ID, File.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_SUMMARY_PANE.setValue( ctx.params(), AV_TRUE );
    IM5LifecycleManager<File> lm = m5model.getLifecycleManager( null );
    MultiPaneComponentModown<File> mpc = new Mpc( ctx, m5model, lm.itemsProvider() );
    mpc.setLifecycleManager( lm );
    panel = new M5CollectionPanelMpcModownWrapper<>( mpc, true );
    panel.createControl( aParent );
    panel.addTsSelectionListener( ( aSource, aSelectedItem ) -> currentFilmService.setCurrent( aSelectedItem ) );
  }

}
