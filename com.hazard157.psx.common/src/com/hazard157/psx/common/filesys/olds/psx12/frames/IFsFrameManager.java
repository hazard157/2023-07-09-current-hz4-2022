package com.hazard157.psx.common.filesys.olds.psx12.frames;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.sc.*;

/**
 * Менеджер управления файлами кадров эпизодов.
 *
 * @author goga
 */
public interface IFsFrameManager {

  /**
   * Возвращает файл кадра эпизода, если таковой существует.
   *
   * @param aFrame {@link IFrame} - искомый кадр
   * @return {@link File} - файл иллюстрации или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  File findFrameFile( IFrame aFrame );

  /**
   * Находит изображение миниатюры указанного кадра.
   * <p>
   * Для этого, сначала находит файл методом {@link #findFrameFile(IFrame)}, а потом загружает миниатюру методом
   * {@link ITsImageManager#findThumb(File, EThumbSize)}.
   *
   * @param aFrame {@link IFrame} - запрошенный кадр
   * @param aThumbSize {@link EThumbSize} - размер получаемого миниатюры
   * @return {@link TsImage} - изображение миниатюры или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize );

  /**
   * Создает экземпляр {@link File}, указываюший местополжение полноразмерного изображения кадра.
   * <p>
   * Не делает проверку на существование или доступность реального файла.
   *
   * @param aFrame {@link IFrame} - кадр
   * @return {@link File} - предполагаеоме расположение файла изображения кадра
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент {@link IFrame#NONE}
   */
  File makeFrameFile( IFrame aFrame );

  /**
   * Returns the frames selected according to criterion.
   * <p>
   * Only the frames with existing image files are returned.
   * <p>
   * When <code>aForcedFrame</code> = <code>true</code>, in case when no frames are selected according to criterion from
   * argument, selector will add one frame from asked interval. This forced frame per interval mode is especially useful
   * for short intervals with long step between frame {@link ISingleCriterion#timeStep()} to avoid empty result.
   *
   * @param aCriterion {@link ISingleCriterion} - selection criterion
   * @param aForcedFrame boolean - forced frame per intarval flag
   * @return {@link IList}&lt;{@link IFrame}&gt; - the frames list
   */
  IList<IFrame> selectFrames( ISingleCriterion aCriterion, boolean aForcedFrame );

  /**
   * Returns the frames selected according to criterion.
   * <p>
   * Only the frames with existing image files are returned.
   * <p>
   * When <code>aForcedFrame</code> = <code>true</code>, in case when no frames are selected according to criterion from
   * argument, selector will add one frame from asked interval. This forced frame per interval mode is especially useful
   * for short intervals with long step between frame {@link ISingleCriterion#timeStep()} to avoid empty result.
   *
   * @param aCriteria {@link IList}&lt;{@link ISingleCriterion}&gt; - selection criteria
   * @param aForcedFrame boolean - forced frame per intarval flag
   * @return {@link IList}&lt;{@link IFrame}&gt; - the frames list
   */
  IList<IFrame> selectFrames( IList<ISingleCriterion> aCriteria, boolean aForcedFrame );

}
