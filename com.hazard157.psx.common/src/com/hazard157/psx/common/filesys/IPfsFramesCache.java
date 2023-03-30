package com.hazard157.psx.common.filesys;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * PSX frames images cache.
 *
 * @author hazard157
 */
public interface IPfsFramesCache {

  /**
   * Lists all frames with image files of specified episode.
   *
   * @param aEpisodeId String - the episode ID
   * @return {@link IList}&lt;{@link IFrame}&gt; - list of frames with corresponding image files
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IFrame> listEpisodeFrames( String aEpisodeId );

  /**
   * Lists all frames with image files of specified episode and camera.
   *
   * @param aEpisodeId String - the episode ID
   * @param aCamId String - the camera ID
   * @return {@link IList}&lt;{@link IFrame}&gt; - list of frames with corresponding image files
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IFrame> listCamFrames( String aEpisodeId, String aCamId );

  /**
   * Refreshes frame in cache - removes if file not exists and ensures if file exists.
   *
   * @param aFrame {@link IFrame} - the frame
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void updateFrameFile( IFrame aFrame );
}
