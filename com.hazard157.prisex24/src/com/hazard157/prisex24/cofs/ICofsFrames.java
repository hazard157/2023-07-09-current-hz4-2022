package com.hazard157.prisex24.cofs;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * COFS - access to the episode frame file.
 * <p>
 * There are following kinds of frame image:
 * <ol>
 * <li>animated GIFs - modern one have duration of 5 seconds and start at seconds boundary, old ones may start at any
 * time and have any duration;</li>
 * <li>second-aligned JPGs - are files names HH-MM-SS_<b>00</b>.jpg;</li>
 * <li>all frame JPGs - are files names HH-MM-SS_<b>FF</b>.jpg, where FF have value 00..24.</li>
 * </ol>
 * COFS contains only frames of kind 1 and 2. Kind 3 (all frames) contains too many files and are outside of COFS. So
 * this interface works only with GIFs and second aligned JPGs.
 * <p>
 * Note: when application asks for frames of kind 3 the warning is logged.
 *
 * @author hazard157
 */
public interface ICofsFrames {

  /**
   * Returns frame file if exists.
   * <p>
   * For {@link IFrame#NONE} returns special file with unknown episode meaning.
   *
   * @param aFrame {@link IFrame} - the frame
   * @return {@link File} - existing frame image file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File findFrameFile( IFrame aFrame );

  /**
   * Returns all frames with images files.
   *
   * @param aEpisodeId String - the requested episode ID
   * @return {@link IList}&lt;{@link IFrame}&gt; - the list of all frames of the episode
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid ID of episode
   */
  IFramesSet listEpisodeFrames( String aEpisodeId );

  // TODO add non-sec-alignes frames management

}
