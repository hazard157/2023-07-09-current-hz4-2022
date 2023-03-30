package com.hazard157.lib.core.legacy.picview;

import org.toxsoft.core.tsgui.panels.toolbar.*;

/**
 * Обработчик пользовательских дйествий, заданных при созданиий {@link PictureViewer}.
 *
 * @author goga
 */
public interface IPictureViewerActionsHandler {

  /**
   * Никакой обработчик - ничего не делает.
   */
  IPictureViewerActionsHandler NONE = new IPictureViewerActionsHandler() {

    @Override
    public void updateActionsState( PictureViewer aSource, TsToolbar aToolbar ) {
      // nop
    }

    @Override
    public void handleAction( PictureViewer aSource, String aActionId ) {
      // nop
    }
  };

  /**
   * Вызывается, когда надо обновить состояние действий.
   *
   * @param aSource {@link PictureViewer} - источник сообщения
   * @param aToolbar {@link TsToolbar} - панель инструментов, на которых находятся пользовательские действия
   */
  void updateActionsState( PictureViewer aSource, TsToolbar aToolbar );

  /**
   * Вызывается когда надо обработать пользовательское действие.
   *
   * @param aSource {@link PictureViewer} - источник сообщения
   * @param aActionId String - идентификатор пользовательского действия
   */
  void handleAction( PictureViewer aSource, String aActionId );

}
