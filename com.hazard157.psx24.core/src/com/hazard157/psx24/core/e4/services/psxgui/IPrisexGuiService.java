package com.hazard157.psx24.core.e4.services.psxgui;

/**
 * Поддержка обобщенного (между всеми версиями) понятий GUI приложения PRISEX.
 * <p>
 * Это общий интерфейс, ссылка на который должна находится в контексте окна. Имплементация сервиса в каждой версии
 * PRISEX - своя.
 *
 * @author goga
 */
public interface IPrisexGuiService {

  /**
   * Переключает в перспективу просмотра списка эпизодов с детальной информацией об эпизодах.
   */
  void switchToEpisodesListPerspective();

}
