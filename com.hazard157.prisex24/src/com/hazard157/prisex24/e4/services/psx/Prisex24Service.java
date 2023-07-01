package com.hazard157.prisex24.e4.services.psx;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.e4.services.psx.IPsxResources.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * {@link IPrisex24Service} implementation.
 *
 * @author hazard157
 */
public class Prisex24Service
    implements IPrisex24Service, IPsxGuiContextable {

  /**
   * Last copy destination apppref ID in the bundle {@link IPrisex24CoreConstants#PBID_PSX24_COMMON}.
   */
  private static final String APPREFID_LAST_DESTINATION = "FrameCopyLastDestination"; //$NON-NLS-1$

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Prisex24Service( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void errDialog( String aMsg, Object... aArgs ) {
    TsDialogUtils.error( getShell(), aMsg, aArgs );
  }

  private void performFrameImageCopy( IFrame aFrame, File aDestDir ) {
    TsNullArgumentRtException.checkNull( aFrame );
    TsFileUtils.checkDirReadable( aDestDir );
    File srcFile = cofsFrames().findFrameFile( aFrame );
    if( srcFile == null ) {
      return;
    }
    String newName = aFrame.episodeId() + '-' + // episode ID
        TsFileUtils.extractBareFileName( srcFile.getName() ) + '-' + // frameNo ЧЧ-ММ-СС_КК
        aFrame.cameraId() + // camera ID
        TsFileUtils.CHAR_EXT_SEPARATOR + TsFileUtils.extractExtension( srcFile.getName() ); // source extension
    File destFile = new File( aDestDir, newName );
    TsFileUtils.copyFile( srcFile, destFile );
  }

  // ------------------------------------------------------------------------------------
  // IPrisex24Service
  //

  @Override
  public TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFrame, aThumbSize );
    File frameFile = cofsFrames().findFrameFile( aFrame );
    if( frameFile == null ) {
      return null;
    }
    return imageManager().findThumb( frameFile, aThumbSize );
  }

  @Override
  public void playEpisodeVideo( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    // check episode exists
    IEpisode e = unitEpisodes().items().findByKey( aSvin.episodeId() );
    if( e == null ) {
      errDialog( FMT_ERR_NO_SUCH_EPISODE, aSvin.episodeId() );
      return;
    }
    IStringMap<ISourceVideo> svs = unitSourceVideos().episodeSourceVideos( aSvin.episodeId() );
    if( svs.isEmpty() ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEOS, aSvin.episodeId() );
      return;
    }
    String camId = aSvin.cameraId();
    if( !aSvin.hasCam() ) {
      camId = TsLibUtils.EMPTY_STRING;
      IFrame f = e.frame();
      if( f.isDefined() ) {
        camId = f.cameraId();
      }
      else {
        camId = svs.keys().get( 0 );
      }
    }
    ISourceVideo srcVideo = svs.findByKey( camId );
    if( srcVideo == null ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEO, aSvin.episodeId(), camId );
      return;
    }
    File f = psxCofs().findSourceVideoFile( srcVideo.id() );
    if( f == null ) {
      errDialog( FMT_ERR_NO_SOURCE_VIDEO_FILE, aSvin.episodeId(), camId );
      return;
    }
    if( !TsFileUtils.isFileReadable( f ) ) {
      errDialog( FMT_ERR_SOURCE_VIDEO_FILE_INACCESSABLE, f.getAbsolutePath() );
      return;
    }
    if( !aSvin.isWholeEpisode() ) {
      mps().playVideoFilePart( f, aSvin.interval().start(), aSvin.interval().duration() + 1 );
    }
    else {
      mps().playVideoFile( f );
    }
  }

  @Override
  public void copyFrameImage( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    try {
      IPrefBundle pb = prefBundle( PBID_PSX24_COMMON );
      String path = pb.prefs().getStr( APPREFID_LAST_DESTINATION, TsLibUtils.EMPTY_STRING );
      File destDir = TsRcpDialogUtils.askDirOpen( getShell(), path );
      if( destDir != null ) {
        performFrameImageCopy( aFrame, destDir );
        path = destDir.getAbsolutePath();
        pb.prefs().setStr( APPREFID_LAST_DESTINATION, path );
      }
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }
  }

  @Override
  public void runKdenliveFor( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    IList<File> projs = psxCofs().listEpisodeKdenliveProjects( aFrame.episodeId() );
    ITsDialogInfo cdi = TsDialogInfo.forSelectEntity( tsContext );
    File sel = DialogItemsList.select( cdi, projs, null, ITsNameProvider.DEFAULT );
    if( sel != null ) {
      TsMiscUtils.runProgram( PROGRAM_KDENLIVE, sel.getAbsolutePath() );
    }
  }

}
