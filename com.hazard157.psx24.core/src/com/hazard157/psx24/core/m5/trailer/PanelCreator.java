package com.hazard157.psx24.core.m5.trailer;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.m5.trailer.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.m5.trailer.TrailerTreeMakerUtils.*;

/**
 * Создание панелей.
 *
 * @author hazard157
 */
class PanelCreator
    extends M5DefaultPanelCreator<Trailer> {

  MultiPaneComponentModown<Trailer> createMpc( ITsGuiContext aContext, IM5ItemsProvider<Trailer> aItemsProvider,
      IM5LifecycleManager<Trailer> aLifecycleManager ) {
    aContext.params().setBool( OPDEF_IS_SUPPORTS_TREE, true );
    MultiPaneComponentModown<Trailer> mpc =
        new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager ) {

          @Override
          protected ITsToolbar doCreateToolbar( ITsGuiContext aCtx, String aName, EIconSize aIconSize,
              IListEdit<ITsActionDef> aActs ) {
            aActs.add( ACDEF_SEPARATOR );
            aActs.add( AI_PLAY );
            return super.doCreateToolbar( aCtx, aName, aIconSize, aActs );
          }

          @Override
          protected void doProcessAction( String aActionId ) {
            switch( aActionId ) {
              case AID_PLAY: {
                Trailer sel = selectedItem();
                IPsxFileSystem fs = tsContext().get( IPsxFileSystem.class );
                if( sel != null && (fs.findTrailerFile( sel.id() ) != null) ) {
                  IPrisexService pss = tsContext().get( IPrisexService.class );
                  pss.playTrailer( sel.id() );
                }
                break;
              }
              default:
                throw new TsNotAllEnumsUsedRtException();
            }
          }

          @Override
          protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, Trailer aSel ) {
            super.doUpdateActionsState( aIsAlive, aIsSel, aSel );
            IPsxFileSystem fs = tsContext().get( IPsxFileSystem.class );
            toolbar().setActionEnabled( AID_PLAY, aIsSel && (fs.findTrailerFile( aSel.id() ) != null) );
          }

        };
    // настройка режимов дерева
    TreeModeInfo<Trailer> tmi = new TreeModeInfo<>( TrailerTreeMakerUtils.NK_EPISODE.id(), STR_N_TREE_MODE_BY_EPISODE,
        STR_D_TREE_MODE_BY_EPISODE, null, new GroupByEpisode( aContext ) );
    mpc.treeModeManager().addTreeMode( tmi );
    tmi = new TreeModeInfo<>( TrailerTreeMakerUtils.NK_NAME.id(), STR_N_TREE_MODE_BY_NAME, STR_D_TREE_MODE_BY_NAME,
        null, new GroupByName( aContext ) );
    mpc.treeModeManager().addTreeMode( tmi );
    return mpc;
  }

  @Override
  protected IM5CollectionPanel<Trailer> doCreateCollEditPanel( ITsGuiContext aContext,
      IM5ItemsProvider<Trailer> aItemsProvider, IM5LifecycleManager<Trailer> aLifecycleManager ) {
    return new M5CollectionPanelMpcModownWrapper<>( createMpc( aContext, aItemsProvider, aLifecycleManager ), false );
  }

  @Override
  protected IM5CollectionPanel<Trailer> doCreateCollViewerPanel( ITsGuiContext aContext,
      IM5ItemsProvider<Trailer> aItemsProvider ) {
    return new M5CollectionPanelMpcModownWrapper<>( createMpc( aContext, aItemsProvider, null ), true );
  }

}
