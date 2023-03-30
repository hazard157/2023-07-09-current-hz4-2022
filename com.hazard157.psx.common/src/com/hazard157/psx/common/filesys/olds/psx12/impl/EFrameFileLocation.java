package com.hazard157.psx.common.filesys.olds.psx12.impl;

import static com.hazard157.psx.common.filesys.olds.psx12.impl.PsxFileSystemUtils.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Стратегия расположения файлов изображений кдров эпизодов.
 *
 * @author goga
 */
enum EFrameFileLocation {

  ANIM( new FrameImageFileFilter( ANIM_FRAME_FILE_EXT ) ) {

    @Override
    File getDir( LocalDate aEpisodeDate ) {
      File epDir = new File( EPISODES_RESOURCES_ROOT, aEpisodeDate.toString() );
      return new File( epDir, EPSUBDIR_FRAMES_ANIM );
    }
  },

  SEC_ALIGNED( new FrameImageFileFilter( STILL_FRAME_FILE_EXT ) ) {

    @Override
    File getDir( LocalDate aEpisodeDate ) {
      File epDir = new File( EPISODES_RESOURCES_ROOT, aEpisodeDate.toString() );
      return new File( epDir, EPSUBDIR_FRAMES_SECS );
    }
  },

  NOT_ALIGNED( new FrameImageFileFilter( STILL_FRAME_FILE_EXT ) ) {

    @Override
    File getDir( LocalDate aEpisodeDate ) {
      return new File( NONSEC_FRAMES_ROOT, aEpisodeDate.toString() );
    }
  };

  private final TsFileFilter fileFilter;

  EFrameFileLocation( TsFileFilter aFileFilter ) {
    fileFilter = aFileFilter;
  }

  File getFile( LocalDate aEpisodeDate, IFrame aFrame ) {
    String bareName = PsxFileSystemUtils.bareSourceFrameFileName( aFrame.frameNo() );
    File camDir = new File( getDir( aEpisodeDate ), aFrame.cameraId() );
    String ext = aFrame.isAnimated() ? ANIM_FRAME_FILE_EXT : STILL_FRAME_FILE_EXT;
    return new File( camDir, bareName + '.' + ext );
  }

  File getFile( IFrame aFrame ) {
    LocalDate date = aFrame.whenDate();
    return getFile( date, aFrame );
  }

  TsFileFilter getImageFileFilter() {
    return fileFilter;
  }

  static EFrameFileLocation locationOf( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    TsIllegalArgumentRtException.checkFalse( aFrame.isDefined() );
    if( aFrame.isAnimated() ) {
      return ANIM;
    }
    if( aFrame.isSecAligned() ) {
      return SEC_ALIGNED;
    }
    return NOT_ALIGNED;
  }

  File getDir( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    TsIllegalArgumentRtException.checkFalse( aFrame.isDefined() );
    return getDir( aFrame.whenDate() );
  }

  abstract File getDir( LocalDate aEpisodeDate );

}
