package com.hazard157.lib.core.glib.fsviewers;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Просмотрщик списка файлов (не директорий) в виде миниатюр.
 *
 * @author hazard157
 */
public interface IFsListThumbViewer
    extends ITsSelectionProvider<File>, ITsDoubleClickEventProducer<File>, IThumbSizeable {

  /**
   * Возвращает список текущих отображаемых файлов.
   *
   * @return IList&lt;File&gt; - список текущих отображаемых файлов
   */
  IList<File> items();

  /**
   * Задает список текущих отображаемых файлов
   *
   * @param aItems IList&lt;File&gt; - список текущих отображаемых файлов
   */
  void setItems( IList<File> aItems );

  /**
   * Возвращает контроль, реализующий промсотрщик.
   *
   * @return {@link Control} - кронтроль (виджет) просмотрщика)
   */
  Control getControl();

}
