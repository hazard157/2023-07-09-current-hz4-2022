package com.hazard157.prisex24.cofs;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.fs.*;

/**
 * COFS - access to the output film files.
 *
 * @author hazard157
 */
public interface ICofsFilms {

  /**
   * Returns the film files.
   *
   * @param aParams {@link IOptionSet} - query params as listed in {@link ICofsFilmsConstants#ALL_FILMS_QP_OPS}
   * @return {@link IList}&lt;{@link File}&gt; - film media files with their stored params
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<OptedFile> listFilms( IOptionSet aParams );

  /**
   * Returns the GIF-animated summary of the film.
   * <p>
   * Creates new GIF-animation if there is no one or film was changed after last GIF creation.
   *
   * @param aFilmFile {@link OptedFile} - film file
   * @return {@link File} - summary GIF file or <code>null</code> if can not be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File getSummaryGif( OptedFile aFilmFile );

  /**
   * Returns the Kdenlive project file without existence check.
   *
   * @param aFilmFile {@link File} - the film file
   * @return {@link IList}&lt;{@link File}&gt; - expected Kdenlive project files (mey me several in subdirs)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<File> listKdenliveProjectFiles( File aFilmFile );

}
