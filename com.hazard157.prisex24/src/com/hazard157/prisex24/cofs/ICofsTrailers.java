package com.hazard157.prisex24.cofs;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.opfil.*;

/**
 * COFS - access to the episode trailer files.
 *
 * @author hazard157
 */
public interface ICofsTrailers {

  /**
   * Returns the trailer files of the specified episode.
   * <p>
   * For non-existent episodes returns an empty list.
   *
   * @param aEpisodeId String - the episode ID
   * @return {@link IList}&lt;{@link File}&gt; - trailer media files with their stored params
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IOptedFile> listEpisodeTrailerFiles( String aEpisodeId );

  /**
   * Finds in {@link #listEpisodeTrailerFiles(String)} specified trailer file.
   *
   * @param aEpisodeId String - the episode ID
   * @param aTrailerName String trailer file name without extension
   * @return {@link IOptedFile} - found file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IOptedFile findTrailerFile( String aEpisodeId, String aTrailerName );

  /**
   * Returns the GIF-animated summary of the trailer.
   * <p>
   * Creates new GIF-animation if there is no one or trailer was changed after last GIF creation.
   *
   * @param aTrailerFile {@link IOptedFile} - trailer file
   * @return {@link File} - summary GIF file or <code>null</code> if can not be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File getSummaryGif( IOptedFile aTrailerFile );

  /**
   * Returns the Kdenlive project file without existence check.
   *
   * @param aTrailerFile {@link File} - the trailer file
   * @return {@link IList}&lt;{@link File}&gt; - expected Kdenlive project files (mey me several in subdirs)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<File> listKdenliveProjectFiles( File aTrailerFile );

}
