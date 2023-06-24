package com.hazard157.lib.core.excl_done.pgviewer;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_done.cellsgrid.*;

/**
 * Просмотрщик картинок в виде миниатюр.
 * <p>
 * Отображатся в качестве картинок (точнее, миниатюр) можно разные сущности, например файлы.
 * <p>
 * Viewer may be configured at creation time by the constants listed in {@link IPicsGridViewerConstants}.
 *
 * @author hazard157
 * @param <V> - тип ображаемых сущностей
 */
public interface IPicsGridViewer<V>
    extends ITsDoubleClickEventProducer<V>, ITsSelectionProvider<V>, IThumbSizeableEx, ITsUserInputProducer,
    ITsKeyInputProducer, ITsMouseInputProducer {

  // TODO item popup menu support

  // TODO mouse move support (with relative coors in cell/image)

  /**
   * Возвращает отображаемые сущности.
   *
   * @return {@link IList}&lt;V&gt; - список отображаемых сущностей
   */
  IList<V> items();

  /**
   * Задает отображаемые сущности.
   * <p>
   * Для очистки следует задать пустой список или <code>null</code>.
   *
   * @param aItems {@link IList}&lt;V&gt; - список отображаемых сущностей, может быть <code>null</code>
   */
  void setItems( IList<V> aItems );

  /**
   * Возвращает параметры настройки сетки миниатюр.
   *
   * @return {@link IGridMargins} - параметры настройка границ и интервалов рисования сетки значков
   */
  IGridMargins getMargins();

  /**
   * Задает параметры настройки сетки миниатюр.
   * <p>
   * Изменение параметров применяется немедленно.
   *
   * @param aMargins {@link IGridMargins} - параметры настройка границ и интервалов рисования сетки значков
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setMargins( IGridMargins aMargins );

  /**
   * Возвращает поставщик текстов к миниатюрам.
   *
   * @return {@link IPgvVisualsProvider} - поставщик текстов к миниатюрам
   */
  IPgvVisualsProvider<V> getVisualsProvider();

  /**
   * Задает поставщик текстов к миниатюрам.
   *
   * @param aVisualsProvider {@link IPgvVisualsProvider} - поставщик текстов к миниатюрам
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setVisualsProvider( IPgvVisualsProvider<V> aVisualsProvider );

  /**
   * Обновляет панель, включая применение измененных параметров контекста.
   */
  void refresh();

  /**
   * Возвращает SWT контрол реализации интерфейса.
   *
   * @return {@link TsComposite} - SWT-контроль
   */
  TsComposite getControl();

}
