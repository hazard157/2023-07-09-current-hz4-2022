package com.hazard157.psx24.core.e4.services.filesys;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.image.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Обратный вызов по мере окночания загрузки очередного изображения кадра.
 * <p>
 * Этот метод вызывается в основном GUI-потоке выполнения с помощью {@link Display#asyncExec(Runnable)}.
 *
 * @author hazard157
 */
public interface IFrameLoadedCallback {

  /**
   * Ничего не делает, используется вместо null.
   */
  IFrameLoadedCallback NULL = ( aImageDef, aImage ) -> {
    // nop
  };

  /**
   * Вызывается, когда изображения загружено.
   * <p>
   * Точнее, вызвается, когда была попытка загрузить указанное изображение. Если загрузка не удалась, аргумент aImage =
   * null.
   *
   * @param aFrame {@link IFrame} - загружаемый кадр
   * @param aImage {@link TsImage} - загруженное изображение, или null
   */
  void imageLoaded( IFrame aFrame, TsImage aImage );

}
