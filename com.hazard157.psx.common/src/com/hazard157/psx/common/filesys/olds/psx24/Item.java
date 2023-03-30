package com.hazard157.psx.common.filesys.olds.psx24;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.filesys.olds.psx24.PsxFileSystem.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Элемент очереди потка загрузки изображений {@link FrameLoaderThread}.
 *
 * @author goga
 */
class Item {

  private final IFrame               frame;
  private final IFrameLoadedCallback callback;
  private final File                 file;
  private final EThumbSize           thumbSize;

  Item( IFrame aFrame, File aFile, EThumbSize aIconSize, IFrameLoadedCallback aCallback ) {
    if( aFrame == null || aFile == null || aCallback == null || aIconSize == null ) {
      throw new TsNullArgumentRtException();
    }
    frame = aFrame;
    file = aFile;
    callback = aCallback;
    thumbSize = aIconSize;
  }

  /**
   * Возвращает кадр.
   *
   * @return {@link IFrame} - кадр, не бывает <code>null</code>
   */
  public IFrame frame() {
    return frame;
  }

  /**
   * Возвращает интерфейс обратного вызовапо загрузке изображения.
   *
   * @return {@link IFrameLoadedCallback} - интерфейс обратного вызовапо загрузке изображения, не бывает
   *         <code>null</code>
   */
  public IFrameLoadedCallback callback() {
    return callback;
  }

  /**
   * Возвращает файл изображения.
   *
   * @return {@link File} - файл изображения, не бывает <code>null</code>
   */
  public File file() {
    return file;
  }

  /**
   * Возвращает размер запрошенной миниатюры.
   *
   * @return {@link EThumbSize} - размер запрошенной миниатюры, не бывает <code>null</code>
   */
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public String toString() {
    return frame.toString();
  }

}
