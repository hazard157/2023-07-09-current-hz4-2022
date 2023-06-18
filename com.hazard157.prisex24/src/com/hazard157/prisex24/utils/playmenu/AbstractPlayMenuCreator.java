package com.hazard157.prisex24.utils.playmenu;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.utils.playmenu.IPsxResources.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;

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
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.glib.dialogs.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Creates play selected item drop-down menu for the action {@link IHzConstants#ACDEF_PLAY_MENU}.
 *
 * @author hazard157
 */
public abstract class AbstractPlayMenuCreator
    extends AbstractMenuCreator
    implements IPsxGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   * <p>
   * The following actions must be handled by the specified handler:
   * <ul>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_CREATE} - create GIF at selected frame;</li>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_RECREATE_ALL} - recreate all GIFs in the speicifed context;</li>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_REMOVE} - remove specified GIF. Called only if the selected frame is an
   * animated GIF.</li>
   * </ul>
   *
   * @param aContext - {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractPlayMenuCreator( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private EThumbSize readThumbSizeFromAppSettings() {
    return APPREF_THUMB_SIZE_IN_MENUS.getValue( prefBundle( PBID_PSX24_COMMON ).prefs() ).asValobj();
  }

  /**
   * Displays a dialog for entering parameters for playing the episode.
   *
   * @param aInitialSvin {@link Svin} - inititla value
   * @return {@link Svin} - selected value or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // AbstractMenuCreator
  //

  @SuppressWarnings( "unused" )
  @Override
  protected boolean fillMenu( Menu aMenu ) {
    final Svin svin = getPlayableSvin();
    if( svin == null ) {
      return false;
    }
    IStringMap<ISourceVideo> svs = unitSourceVideos().episodeSourceVideos( svin.episodeId() );
    if( svs.size() < 1 ) {
      return false;
    }
    IEpisode e = unitEpisodes().items().findByKey( svin.episodeId() );
    if( e == null ) {
      return false;
    }
    MenuItem mItem;
    EThumbSize thumbSize = readThumbSizeFromAppSettings();
    int spotlightSec = svin.frame().secNo();
    for( final String camId : svs.keys() ) {
      mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( camId );
      ISourceVideo sv = svs.getByKey( camId );
      // выберем кадр для отображения в качестве значка пункта меню
      IFrame itemIconFrame = new Frame( svin.episodeId(), camId, spotlightSec * FPS, false );
      TsImage mi = psxService().findThumb( itemIconFrame, thumbSize );
      if( mi != null ) {
        mItem.setImage( mi.image() );
      }
      final Svin itemSvin = new Svin( svin.episodeId(), camId, svin.interval() );
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          psxService().playEpisodeVideo( itemSvin );
        }
      } );
    }
    // "Choose"
    new MenuItem( aMenu, SWT.SEPARATOR );
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( STR_MENU_TEXT_SELECT_PLAY_PARAMS );
    mItem.addSelectionListener( new SelectionListenerAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        Svin svin2 = select( svin );
        if( svin2 != null ) {
          psxService().playEpisodeVideo( svin2 );
        }
      }
    } );
    // "View frames"
    mItem = new MenuItem( aMenu, SWT.SEPARATOR );
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( STR_MENU_SHOW_FRAMES );
    mItem.addSelectionListener( new SelectionListenerAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        int frameNo = (svin.interval().start() + svin.interval().duration() / 2) * FPS;
        IFrame initFrame = new Frame( svin.episodeId(), svin.cameraId(), frameNo, false );
        DialogWorkWithFrames.open( tsContext, initFrame );
      }
    } );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Returns SVIN to be played.
   * <p>
   * {@link Svin#episodeId()} and {@link Svin#cameraId()} determines source video to play during interval
   * {@link Svin#interval()}. {@link Svin#frame()} determines time moment to display menu icons.
   *
   * @return {@link Svin} - SVIN to play or <code>null</code>
   */
  protected abstract Svin getPlayableSvin();

}
