package com.hazard157.psx24.core.glib.frlstviewer_ep;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;

/**
 * Single episode frame list viewer.
 * <p>
 * Contains:
 * <ul>
 * <li>toolbar - on top, allows you to change display options and work with frames;</li>
 * <li>frame tables - on the left, contains a list of frames selected by the {@link #getCriteria()} criterion;</li>
 * <li>frame preview - on the right, contains the image of the frame selected in the table.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IEpisodeFramesListViewer
    extends ILazyControl<TsComposite>, ITsSelectionProvider<IFrame>, ITsDoubleClickEventProducer<IFrame> {

  /**
   * Возвращает текущий критерии показа кадров.
   * <p>
   * Текущий критерий состоит из приведенных значений по умолчанию каждого из индивидуальных параметров.
   *
   * @return {@link FrameSelectionCriteria} - критерии показа кадров
   */
  FrameSelectionCriteria getCriteria();

  /**
   * Изменяет список по новому критерию показа кадров.
   *
   * @param aCriteria {@link FrameSelectionCriteria} - новый криетрии
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setCriteria( FrameSelectionCriteria aCriteria );

  // /**
  // * Устанавливает выделенным кадр, ближайший за указанную секунду.
  // *
  // * @param aSec int - секунда эпизода
  // * @return {@link IFrame} - выдленный кадр или null если нечего выделять
  // */
  // IFrame positionOnSec( int aSec );

  /**
   * Возвращает кадры, которые отображены в списке.
   * <p>
   * Этот список меняется в зависимости от критерия {@link #getCriteria()} и положении кнопок на панели управления
   * списоком (например, пользователь отключил показ не-анимированных кадров)
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - перечень отображаемых в списке кадров
   */
  IList<IFrame> listShownFrames();

  /**
   * Обновляет список кадров.
   */
  void refresh();

}
