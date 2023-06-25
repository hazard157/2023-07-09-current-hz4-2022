package com.hazard157.lib.core.excl_plan.plv;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Элемент отображения просмотрщиком {@link PicturesListViewer}.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public class PlvItem {

  private final TsImage image;
  private final String  label1;
  private final String  label2;
  private final Object  userData;
  private final int     width;
  private final int     height;
  private int           currentIndex = 0;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aImage {@link TsImage} - само изображения
   * @param aLabel1 String - строка подписи 1
   * @param aLabel2 String - строка подписи 2
   * @param aUserData Object - произвольные пользовательские данные, может быть null
   * @throws TsNullArgumentRtException любой аргумент (кроме aUserData) = null
   * @throws TsIllegalArgumentRtException aMage уже освобожден (disposed)
   */
  public PlvItem( TsImage aImage, String aLabel1, String aLabel2, Object aUserData ) {
    TsNullArgumentRtException.checkNulls( aImage );
    TsNullArgumentRtException.checkNulls( aLabel1, aLabel2 );
    TsIllegalArgumentRtException.checkTrue( aImage.isDisposed() );
    image = aImage;
    label1 = aLabel1;
    label2 = aLabel2;
    userData = aUserData;
    width = image.image().getBounds().width;
    height = image.image().getBounds().height;
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Меняет текущий индекс на следующий с переходом по циклу в массиве {@link TsImage#frames()}.
   */
  void nextIndex() {
    if( !image.isSingleFrame() ) {
      if( ++currentIndex >= image.count() ) {
        currentIndex = 0;
      }
    }
  }

  /**
   * Возвращает изображение - кадр с текущим индексом из массика {@link TsImage#frames()}.
   *
   * @return {@link Image} - изображение с текущим индексом
   */
  Image getCurrenrImage() {
    return image.frames().get( currentIndex );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает само изображения.
   *
   * @return {@link TsImage} - само изображения
   */
  public TsImage image() {
    return image;
  }

  /**
   * Определяет, является ли изображениа анимированным.
   *
   * @return boolean - признак анимированного изображения
   */
  public boolean isAnimated() {
    return image.count() > 1;
  }

  /**
   * Возвращает строку подписи 1.
   *
   * @return String - строка подписи 1
   */
  public String label1() {
    return label1;
  }

  /**
   * Возвращает строку подписи 2.
   *
   * @return String - строка подписи 2
   */
  public String label2() {
    return label2;
  }

  /**
   * Возвращает произвольные пользовательские данные.
   *
   * @return Object - произвольные пользовательские данные, может быть null
   */
  public Object userData() {
    return userData;
  }

  /**
   * Возвращает ширину изображения.
   *
   * @return int - ширина изображения в пикселях
   */
  public int width() {
    return width;
  }

  /**
   * Возвращает высота изображения.
   *
   * @return int - высота изображения в пикселях
   */
  public int height() {
    return height;
  }

}
