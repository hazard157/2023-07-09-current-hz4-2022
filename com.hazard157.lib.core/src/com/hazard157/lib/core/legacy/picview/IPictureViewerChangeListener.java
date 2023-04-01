package com.hazard157.lib.core.legacy.picview;

/**
 * Слушатель изменения параметров просмотра {@link PictureViewer}.
 *
 * @author hazard157
 */
public interface IPictureViewerChangeListener {

  /**
   * Вызвается при изменении отображаемой картинки.
   * <p>
   * <b>Не</b> вызвается при смене кадров!
   *
   * @param aSource {@link PictureViewer} - источник сообщения
   */
  void onPictureViewerImageChanged( PictureViewer aSource );

  /**
   * Вызвается при изменении параметров просмотра изображения.
   * <p>
   * К параметрам просмотра относятся следующие:
   * <ul>
   * <li>{@link PictureViewer#getZoom()} - масштаб изображения;</li>
   * <li>{@link PictureViewer#getExpandToFit()} - =увеличивается ли маленькая картинка в режиме "вместить".</li>
   * </ul>
   *
   * @param aSource {@link PictureViewer} - источник сообщения
   */
  void onPictureViewerParamChanged( PictureViewer aSource );

}
