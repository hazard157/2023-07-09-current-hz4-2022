package com.hazard157.lib.core.legacy.picview;

import org.eclipse.swt.events.*;

/**
 * Слушатель щелчков на {@link PictureViewer}.
 *
 * @author goga
 */
public interface IPictureViewerMouseListener {

  /**
   * Вызвается при щелчке на изображении или подписи к ней..
   * <p>
   * <b>Не</b> вызвается при смене кадров!
   *
   * @param aSource {@link PictureViewer} - источник сообщения
   * @param aIsDoubleClick boolean - признак двойного, а не одиночного нажатия
   * @param aStateMask int - {@link MouseEvent#stateMask}
   * @param aX int - {@link MouseEvent#x}
   * @param aY int - {@link MouseEvent#y}
   */
  void onPictureViewerMouseClick( PictureViewer aSource, boolean aIsDoubleClick, int aStateMask, int aX, int aY );

  /**
   * Вызывается когда пользователь прокрутил колеско мыши.
   *
   * @param aSource {@link PictureViewer} - источник сообщения
   * @param aStateMask int - {@link MouseEvent#stateMask}
   * @param aScrollLines ште - кол-во прокручиваемых строк (отрицательные значения - прокрутка вниз)
   */
  default void onPictureViewerMouseWheel( PictureViewer aSource, int aScrollLines, int aStateMask ) {
    // nop
  }

}
