package com.hazard157.psx24.core.e4.services.filesys;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Поток загрузки изображений кадров.
 * <p>
 * Запускается методом {@link IPsxFileSystem#startThumbsLoading(IList, EThumbSize, IFrameLoadedCallback, boolean)}.
 * Завершается либо после загрузки всех кадров, либо пользователем методом {@link #close()}.
 *
 * @author goga
 */
public interface IFrameLoaderThread
    extends ICloseable {

  /**
   * Определяет, работат ли поток.
   *
   * @return boolean - признак работы потока
   */
  boolean isRunning();

  /**
   * Принудительно останавливает загрузку кадров.
   * <p>
   * Вызов на оснаовленном потоке является безопсаным.
   */
  @Override
  void close();

}
