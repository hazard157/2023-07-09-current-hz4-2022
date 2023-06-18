package com.hazard157.psx24.core.e4.services.playmenu;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.e4.services.playmenu.IPsxResources.*;
import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.glib.dialogs.imgs.*;

/**
 * Реализация {@link IPlayMenuSupport}.
 *
 * @author hazard157
 */
public class PlayMenuSupport
    implements IPlayMenuSupport {

  private class MenuCreator
      extends AbstractMenuCreator {

    final ITsGuiContext           tsContext;
    final IPlayMenuParamsProvider paramsProvider;
    final IPrisexService          prisexService;
    final IPsxFileSystem          fileSystem;
    final IPrefBundle             prefBundle;

    MenuCreator( ITsGuiContext aContext, IPlayMenuParamsProvider aPlayMenuParamsProvider ) {
      tsContext = aContext;
      paramsProvider = aPlayMenuParamsProvider;
      prisexService = tsContext.get( IPrisexService.class );
      fileSystem = tsContext.get( IPsxFileSystem.class );
      prefBundle = tsContext.get( IPrefBundle.class );
    }

    /**
     * Выводит диалог ввода параметров воспроизведения эпизода.
     *
     * @param aInitialSvin {@link Svin} - начальные значения параметров воспроизведения
     * @return {@link Svin} - параметры воспроизведения или null, если пользователь отказался от ввода
     * @throws TsNullArgumentRtException любой аргумент = null
     */
    Svin select( Svin aInitialSvin ) {
      IM5Domain domain = tsContext.get( IM5Domain.class );
      IM5Model<Svin> model = domain.getModel( MID_SVIN, Svin.class );
      ITsGuiContext ctx = new TsGuiContext( tsContext );
      TsDialogInfo cdi = TsDialogInfo.forEditEntity( ctx );
      Svin svin = M5GuiUtils.askEdit( ctx, model, aInitialSvin, cdi, model.getLifecycleManager( null ) );
      return svin;
    }

    // ------------------------------------------------------------------------------------
    // Реализация методов базового класса
    //

    @SuppressWarnings( "unused" )
    @Override
    protected boolean fillMenu( Menu aMenu ) {
      final Svin svin = paramsProvider.playParams();
      if( svin == null ) {
        return false;
      }
      IStringMap<ISourceVideo> svs = tsContext.get( IUnitSourceVideos.class ).episodeSourceVideos( svin.episodeId() );
      if( svs.size() < 1 ) {
        return false;
      }
      IEpisode e = tsContext.get( IUnitEpisodes.class ).items().findByKey( svin.episodeId() );
      if( e == null ) {
        return false;
      }
      for( final String camId : svs.keys() ) {
        MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
        mItem.setText( camId );
        ISourceVideo sv = svs.getByKey( camId );
        EThumbSize thumbSize = APPRM_THUMBSZ_FRAMES_IN_MENUS.getValue( prefBundle.prefs() ).asValobj();
        // выберем кадр для отображения в качестве значка пункта меню
        IFrame itemIconFrame;
        int spotlightSec = paramsProvider.spotlightSec();
        if( spotlightSec > 0 ) {
          itemIconFrame = new Frame( svin.episodeId(), camId, spotlightSec * FPS, false );
        }
        else {
          itemIconFrame = sv.frame(); // стандартный кадр исходного видео
          // найдем, если сможем, фрейм, наиболее подходящий к этому участку
          IScene bestScene = e.story().findBestSceneFor( svin.interval(), true );
          if( bestScene != null ) {
            if( bestScene.frame().isDefined() ) {
              itemIconFrame = new Frame( svin.episodeId(), camId, bestScene.frame().frameNo(), false );
            }
          }
        }
        TsImage mi = fileSystem.findThumb( itemIconFrame, thumbSize );
        if( mi != null ) {
          mItem.setImage( mi.image() );
        }
        final Svin itemSvin = new Svin( svin.episodeId(), camId, svin.interval() );
        mItem.addSelectionListener( new SelectionListenerAdapter() {

          @Override
          public void widgetSelected( SelectionEvent aEvent ) {
            prisexService.playEpisodeVideo( itemSvin );
          }
        } );
      }
      // "Выбрать"
      new MenuItem( aMenu, SWT.SEPARATOR );
      MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( STR_MENU_TEXT_SELECT_PLAY_PARAMS );
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          Svin svin2 = select( svin );
          if( svin2 != null ) {
            prisexService.playEpisodeVideo( svin2 );
          }
        }
      } );
      // "Просмотр кадров"
      new MenuItem( aMenu, SWT.SEPARATOR );
      mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( STR_MENU_SHOW_FRAMES );
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          int frameNo = (svin.interval().start() + svin.interval().duration() / 2) * FPS;
          IFrame initFrame = new Frame( svin.episodeId(), svin.cameraId(), frameNo, false );
          DialogWorkWithFrames.openEpisode( tsContext, initFrame, e );
        }

      } );
      return true;
    }

  }

  /**
   * Пустой конструктор.
   */
  public PlayMenuSupport() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPlayMenuSupport
  //

  @Override
  public IMenuCreator getPlayMenuCreator( ITsGuiContext aContext, IPlayMenuParamsProvider aParamsProvider ) {
    TsNullArgumentRtException.checkNulls( aContext, aParamsProvider );
    return new MenuCreator( aContext, aParamsProvider );
  }

}
