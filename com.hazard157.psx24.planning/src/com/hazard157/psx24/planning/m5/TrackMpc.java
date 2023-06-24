package com.hazard157.psx24.planning.m5;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.planning.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx.proj3.songs.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link TrackM5Model} panels.
 *
 * @author hazard157
 */
class TrackMpc
    extends MultiPaneComponentModown<ITrack> {

  private static final String PLAYLIST_FILE_EXT = "xspf"; //$NON-NLS-1$

  private String lastFilePath = TsLibUtils.EMPTY_STRING;

  public TrackMpc( ITsGuiContext aContext, IM5Model<ITrack> aModel, IM5ItemsProvider<ITrack> aItemsProvider,
      IM5LifecycleManager<ITrack> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aCtx, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    aActs.addAll( ACDEF_SEPARATOR, ACDEF_SAVE_AS, ACDEF_SEPARATOR, AI_PLAY );
    return super.doCreateToolbar( aCtx, aName, aIconSize, aActs );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  @SuppressWarnings( "nls" )
  final void createXspfFile( File aFile, IList<ITrack> aTracks ) {
    IUnitSongs unitSongs = tsContext().get( IUnitSongs.class );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      sw.pl( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
      sw.pl( "<playlist version=\"1\" xmlns=\"http://xspf.org/ns/0/\">" );
      sw.pl( "  <trackList>" );
      for( ITrack t : aTracks ) {
        ISong s = unitSongs.items().findByKey( t.songId() );
        if( s != null ) {
          sw.pl( "    <track>" );
          sw.pl( "      <title>%s</title>", s.nmName() );
          sw.pl( "      <location>%s</location>", s.filePath() );
          sw.pl( "    </track>" );
        }
      }
      sw.pl( "  </trackList>" );
      sw.pl( "</playlist>" );
      sw.writeEol();
    }
  }

  // ------------------------------------------------------------------------------------
  // MultiPaneComponentModown
  //

  @Override
  protected void doProcessAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_SAVE_AS: {
        File xspfFile = TsRcpDialogUtils.askFileSave( getShell(), lastFilePath, PLAYLIST_FILE_EXT );
        if( xspfFile != null ) {
          createXspfFile( xspfFile, tree().items() );
          lastFilePath = xspfFile.getAbsolutePath();
        }
        break;
      }
      case AID_PLAY: {
        ITrack sel = selectedItem();
        if( sel == null ) {
          break;
        }
        IUnitSongs unitSongs = tsContext().get( IUnitSongs.class );
        ISong song = unitSongs.items().findByKey( sel.songId() );
        if( song == null ) {
          TsDialogUtils.error( getShell(), FMT_UNKNOWN_SONG, sel.songId() );
          break;
        }
        File f = new File( song.filePath() );
        if( !TsFileUtils.isFileReadable( f ) ) {
          TsDialogUtils.error( getShell(), FMT_NO_SONG_FILE, f.getAbsolutePath() );
          break;
        }
        IMediaPlayerService mps = tsContext().get( IMediaPlayerService.class );
        mps.playAudioFilePart( f, sel.interval().start(), sel.interval().duration() );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ITrack aSel ) {
    toolbar().setActionEnabled( AID_PLAY, aIsSel );
  }

  @Override
  protected IMpcSummaryPane<ITrack> doCreateSummaryPane() {
    return new TrackSummaryPane( this );
  }

}
