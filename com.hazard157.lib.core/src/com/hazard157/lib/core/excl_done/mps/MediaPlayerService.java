package com.hazard157.lib.core.excl_done.mps;

import static com.hazard157.lib.core.excl_done.mps.IHzResources.*;
import static org.toxsoft.core.tslib.utils.TsMiscUtils.*;

import java.io.*;

import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Реализация {@link IMediaPlayerService}.
 *
 * @author hazard157
 */
public class MediaPlayerService
    implements IMediaPlayerService {

  /**
   * Имя программы для воспроизведения видео.
   * <p>
   * Предполагается использование MPlayer или совместимого по аргументам командной строки проигривателя.
   */
  public static final String VIDEO_PLAYER_NAME = "gmplayer"; //$NON-NLS-1$

  /**
   * Имя программы для воспроизведения аудио.
   */
  public static final String AUDIO_PLAYER_NAME = "gmplayer"; //$NON-NLS-1$

  // private static final String XDG_OPEN = "/usr/bin/xdg-open"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public MediaPlayerService() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IMediaPlayerService
  //

  @SuppressWarnings( "nls" )
  @Override
  public void playVideoFile( File aVideoFile, boolean aFullscreen ) {
    TsNullArgumentRtException.checkNull( aVideoFile );
    TsFileUtils.checkFileReadable( aVideoFile );
    if( aFullscreen ) {
      runProgram( "mplayer", "-quiet", "-fs", aVideoFile.getAbsolutePath() );
    }
    else {
      runProgram( VIDEO_PLAYER_NAME, "-quiet", aVideoFile.getAbsolutePath() );
    }
  }

  @SuppressWarnings( "nls" )
  @Override
  public void playVideoFilePart( File aVideoFile, int aStartSec, int aDurationSecs ) {
    TsIllegalArgumentRtException.checkTrue( aStartSec < 0 );
    TsIllegalArgumentRtException.checkTrue( aDurationSecs < 0 );
    TsFileUtils.checkFileReadable( aVideoFile );
    Integer hours = Integer.valueOf( aStartSec / 3600 );
    Integer mins = Integer.valueOf( (aStartSec % 3600) / 60 );
    Integer secs = Integer.valueOf( aStartSec % 60 );
    String argStart = String.format( "%02d:%02d:%02d", hours, mins, secs );
    String argEndpos = String.format( "%d", Integer.valueOf( aDurationSecs ) );
    // эти опции работают с mplayer, но не работают с smplayer
    runProgram( VIDEO_PLAYER_NAME, "-quiet", //
        "-ss", argStart, //
        "-endpos", argEndpos, //
        "actions", "size_150", //
        aVideoFile.getAbsolutePath() );
  }

  @Override
  public void playAudioFile( File aAudioFile ) {
    TsNullArgumentRtException.checkNull( aAudioFile );
    TsFileUtils.checkFileReadable( aAudioFile );
    runProgram( AUDIO_PLAYER_NAME, aAudioFile.getAbsolutePath() );
  }

  @SuppressWarnings( "nls" )
  @Override
  public void playAudioFilePart( File aAudioFile, int aStartSec, int aDurationSecs ) {
    TsIllegalArgumentRtException.checkTrue( aStartSec < 0 );
    TsIllegalArgumentRtException.checkTrue( aDurationSecs < 0 );
    TsFileUtils.checkFileReadable( aAudioFile );
    Integer hours = Integer.valueOf( aStartSec / 3600 );
    Integer mins = Integer.valueOf( (aStartSec % 3600) / 60 );
    Integer secs = Integer.valueOf( aStartSec % 60 );
    String argStart = String.format( "%02d:%02d:%02d", hours, mins, secs );
    String argEndpos = String.format( "%d", Integer.valueOf( aDurationSecs ) );
    // эти опции работают с mplayer, но не работают с smplayer
    runProgram( AUDIO_PLAYER_NAME, "-ss", argStart, "-endpos", argEndpos, "-volume", "100",
        aAudioFile.getAbsolutePath() );
  }

  @Override
  public void runPrefApp( File aFile ) {
    if( aFile == null ) {
      TsDialogUtils.error( null, MSG_ERR_ARG_IS_NULL );
      return;
    }
    if( !aFile.exists() ) {
      TsDialogUtils.error( null, FMT_ERR_FILE_NOT_EXISTS, aFile.getAbsolutePath() );
      return;
    }

    // FIXME ???

    try {
      Runtime.getRuntime().exec( new String[] { "xdg-open", aFile.getAbsolutePath() } ); //$NON-NLS-1$
    }
    catch( IOException ex ) {
      // TODO Auto-generated catch block
      LoggerUtils.errorLogger().error( ex );
    }

    // runProgram( "xdg-open", aFile.getAbsolutePath() ); // NOT WORKS :(

    // if( java.awt.Desktop.isDesktopSupported() ) {
    // java.awt.Desktop d = java.awt.Desktop.getDesktop();
    // try {
    // d.open( aFile );
    // }
    // catch( IOException ex ) {
    // LoggerUtils.errorLogger().error( ex );
    // TsDialogUtils.error( null, ex );
    // }
    // }
  }

}
