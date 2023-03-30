package com.hazard157.psx.common.filesys.olds.psx12.films;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.incub.optedfile.*;

/**
 * Working with films resource files.
 *
 * @author goga
 */
public interface IFsFilmFilesManager {

  /**
   * Returns the valid films root directory.
   *
   * @return {@link File} - the films root directory
   */
  File getRootDir();

  /**
   * Returns the film files.
   *
   * @param aParams {@link IOptionSet} - query params
   * @return {@link IList}&lt;{@link File}&gt; - film media files with their stored params
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<OptedFile> listFilms( IOptionSet aParams );

  /**
   * Returns the GIF-animated film trailer image.
   * <p>
   * Creates new GOF-animation if there is no one or film was changed after last GIF creation.
   *
   * @param aFilmFile {@link File} - film file
   * @param aThumb {@link EThumbSize} - thumb size
   * @return {@link TsImage} - an image or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsImage loadFilmThumb( File aFilmFile, EThumbSize aThumb );

  /**
   * Возвращает файл проекта Kdenlive запрошенного фильма, не проверяя его существование.
   *
   * @param aFilmFile {@link File} - файл фильма
   * @return {@link File} - файл проекта Kdenlive запрошенного фильма, может не существовать
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  File kdenliveProjectFile( File aFilmFile );

}
