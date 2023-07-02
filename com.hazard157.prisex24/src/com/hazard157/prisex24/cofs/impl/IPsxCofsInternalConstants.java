package com.hazard157.prisex24.cofs.impl;

import static com.hazard157.prisex24.cofs.l10n.IPsxCofsSharedResources.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.fs.*;

/**
 * Constants with persistent values.
 *
 * @author hazard157
 */
interface IPsxCofsInternalConstants {

  File MASTER_MEDIA_ROOT = new File( "/home/clouds.arch/hazard157.ru/prisex/original/" ); //$NON-NLS-1$

  // File HMADE_ROOT = new File( "/home/hmade" ); //$NON-NLS-1$

  File COFS_ROOT          = new File( "/home/hmade/cofs/" );     //$NON-NLS-1$
  File COFS_EPISODES_ROOT = new File( COFS_ROOT, "episodes/" );  //$NON-NLS-1$
  File COFS_GAZES_ROOT    = new File( COFS_ROOT, "inc-gazes/" ); //$NON-NLS-1$
  File COFS_MEDIA_ROOT    = new File( COFS_ROOT, "media/" );     //$NON-NLS-1$

  File COFS_CACHE_ROOT           = new File( COFS_ROOT, "cache/" );            //$NON-NLS-1$
  File COFS_CACHE_SUMMARIES_ROOT = new File( COFS_CACHE_ROOT, "summaries//" ); //$NON-NLS-1$

  File SPEC_IMAGE_MFK_AUDIO_FILE = new File( "/home/hmade/data/resources/spec-images/mfk-audio.png" ); //$NON-NLS-1$
  File SPEC_IMAGE_MFK_OTHER_FILE = new File( "/home/hmade/data/resources/spec-images/mfk-other.png" ); //$NON-NLS-1$

  String SUBDIR_GAZE_SOURCE = "source"; //$NON-NLS-1$
  String SUBDIR_GAZE_OUTPUT = "output"; //$NON-NLS-1$

  String SUBDIR_EP_STILL_IMAGES  = "frames-still"; //$NON-NLS-1$
  String SUBDIR_EP_ANIM_IMAGES   = "frames-anim";  //$NON-NLS-1$
  String SUBDIR_EP_SOURCE_VIDEOS = "srcvideos";    //$NON-NLS-1$
  String SUBDIR_EP_TRAILERS      = "trailers";     //$NON-NLS-1$
  String SUBDIR_EP_DEVEL         = "devel";        //$NON-NLS-1$

  String STILL_IMAGE_FILE_EXT = "jpg"; //$NON-NLS-1$
  String ANIM_IMAGE_FILE_EXT  = "gif"; //$NON-NLS-1$

  IStringList ALL_IMAGE_FILE_EXTS = new StringArrayList( //
      STILL_IMAGE_FILE_EXT, //
      ANIM_IMAGE_FILE_EXT //
  );

  File GIF_CREATE_SH = new File( "/home/hmade/bin/scripts/make-video-thumb.sh" ); //$NON-NLS-1$

  /**
   * Filters frame files.
   * <p>
   * Frame file is file having on of the {@link #ALL_IMAGE_FILE_EXTS} extensions (case-insensitive). File name must
   * match the rule of {@link PsxCofsUtils#isEpisodeFrameFileName(String)}.
   */
  TsFileFilter FF_FRAME_FILES = new TsFileFilter( EFsObjKind.FILE, ALL_IMAGE_FILE_EXTS, false, false ) {

    @Override
    protected boolean doAccept( File aPathName, boolean aIsFile ) {
      String bareName = TsFileUtils.extractBareFileName( aPathName.getName() );
      return PsxCofsUtils.isEpisodeFrameFileName( bareName );
    }

  };

  /**
   * ID of option {@link #OPDEF_MASTER_MEDIA_DATE}.
   */
  String OPID_MASTER_MEDIA_DATE = PSX_ID + ".date"; //$NON-NLS-1$

  /**
   * Option for {@link OptedFile#params()}: starting date-time.
   */
  IDataDef OPDEF_MASTER_MEDIA_DATE = DataDef.create( OPID_MASTER_MEDIA_DATE, VALOBJ, //
      TSID_NAME, STR_N_MASTER_MEDIA_DATE, //
      TSID_DESCRIPTION, STR_D_MASTER_MEDIA_DATE, //
      TSID_KEEPER_ID, LocalDateKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( LocalDate.ofInstant( Instant.now(), ZoneId.systemDefault() ) ) //
  );

}
