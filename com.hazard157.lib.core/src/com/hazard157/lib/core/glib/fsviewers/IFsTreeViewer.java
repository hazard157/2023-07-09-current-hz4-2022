package com.hazard157.lib.core.glib.fsviewers;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.fs.*;
import com.hazard157.lib.core.incub.kof.*;

// TODO TRANSLATE

/**
 * Просмотрщик файловой системы {@link IKofFileSystem}.
 *
 * @author hazard157
 * @param <T> - type of OptedFile subclass
 */
public interface IFsTreeViewer<T extends OptedFile>
    extends ITsDoubleClickEventProducer<T>, ITsSelectionProvider<T>, IIconSizeable {

  /**
   * Возвращает отображаемую файловую систему.
   *
   * @return {@link IKofFileSystem} - поставщик файловой системы
   */
  IKofFileSystem<T> fsProvider();

  /**
   * Задает отображаемую файловую систему.
   *
   * @param aFileSystemProvider {@link IKofFileSystem} - поставщик файловой системы
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setFsProvider( IKofFileSystem<T> aFileSystemProvider );

  /**
   * Определят, показываются ли файлы, или только директории.
   * <p>
   * По умолчанию - <code>false</code>.
   *
   * @return boolean - признак показа файлов<br>
   *         <b>true</b> - в директориях показаны файлы;<br>
   *         <b>false</b> - в дерево показаны только директории, но не файлы.
   */
  boolean isFilesShown();

  /**
   * Задает признак показа файлов {@link #isFilesShown()}.
   *
   * @param aShown boolean - признак показа файлов
   */
  void setFilesShown( boolean aShown );

  /**
   * Определяет, рисуются ли файлы миниатюрами или значками типа файла.
   * <p>
   * Фактически, определяет, какой из методов интерфейса {@link IFsIconProvider} поставляет изображение файла:
   * <ul>
   * <li><code>true</code> - используется {@link IFsIconProvider#getFileContentThumbnail(File, EThumbSize)};</li>
   * <li><code>false</code> - используется {@link IFsIconProvider#getFileTypeIcon(File, EIconSize)}.</li>
   * </ul>
   * <p>
   * По умолчанию - <code>true</code>.
   *
   * @return boolean - признак отображения миниатюр содержимого (а не значков типа файла)
   */
  boolean isFileContentThumbsUsed();

  /**
   * Задает, рисуются ли файлы миниатюрами или значками типа файла.
   *
   * @param aUse boolean - признак отображения миниатюр содержимого (а не значков типа файла)
   */
  void setFileContentThumbsUsed( boolean aUse );

  /**
   * Возвращает поставщик значков и миниатюр файлов.
   * <p>
   * По умолчанию используется {@link IFsIconProvider#NULL}.
   *
   * @return {@link IFsIconProvider} - поставщик значков и миниатюр файлов
   */
  IFsIconProvider fileIconProvider();

  /**
   * Задает поставщик значков и миниатюр файлов.
   * <p>
   * Внимание: это просмотрщик не становится владельцем ресурсов. Это задача постащика освобождать ресурсы изображеия.
   *
   * @param aProvider {@link IFsIconProvider} - поставщик значков и миниатюр файлов
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setFileIconProvider( IFsIconProvider aProvider );

  /**
   * Возвращает средства управления визуальным представлением дерева.
   * <p>
   * Узлами в консоли являются объекты класса {@link File}.
   *
   * @return {@link ITsTreeViewerConsole} - средства управления визуальным представлением дерева
   */
  ITsTreeViewerConsole console();

  /**
   * Обновляет все дерево.
   */
  void refresh();

  /**
   * Обновляет дерево начиная от заданного корня и ниже.
   * <p>
   * Если аргумент не директория, то обновляет дерево начиная с корня - родительского файла.
   *
   * @param aRoot &lt;T&gt; - корневая директория для обновления
   * @throws TsNullArgumentRtException аргумент = null
   */
  void refresh( T aRoot );

  /**
   * Возвращает контроль, реализующий промсотрщик.
   *
   * @return {@link Control} - кронтроль (виджет) просмотрщика)
   */
  Control getControl();

}
