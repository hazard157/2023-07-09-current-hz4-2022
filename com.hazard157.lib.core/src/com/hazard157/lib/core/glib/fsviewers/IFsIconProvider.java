package com.hazard157.lib.core.glib.fsviewers;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

//TODO TRANSLATE

/**
 * Поставщик значков и миниатюр файлов.
 * <p>
 * Кто является владельцем возвращаемых ресурсов определяется конкретной реализацией интерфейса.
 *
 * @author goga
 */
public interface IFsIconProvider {

  /**
   * "Нулевой" постащик значков, возвращает <code>null</code>-ы.
   */
  IFsIconProvider NULL = new InternalNullFileIconProvider();

  /**
   * Возвращает значок, соответствующий типу файла.
   *
   * @param aFile {@link File} - файл (всегда {@link File#isFile()} = <code>true</code>)
   * @param aSize {@link EIconSize} - запрашиваемый размер значка
   * @return {@link Image} - значок типа файла или null, если нет значка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  Image getFileTypeIcon( File aFile, EIconSize aSize );

  /**
   * Возвращает миниатюру содерджимого файла.
   *
   * @param aFile {@link File} - файл (всегда {@link File#isFile()} = <code>true</code>)
   * @param aSize {@link EThumbSize} - запрашиваемый размер миниатюры
   * @return {@link TsImage} - миниатура, соответствующая содержимому файла или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  TsImage getFileContentThumbnail( File aFile, EThumbSize aSize );

  /**
   * Возвращает значок для файлов неизвестного типа.
   * <p>
   * Этот же значок может использоваться, когда отменен показ специфичных для файла значков.
   *
   * @param aSize {@link EIconSize} - запрашиваемый размер значка
   * @return {@link Image} - значок или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  Image getDefaultFileIcon( EIconSize aSize );

  /**
   * Возвращает значок для папок (директорий).
   *
   * @param aSize {@link EIconSize} - запрашиваемый размер значка
   * @return {@link Image} - значок или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  Image getDefaultFolderIcon( EIconSize aSize );

}

class InternalNullFileIconProvider
    implements IFsIconProvider {

  @Override
  public Image getFileTypeIcon( File aFile, EIconSize aSize ) {
    return null;
  }

  @Override
  public TsImage getFileContentThumbnail( File aFile, EThumbSize aSize ) {
    return null;
  }

  @Override
  public Image getDefaultFileIcon( EIconSize aSize ) {
    return null;
  }

  @Override
  public Image getDefaultFolderIcon( EIconSize aSize ) {
    return null;
  }

}
