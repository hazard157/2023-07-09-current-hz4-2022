package com.hazard157.prisex24.cofs;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Access to the PRISEX file resources in COFS (Cloud Optimized File System).
 * <p>
 * As for now (2023-03-28) PSX FS (File System) is based on the "hmade" structure. This is attempt to develop COFS, new
 * cloud storage optimized structure. COFS is single root based directories tree containing PRISEX resource files. Main
 * ideas to be implemented are:
 * <ul>
 * <li>most file resources will be placed in the root tree. The few exceptions are:
 * <ul>
 * <li>master video footage, only videos for episodes not gazes and other. <i>Motivation:</i> these materials are too
 * huge and are not used directly, only in the form of converted Source Videos;</li>
 * <li>non-second aligned source videos frame images, while seconds images and GIFs are in the workroom.
 * <i>Motivation:</i> there are too may such images so it takes too long time to synchronize between PCs and is too
 * heavy for cloud. Moreover, they are changed only when source videos are generated, that is very rarely;</li>
 * <li>cached thumbnail images generated by TsImageManager are placed somewhere in the system caches directory, outside
 * of the the COFS, while other cached resources (like trailer thumbnail GIF, generated episode image, etc) are in the
 * COFS.</li>
 * </ul>
 * </li>
 * <li>all generated media files to be placed in the single COFS "media" directory. Generated files are to be used not
 * only from PRISEX application, also from outside players/viewers. <i>Motivation:</i> single output directory makes it
 * easy to synchronize cloud on mobile gadgets and other devices with minimal amount of storage;</li>
 * <li>there must be separate folder for each incident (episode, gaze, etc) to make it easy opening separate top-level
 * windows of the PRISEX application. <i>Motivation:</i> the PRISEX application is both for fun and testing purposes.
 * Right now the multi-window (MWA) implementation of the ToxSoft's MWS needs to be tested heavily, so PRISEX is to be
 * redesigned on MWA environment;</li>
 * <li>COFS structure is to be used in all future versions of the PRISEX application, including all kinds of data (not a
 * file resources) storage: text file, USkat database, Convoy files, etc..</li>
 * <li>the symbolic links usage must be minimized or better, totally eliminated. <i>Motivation:</i> symbolic links are
 * not supported by Nextcloud cloud infrastructure.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IPsxCofs {

  /**
   * Returns the COFS root directory.
   *
   * @return {@link File} - the COFS root directory
   */
  File cofsRoot();

  /**
   * Returns means to access to the episode frame files.
   *
   * @return {@link ICofsFrames} - frame files manager
   */
  ICofsFrames cofsFrames();

  /**
   * Returns means to access output media files.
   *
   * @return {@link ICofsOutputMedia} - output media files manager
   */
  ICofsOutputMedia cofsOutputMedia();

  // ------------------------------------------------------------------------------------
  // yet unsorted may be put in some helper interfaces

  /**
   * Returns all Kdenlive project files of given episode.
   *
   * @param aEpisodeId String - the episode OD
   * @return IList&lt;File&gt; - list of project files *.kdenlive
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid episode ID
   */
  IList<File> listEpisodeKdenliveProjects( String aEpisodeId );

  /**
   * Returns source video file if exists.
   *
   * @param aSourceVideoId String - source video ID
   * @return {@link File} - source video file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File findSourceVideoFile( String aSourceVideoId );

}
