package com.hazard157.psx24.core.e4.services.prisex;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link PrisexService}
   */
  String FMT_ERR_NO_SUCH_EPISODE                = "Нет эпизода с идентификатором '%s'";
  String FMT_ERR_NO_SOURCE_VIDEOS               = "Для эпизода '%s' не задано не одно исходное видео";
  String FMT_ERR_NO_SOURCE_VIDEO                = "Для эпизода '%s' не задано исходное видео для камеры '%s'";
  String FMT_ERR_NO_SOURCE_VIDEO_FILE           = "Для эпизода '%s' не существует файла исходное видео '%s'";
  String FMT_ERR_SOURCE_VIDEO_FILE_INACCESSABLE = "Недоступен для чтения файлисходного видео '%s'";
  String FMT_ERR_NO_TRAILER_FILE                = "Для трейлера '%s' не существует видео-файла";
  String FMT_ERR_TRAILER_FILE_INACCESSABLE      = "Недоступен для чтения видео-файл '%s'";
  String FMT_ERR_NO_FILM_FILE                   = "Для фильма '%s' не существует видео-файла";
  String FMT_ERR_FILM_FILE_INACCESSABLE         = "Недоступен для чтения видео-файл '%s'";

}
