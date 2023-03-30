package com.hazard157.psx24.core.glib.frlstviewer_ep;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;

/**
 * Просмотрщик списка кадров одного эпизода.
 * <p>
 * Состоит из:
 * <ul>
 * <li>панели инструментов - сверху, позволяет менять параметры отображения и работать с кадрами;</li>
 * <li>таблицы кадров - слева, содержит список отобранных критерием {@link #getCriteria()} кадров;</li>
 * <li>изображение кадра - справа, содержит изображение выбранного в таблице кадра.</li>
 * </ul>
 *
 * @author goga
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
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setCriteria( FrameSelectionCriteria aCriteria );

  /**
   * Устанавливает выделенным кадр, ближайший за указанную секунду.
   *
   * @param aSec int - секунда эпизода
   * @return {@link IFrame} - выдленный кадр или null если нечего выделять
   */
  IFrame positionOnSec( int aSec );

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
